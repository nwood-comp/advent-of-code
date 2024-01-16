package common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class IntCoord3D {

    public static final IntCoord3D ZERO = new IntCoord3D(IntCoord2D.ZERO, 0);
    public static final IntCoord3D U = new IntCoord3D(IntCoord2D.ZERO, 1);
    public static final IntCoord3D D = new IntCoord3D(IntCoord2D.ZERO, -1);
    public static final IntCoord3D L = new IntCoord3D(IntCoord2D.L, 0);
    public static final IntCoord3D R = new IntCoord3D(IntCoord2D.R, 0);
    public static final IntCoord3D F = new IntCoord3D(IntCoord2D.U, 0);
    public static final IntCoord3D B = new IntCoord3D(IntCoord2D.D, 0);
    public static final IntCoord3D FL = new IntCoord3D(IntCoord2D.UL, 0);
    public static final IntCoord3D FR = new IntCoord3D(IntCoord2D.UR, 0);
    public static final IntCoord3D BL = new IntCoord3D(IntCoord2D.DL, 0);
    public static final IntCoord3D BR = new IntCoord3D(IntCoord2D.DR, 0);
    public static final IntCoord3D UL = new IntCoord3D(IntCoord2D.L, 1);
    public static final IntCoord3D UR = new IntCoord3D(IntCoord2D.R, 1);
    public static final IntCoord3D UF = new IntCoord3D(IntCoord2D.U, 1);
    public static final IntCoord3D UB = new IntCoord3D(IntCoord2D.D, 1);
    public static final IntCoord3D UFL = new IntCoord3D(IntCoord2D.UL, 1);
    public static final IntCoord3D UFR = new IntCoord3D(IntCoord2D.UR, 1);
    public static final IntCoord3D UBL = new IntCoord3D(IntCoord2D.DL, 1);
    public static final IntCoord3D UBR = new IntCoord3D(IntCoord2D.DR, 1);
    public static final IntCoord3D DL = new IntCoord3D(IntCoord2D.L, -1);
    public static final IntCoord3D DR = new IntCoord3D(IntCoord2D.R, -1);
    public static final IntCoord3D DF = new IntCoord3D(IntCoord2D.U, -1);
    public static final IntCoord3D DB = new IntCoord3D(IntCoord2D.D, -1);
    public static final IntCoord3D DFL = new IntCoord3D(IntCoord2D.UL, -1);
    public static final IntCoord3D DFR = new IntCoord3D(IntCoord2D.UR, -1);
    public static final IntCoord3D DBL = new IntCoord3D(IntCoord2D.DL, -1);
    public static final IntCoord3D DBR = new IntCoord3D(IntCoord2D.DR, -1);

    public static final IntCoord3D N = F;
    public static final IntCoord3D S = B;
    public static final IntCoord3D W = L;
    public static final IntCoord3D E = R;
    public static final IntCoord3D NE = FR;
    public static final IntCoord3D SE = BR;
    public static final IntCoord3D SW = BL;
    public static final IntCoord3D NW = FL;
    public static final IntCoord3D UN = UF;
    public static final IntCoord3D US = UB;
    public static final IntCoord3D UW = UL;
    public static final IntCoord3D UE = UR;
    public static final IntCoord3D UNE = UFR;
    public static final IntCoord3D USE = UBR;
    public static final IntCoord3D USW = UBL;
    public static final IntCoord3D UNW = UFL;
    public static final IntCoord3D DN = DF;
    public static final IntCoord3D DS = DB;
    public static final IntCoord3D DW = DL;
    public static final IntCoord3D DE = DR;
    public static final IntCoord3D DNE = DFR;
    public static final IntCoord3D DSE = DBR;
    public static final IntCoord3D DSW = DBL;
    public static final IntCoord3D DNW = DFL;

    public static final List<IntCoord3D> DIRECTIONS_6 = List.of(F, R, B, L, U, D);
    public static final List<IntCoord3D> DIRECTIONS_26 = List.of(
            F, FR, R, BR, B, BL, L, FL,
            U, UF, UFR, UR, UBR, UB, UBL, UL, UFL,
            D, DF, DFR, DR, DBR, DB, DBL, DL, DFL);

    private final IntCoord2D xy;
    private final int z;

    private IntCoord3D(int x, int y, int z) {
        this(IntCoord2D.of(x, y), z);
    }

    private IntCoord3D(IntCoord2D xy, int z) {
        this.xy = Objects.requireNonNull(xy, "xy");
        this.z = z;
    }

    public final int x() {
        return this.xy.x();
    }

    public final int y() {
        return this.xy.y();
    }

    public final int z() {
        return this.z;
    }

    public final IntCoord2D xy() {
        return this.xy;
    }

    public final int manhattanMagnitude() {
        return ZERO.manhattanDistanceTo(this);
    }

    public final int manhattanDistanceTo(IntCoord3D other) {
        Objects.requireNonNull(other, "other");
        final int dxy = this.xy.manhattanDistanceTo(other.xy);
        final int dz = Math.subtractExact(other.z, this.z);
        if (dz == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return Math.addExact(dxy, Math.abs(dz));
    }

    public final int magnitudeSquared() {
        return dot(this);
    }

    public final int distanceSquaredTo(IntCoord3D other) {
        final IntCoord3D translation = translationTo(other);
        return translation.magnitudeSquared();
    }

    public final IntCoord3D scale(int factor) {
        return of(Math.multiplyExact(this.x(), factor), Math.multiplyExact(this.y(), factor), Math.multiplyExact(this.z, factor));
    }

    public final IntCoord3D translate(int x, int y, int z) {
        return of(this.xy.translate(x, y), Math.addExact(this.z, z));
    }

    public final IntCoord3D translate(IntCoord3D other) {
        return add(other);
    }

    public final IntCoord3D translationFrom(IntCoord3D other) {
        return subtract(other);
    }

    public final IntCoord3D translationTo(IntCoord3D other) {
        return Objects.requireNonNull(other, "other").subtract(this);
    }

    public final IntCoord3D add(IntCoord3D other) {
        Objects.requireNonNull(other, "other");
        return of(this.xy.add(other.xy), Math.addExact(this.z, other.z));
    }

    public final IntCoord3D subtract(IntCoord3D other) {
        Objects.requireNonNull(other, "other");
        return of(this.xy.subtract(other.xy), Math.subtractExact(this.z, other.z));
    }

    public final int dot(IntCoord3D other) {
        Objects.requireNonNull(other, "other");
        return Math.addExact(this.xy.dot(other.xy), Math.multiplyExact(this.z, other.z));
    }

    public final IntCoord3D cross(IntCoord3D other) {
        Objects.requireNonNull(other, "other");
        final int newX = Math.subtractExact(Math.multiplyExact(y(), other.z), Math.multiplyExact(this.z, other.y()));
        final int newY = Math.subtractExact(Math.multiplyExact(this.z, other.x()), Math.multiplyExact(x(), other.z));
        final int newZ = this.xy.cross(other.xy);
        return of(newX, newY, newZ);
    }

    public final List<IntCoord3D> getNeighbors(boolean allowDiagonals) {
        return (allowDiagonals ? getAllNeighbors() : getCardinalNeighbors());
    }

    public final List<IntCoord3D> getCardinalNeighbors() {
        return DIRECTIONS_6.stream()
                .map(this::add)
                .collect(Collectors.toList());
    }

    public final List<IntCoord3D> getAllNeighbors() {
        return DIRECTIONS_26.stream()
                .map(this::add)
                .collect(Collectors.toList());
    }

    public final IntCoord3D reduce() {
        final int gcd = MathUtil.binaryGcd(MathUtil.binaryGcd(x(), y()), z());
        if (gcd < 2) {
            return this;
        }
        return of(x() / gcd, y() / gcd, z() / gcd);
    }

    public final List<IntCoord3D> latticePointsTo(IntCoord3D destination) {
        Objects.requireNonNull(destination, "destination");
        final List<IntCoord3D> out = new ArrayList<>();
        out.add(this);
        if (!this.equals(destination)) {
            final IntCoord3D reduced = this.translationTo(destination).reduce();
            IntCoord3D next = this;
            do {
                next = next.translate(reduced);
                out.add(next);
            } while (!next.equals(destination));
        }
        return out;
    }

    public IntCoord3D rotate90(IntCoord3D axis) {
        return rotation90(axis).apply(this);
    }

    public IntCoord3D rotate90(IntCoord3D origin, IntCoord3D axisDirectionVector) {
        return rotation90(origin, axisDirectionVector).apply(this);
    }

    public IntCoord3D rotate180(IntCoord3D axis) {
        return rotation180(axis).apply(this);
    }

    public IntCoord3D rotate180(IntCoord3D origin, IntCoord3D axisDirectionVector) {
        return rotation180(origin, axisDirectionVector).apply(this);
    }

    public IntCoord3D rotate270(IntCoord3D axis) {
        return rotation270(axis).apply(this);
    }

    public IntCoord3D rotate270(IntCoord3D origin, IntCoord3D axisDirectionVector) {
        return rotation270(origin, axisDirectionVector).apply(this);
    }

    @Override
    public final int hashCode() {
        return (31 * this.xy.hashCode()) + this.z;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        final IntCoord3D other = (IntCoord3D) o;
        return (this.xy.equals(other.xy) && this.z == other.z);
    }

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
        return new StringBuilder().append('(').append(x()).append(',').append(y()).append(',').append(this.z).append(')').toString();
    }

    public static IntCoord3D of(int x, int y, int z) {
        return new IntCoord3D(x, y, z);
    }

    public static IntCoord3D of(IntCoord2D xy, int z) {
        return new IntCoord3D(Objects.requireNonNull(xy, "xy"), z);
    }

    public static IntCoord3D ofNoZ(IntCoord2D xy) {
        return of(xy, 0);
    }

    public static List<IntCoord3D> rotate90(Collection<IntCoord3D> coordinates, IntCoord3D axis) {
        return Objects.requireNonNull(coordinates, "coordinates").stream()
                .map(rotation90(axis))
                .collect(Collectors.toList());
    }

    public static List<IntCoord3D> rotate180(Collection<IntCoord3D> coordinates, IntCoord3D axis) {
        return Objects.requireNonNull(coordinates, "coordinates").stream()
                .map(rotation180(axis))
                .collect(Collectors.toList());
    }

    public static List<IntCoord3D> rotate270(Collection<IntCoord3D> coordinates, IntCoord3D axis) {
        return Objects.requireNonNull(coordinates, "coordinates").stream()
                .map(rotation270(axis))
                .collect(Collectors.toList());
    }

    public static List<IntCoord3D> rotate90(Collection<IntCoord3D> coordinates, IntCoord3D origin, IntCoord3D axisDirectionVector) {
        return Objects.requireNonNull(coordinates, "coordinates").stream()
                .map(rotation90(origin, axisDirectionVector))
                .collect(Collectors.toList());
    }

    public static List<IntCoord3D> rotate180(Collection<IntCoord3D> coordinates, IntCoord3D origin, IntCoord3D axisDirectionVector) {
        return Objects.requireNonNull(coordinates, "coordinates").stream()
                .map(rotation180(origin, axisDirectionVector))
                .collect(Collectors.toList());
    }

    public static List<IntCoord3D> rotate270(Collection<IntCoord3D> coordinates, IntCoord3D origin, IntCoord3D axisDirectionVector) {
        return Objects.requireNonNull(coordinates, "coordinates").stream()
                .map(rotation270(origin, axisDirectionVector))
                .collect(Collectors.toList());
    }

    private static Function<IntCoord3D, IntCoord3D> addition(IntCoord3D addend) {
        Objects.requireNonNull(addend, "addend");
        return (it -> it.add(addend));
    }

    private static Function<IntCoord3D, IntCoord3D> subtraction(IntCoord3D subtrahend) {
        Objects.requireNonNull(subtrahend, "subtrahend");
        return (it -> it.subtract(subtrahend));
    }

    private static Function<IntCoord3D, IntCoord3D> rotation90(IntCoord3D axis) {
        Objects.requireNonNull(axis, "axis");
        // https://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle
        // Let dx = axis.x()
        // Let dy = axis.y()
        // Let dz = axis.z()
        // Let m = sqrt(dx*dx + dy*dy + dz*dz)
        // Let a = ux = dx/m
        // Let b = uy = dy/m
        // Let c = uz = dz/m
        // Here, theta is 90, so our matrix R becomes:
        // [ a^2 ab-c ac+b ]
        // [ba+c b^2  bc-a ]
        // [ca-b cb+a c^2  ]
        // Let's define Q = m^2 R.
        // Then Q is:
        // [ (dx)^2            (dx)(dy)-(m)(dz)  (dx)(dz)+(m)(dy) ]
        // [ (dx)(dy)+(m)(dz)  (dy)^2            (dy)(dz)-(m)(dx) ]
        // [ (dx)(dz)-(m)(dy)  (dy)(dz)+(m)(dx)  (dz)^2           ]
        // Now, if we multiply Q instead of R by each coordinate, we must remember to divide by m^2 eventually.
        // Let's compute q:
        final int m2 = axis.magnitudeSquared();
        if (m2 == 0) {
            throw new IllegalArgumentException(
                    String.format(
                            "invalid axis: %s (must not be zero-magnitude)",
                            axis));
        }
        final int m = (int) Math.sqrt(m2);
        if (Math.multiplyExact(m, m) != m2) {
            throw new IllegalArgumentException(
                    String.format(
                            "invalid axis: %s (does not support integer rotation due to non-integer magnitude)",
                            axis));
        }
        final int dx = axis.x();
        final int dy = axis.y();
        final int dz = axis.z();
        final int dx2 = Math.multiplyExact(dx, dx);
        final int dy2 = Math.multiplyExact(dy, dy);
        final int dz2 = Math.multiplyExact(dz, dz);
        final int dxdy = Math.multiplyExact(dx, dy);
        final int dxdz = Math.multiplyExact(dx, dz);
        final int dydz = Math.multiplyExact(dy, dz);
        final int mdx = Math.multiplyExact(m, dx);
        final int mdy = Math.multiplyExact(m, dy);
        final int mdz = Math.multiplyExact(m, dz);
        final int[][] q = new int[][] {
                /* Row 1 */ { dx2, Math.subtractExact(dxdy, mdz), Math.addExact(dxdz, mdy) },
                /* Row 2 */ { Math.addExact(dxdy, mdz), dy2, Math.subtractExact(dydz, mdx) },
                /* Row 3 */ { Math.subtractExact(dxdz, mdy), Math.addExact(dydz, mdx), dz2 }
        };
        // Now let's define a function to perform the rotation (our return value) so it can be
        // reused efficiently on multiple points in case we are rotating multiple points. This
        // saves the effort of recomputing our matrix q for each rotation operation.
        return (a) -> {
            final int ax = a.x();
            final int ay = a.y();
            final int az = a.z();
            final int[] qa = new int[] {
                    addExact(Math.multiplyExact(q[0][0], ax), Math.multiplyExact(q[0][1], ay), Math.multiplyExact(q[0][2], az)),
                    addExact(Math.multiplyExact(q[1][0], ax), Math.multiplyExact(q[1][1], ay), Math.multiplyExact(q[1][2], az)),
                    addExact(Math.multiplyExact(q[2][0], ax), Math.multiplyExact(q[2][1], ay), Math.multiplyExact(q[2][2], az))
            };
            final Integer rax = divideIfDivisible(qa[0], m2);
            final Integer ray = divideIfDivisible(qa[1], m2);
            final Integer raz = divideIfDivisible(qa[2], m2);
            if (rax == null || ray == null || raz == null) {
                throw new IllegalArgumentException(
                        String.format(
                                "coordinate %s does not have an integer rotation around %s",
                                a, axis));
            }
            return IntCoord3D.of(rax, ray, raz);
        };
    }

    private static Function<IntCoord3D, IntCoord3D> rotation180(IntCoord3D axis) {
        Objects.requireNonNull(axis, "axis");
        final Function<IntCoord3D, IntCoord3D> rotation90 = rotation90(axis);
        return rotation90.andThen(rotation90);
    }

    private static Function<IntCoord3D, IntCoord3D> rotation270(IntCoord3D axis) {
        Objects.requireNonNull(axis, "axis");
        final Function<IntCoord3D, IntCoord3D> rotation90 = rotation90(axis);
        return rotation90.andThen(rotation90).andThen(rotation90);
    }

    private static Function<IntCoord3D, IntCoord3D> rotation90(IntCoord3D origin, IntCoord3D axisDirectionVector) {
        Objects.requireNonNull(origin, "origin");
        Objects.requireNonNull(origin, "axis direction vector");
        return subtraction(origin)
                .andThen(rotation90(axisDirectionVector))
                .andThen(addition(origin));
    }

    private static Function<IntCoord3D, IntCoord3D> rotation180(IntCoord3D origin, IntCoord3D axisDirectionVector) {
        Objects.requireNonNull(origin, "origin");
        Objects.requireNonNull(origin, "axis direction vector");
        return subtraction(origin)
                .andThen(rotation180(axisDirectionVector))
                .andThen(addition(origin));
    }

    private static Function<IntCoord3D, IntCoord3D> rotation270(IntCoord3D origin, IntCoord3D axisDirectionVector) {
        Objects.requireNonNull(origin, "origin");
        Objects.requireNonNull(origin, "axis direction vector");
        return subtraction(origin)
                .andThen(rotation270(axisDirectionVector))
                .andThen(addition(origin));
    }

    private static int addExact(int a, int b, int c) {
        return Math.addExact(Math.addExact(a, b), c);
    }

    private static Integer divideIfDivisible(int a, int b) {
        if (b == 0) {
            return null;
        }
        final int r = a % b;
        if (r != 0) {
            return null;
        }
        return (a / b);
    }
}