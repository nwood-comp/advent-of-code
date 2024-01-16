package common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class IntCoord2D {

    public static final IntCoord2D ZERO = new IntCoord2D(0, 0);
    public static final IntCoord2D U = new IntCoord2D(0, 1);
    public static final IntCoord2D D = new IntCoord2D(0, -1);
    public static final IntCoord2D L = new IntCoord2D(-1, 0);
    public static final IntCoord2D R = new IntCoord2D(1, 0);
    public static final IntCoord2D N = U;
    public static final IntCoord2D S = D;
    public static final IntCoord2D W = L;
    public static final IntCoord2D E = R;
    public static final IntCoord2D UL = new IntCoord2D(-1, 1);
    public static final IntCoord2D UR = new IntCoord2D(1, 1);
    public static final IntCoord2D DL = new IntCoord2D(-1, -1);
    public static final IntCoord2D DR = new IntCoord2D(1, -1);
    public static final IntCoord2D LU = UL;
    public static final IntCoord2D RU = UR;
    public static final IntCoord2D LD = DL;
    public static final IntCoord2D RD = DR;
    public static final IntCoord2D NW = UL;
    public static final IntCoord2D NE = UR;
    public static final IntCoord2D SW = DL;
    public static final IntCoord2D SE = DR;

    public static final List<IntCoord2D> DIRECTIONS_4 = List.of(U, R, D, L);
    public static final List<IntCoord2D> DIRECTIONS_8 = List.of(U, UR, R, DR, D, DL, L, UL);
    public static final List<IntCoord2D> DIRECTIONS_DIAGONAL_4 = List.of(UR, DR, DL,UL);

    private final int x;
    private final int y;

    private final int hashCode;

    private IntCoord2D(int x, int y) {
        this.x = x;
        this.y = y;
        this.hashCode = (46337 * this.x) + this.y;
    }

    public final int x() {
        return this.x;
    }

    public final int y() {
        return this.y;
    }

    public final int manhattanMagnitude() {
        return ZERO.manhattanDistanceTo(this);
    }

    public final int manhattanDistanceTo(IntCoord2D other) {
        Objects.requireNonNull(other, "other");
        final int dx = Math.subtractExact(other.x, this.x);
        final int dy = Math.subtractExact(other.y, this.y);
        if (dx == Integer.MIN_VALUE || dy == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return Math.addExact(Math.abs(dx), Math.abs(dy));
    }

    public final int magnitudeSquared() {
        return dot(this);
    }

    public final int distanceSquaredTo(IntCoord2D other) {
        final IntCoord2D translation = translationTo(other);
        return translation.magnitudeSquared();
    }

    public final IntCoord2D scale(int factor) {
        return of(Math.multiplyExact(this.x, factor), Math.multiplyExact(this.y, factor));
    }

    public final IntCoord2D translate(int x, int y) {
        return of(this.x + x, this.y + y);
    }

    public final IntCoord2D translate(IntCoord2D other) {
        return add(other);
    }

    public final IntCoord2D translationFrom(IntCoord2D other) {
        return subtract(other);
    }

    public final IntCoord2D translationTo(IntCoord2D other) {
        return Objects.requireNonNull(other, "other").subtract(this);
    }

    public final IntCoord2D add(IntCoord2D other) {
        Objects.requireNonNull(other, "other");
        return of(Math.addExact(this.x, other.x), Math.addExact(this.y, other.y));
    }

    public final IntCoord2D subtract(IntCoord2D other) {
        Objects.requireNonNull(other, "other");
        return of(Math.subtractExact(this.x, other.x), Math.subtractExact(this.y, other.y));
    }

    public final int dot(IntCoord2D other) {
        Objects.requireNonNull(other, "other");
        return Math.addExact(Math.multiplyExact(this.x, other.x), Math.multiplyExact(this.y, other.y));
    }

    public final int cross(IntCoord2D other) {
        Objects.requireNonNull(other, "other");
        return Math.subtractExact(Math.multiplyExact(this.x, other.y), Math.multiplyExact(other.x, this.y));
    }

    public final List<IntCoord2D> getNeighbors(boolean allowDiagonals) {
        return (allowDiagonals ? getAllNeighbors() : getCardinalNeighbors());
    }

    public final List<IntCoord2D> getCardinalNeighbors() {
        return DIRECTIONS_4.stream()
                .map(this::add)
                .collect(Collectors.toList());
    }

    public final List<IntCoord2D> getAllNeighbors() {
        return DIRECTIONS_8.stream()
                .map(this::add)
                .collect(Collectors.toList());
    }


    public final List<IntCoord2D> getDiagonalNeighbors() {
        return DIRECTIONS_DIAGONAL_4.stream()
                .map(this::add)
                .collect(Collectors.toList());
    }

    public final IntCoord2D reduce() {
        final int gcd = MathUtil.binaryGcd(this.x, this.y);
        if (gcd < 2) {
            return this;
        }
        return of(this.x / gcd, this.y / gcd);
    }

    public final List<IntCoord2D> latticePointsTo(IntCoord2D destination) {
        Objects.requireNonNull(destination, "destination");
        final List<IntCoord2D> out = new ArrayList<>();
        out.add(this);
        if (!this.equals(destination)) {
            final IntCoord2D reduced = this.translationTo(destination).reduce();
            IntCoord2D next = this;
            do {
                next = next.translate(reduced);
                out.add(next);
            } while (!next.equals(destination));
        }
        return out;
    }

    public final boolean indexes(boolean[][] arrayYX) {
        return (arrayYX != null &&
                this.x >= 0 &&
                this.y >= 0 &&
                this.y < arrayYX.length &&
                arrayYX[this.y] != null &&
                this.x < arrayYX[this.y].length);
    }

    public final boolean indexes(int[][] arrayYX) {
        return (arrayYX != null &&
                this.x >= 0 &&
                this.y >= 0 &&
                this.y < arrayYX.length &&
                arrayYX[this.y] != null &&
                this.x < arrayYX[this.y].length);
    }

    public final boolean indexes(long[][] arrayYX) {
        return (arrayYX != null &&
                this.x >= 0 &&
                this.y >= 0 &&
                this.y < arrayYX.length &&
                arrayYX[this.y] != null &&
                this.x < arrayYX[this.y].length);
    }

    public final boolean indexes(double[][] arrayYX) {
        return (arrayYX != null &&
                this.x >= 0 &&
                this.y >= 0 &&
                this.y < arrayYX.length &&
                arrayYX[this.y] != null &&
                this.x < arrayYX[this.y].length);
    }

    public final <T> boolean indexes(T[][] arrayYX) {
        return (arrayYX != null &&
                this.x >= 0 &&
                this.y >= 0 &&
                this.y < arrayYX.length &&
                arrayYX[this.y] != null &&
                this.x < arrayYX[this.y].length);
    }

    public final boolean indexing(boolean[][] arrayYX) {
        if (!indexes(arrayYX)) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s does not index %s x %s array",
                            this,
                            arrayYX == null ? null : arrayYX.length,
                            arrayYX == null ? null : (
                                    arrayYX.length == 0 ? null : (
                                            arrayYX[0] == null ? null : arrayYX[0].length))));
        }
        return arrayYX[this.y][this.x];
    }

    public final int indexing(int[][] arrayYX) {
        if (!indexes(arrayYX)) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s does not index %s x %s array",
                            this,
                            arrayYX == null ? null : arrayYX.length,
                            arrayYX == null ? null : (
                                    arrayYX.length == 0 ? null : (
                                            arrayYX[0] == null ? null : arrayYX[0].length))));
        }
        return arrayYX[this.y][this.x];
    }

    public final long indexing(long[][] arrayYX) {
        if (!indexes(arrayYX)) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s does not index %s x %s array",
                            this,
                            arrayYX == null ? null : arrayYX.length,
                            arrayYX == null ? null : (
                                    arrayYX.length == 0 ? null : (
                                            arrayYX[0] == null ? null : arrayYX[0].length))));
        }
        return arrayYX[this.y][this.x];
    }

    public final double indexing(double[][] arrayYX) {
        if (!indexes(arrayYX)) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s does not index %s x %s array",
                            this,
                            arrayYX == null ? null : arrayYX.length,
                            arrayYX == null ? null : (
                                    arrayYX.length == 0 ? null : (
                                            arrayYX[0] == null ? null : arrayYX[0].length))));
        }
        return arrayYX[this.y][this.x];
    }

    public final <T> T indexing(T[][] arrayYX) {
        if (!indexes(arrayYX)) {
            throw new IllegalArgumentException(
                    String.format(
                            "%s does not index %s x %s array",
                            this,
                            arrayYX == null ? null : arrayYX.length,
                            arrayYX == null ? null : (
                                    arrayYX.length == 0 ? null : (
                                            arrayYX[0] == null ? null : arrayYX[0].length))));
        }
        return arrayYX[this.y][this.x];
    }

    /**
     * Produces the coordinate resulting from rotating the current coordinate around the origin a
     * quarter circle in a direction from the positive x-axis to the positive y-axis.
     *
     * @return the coordinate rotated 90 degrees around the origin respecting the right-hand rule
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public final IntCoord2D rotate90() {
        return IntCoord2D.of(-this.y, this.x);
    }

    /**
     * Produces the coordinate resulting from rotating the current coordinate around the origin a
     * half circle in a direction from the positive x-axis to the positive y-axis.
     *
     * @return the coordinate rotated 180 degrees around the origin respecting the right-hand rule
     */
    public final IntCoord2D rotate180() {
        return IntCoord2D.of(-this.x, -this.y);
    }

    /**
     * Produces the coordinate resulting from rotating the current coordinate around the origin
     * three-quarters of a circle in a direction from the positive x-axis to the positive y-axis.
     * This is the same as rotating a quarter circle in a direction from the positive y-axis to
     * the positive x-axis.
     *
     * @return the coordinate rotated 270 degrees around the origin respecting the right-hand rule
     */
    @SuppressWarnings("SuspiciousNameCombination")
    public final IntCoord2D rotate270() {
        return IntCoord2D.of(this.y, -this.x);
    }

    public final IntCoord2D rotateRight90(boolean graphicsY) {
        return (graphicsY ? rotate90() : rotate270());
    }

    public final IntCoord2D rotateLeft90(boolean graphicsY) {
        return (graphicsY ? rotate270() : rotate90());
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        final IntCoord2D other = (IntCoord2D) o;
        return (this.x == other.x && this.y == other.y);
    }

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
        return new StringBuilder().append('(').append(this.x).append(',').append(this.y).append(')').toString();
    }

    public static IntCoord2D of(int x, int y) {
        return new IntCoord2D(x, y);
    }

    public static IntCoord2D parse(String s) {
        if (s == null) {
            return null;
        }
        String trimmed = s.trim();
        if (trimmed.length() == 1) {
            final char ch = s.charAt(0);
            if (ch == 'Z' || ch == 'z' || ch == '0') {
                return ZERO;
            } else if (ch == 'U' || ch == 'u' || ch == 'N' || ch == 'n') {
                return U;
            } else if (ch == 'D' || ch == 'd' || ch == 'S' || ch == 's') {
                return D;
            } else if (ch == 'L' || ch == 'l' || ch == 'W' || ch == 'w') {
                return L;
            } else if (ch == 'R' || ch == 'r' || ch == 'E' || ch == 'e') {
                return R;
            }
        } else if (trimmed.length() == 2) {
            final char ch = s.charAt(0);
            final char ch2 = s.charAt(1);
            if (ch == 'U' || ch == 'u') {
                if (ch2 == 'L' || ch2 == 'l') {
                    return UL;
                } else if (ch2 == 'R' || ch2 == 'r') {
                    return UR;
                }
            } else if (ch == 'D' || ch == 'd') {
                if (ch2 == 'L' || ch2 == 'l') {
                    return DL;
                } else if (ch2 == 'R' || ch2 == 'r') {
                    return DR;
                }
            } else if (ch == 'L' || ch == 'l') {
                if (ch2 == 'U' || ch2 == 'u') {
                    return UL;
                } else if (ch2 == 'D' || ch2 == 'd') {
                    return DL;
                }
            } else if (ch == 'R' || ch == 'r') {
                if (ch2 == 'U' || ch2 == 'u') {
                    return UR;
                } else if (ch2 == 'D' || ch2 == 'd') {
                    return DR;
                }
            } else if (ch == 'N' || ch == 'n') {
                if (ch2 == 'W' || ch2 == 'w') {
                    return UL;
                } else if (ch2 == 'E' || ch2 == 'e') {
                    return UR;
                }
            } else if (ch == 'S' || ch == 's') {
                if (ch2 == 'W' || ch2 == 'w') {
                    return DL;
                } else if (ch2 == 'E' || ch2 == 'e') {
                    return DR;
                }
            }
        } else {
            if (trimmed.equalsIgnoreCase("ZERO")) {
                return IntCoord2D.ZERO;
            }
            boolean parenthesized = false;
            if (trimmed.charAt(0) == '(') {
                if (trimmed.endsWith(")")) {
                    trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
                    parenthesized = true;
                }
            }
            final int comma = trimmed.indexOf(',');
            if (comma > 0) {
                try {
                    final int x = Integer.parseInt(trimmed.substring(0, comma).trim());
                    final int y = Integer.parseInt(trimmed.substring(comma + 1).trim());
                    return of(x, y);
                } catch (Exception e) {
                    if (parenthesized) {
                        throw new IllegalArgumentException(String.format("invalid coordinate string: '%s' (cannot be parsed as an '(x,y)' string)", s), e);
                    } else {
                        throw new IllegalArgumentException(String.format("invalid coordinate string: '%s' (cannot be parsed as an 'x,y' string)", s), e);
                    }
                }
            } else if (!parenthesized) {
                // Handle the form: 'NN X' where NN is a sign or signed integer or unsigned integer, and
                // X is a valid coordinate under parsing.  Examples: '4N' or '2L' or '-3NW' or '+4UL'
                int val = 0;
                boolean negative = false;
                boolean signed = false;
                if (trimmed.charAt(0) == '-') {
                    negative = true;
                    signed = true;
                    trimmed = trimmed.substring(1).trim();
                } else if (trimmed.charAt(0) == '+') {
                    signed = true;
                    trimmed = trimmed.substring(1).trim();
                }
                if (!trimmed.isEmpty() && (signed || Character.isDigit(trimmed.charAt(0)))) {
                    try {
                        boolean digitized = false;
                        while (!trimmed.isEmpty() && Character.isDigit(trimmed.charAt(0))) {
                            int d = trimmed.charAt(0) - '0';
                            val = Math.addExact(negative ? -d : d, Math.multiplyExact(val, 10));
                            trimmed = trimmed.substring(1); // do not trim
                            digitized = true;
                        }
                        if (signed && !digitized) {
                            val = (negative ? -1 : 1);
                        }
                        trimmed = trimmed.trim();
                        // We only get here if no comma, as we are not handling things like -(1,2) or 2(1,2).
                        // Otherwise too hard to distinguish from -1,2.  So we can enforce 1 <= length <= 2 here
                        // after checking for ZERO.
                        if (!trimmed.isEmpty() && (trimmed.length() < 3 || trimmed.equalsIgnoreCase("ZERO"))) {
                            return parse(trimmed).scale(val);
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException(String.format("invalid coordinate string: '%s' (unable to parse)", s), e);
                    }
                }
            }
        }
        throw new IllegalArgumentException(String.format("invalid coordinate string: '%s' (unable to parse)", s));
    }
}