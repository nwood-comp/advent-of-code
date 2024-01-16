package common;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;

@SuppressWarnings("unused")
public class IntGrid2D<V> implements Map<IntCoord2D, V>, Cloneable {

    private TreeMap<IntCoord2D, V> map;
    private int maxX;
    private int maxY;

    public IntGrid2D() {
        this.map = new TreeMap<>(Comparator.comparing(IntCoord2D::y).thenComparing(IntCoord2D::x));
    }

    public IntGrid2D(final Map<IntCoord2D, ? extends V> map) {
        this();
        this.map.putAll(map);
    }

    int[] cachedExtrema;

    public final void cacheExtrema() {
        this.cachedExtrema = extrema();
        this.maxX = cachedExtrema[2] + 1;
        this.maxY = cachedExtrema[3] + 1;
    }

    public final V get(int x, int y) {
        return get(IntCoord2D.of(x, y));
    }

    public final V put(int x, int y, V value) {
        return put(IntCoord2D.of(x, y), value);
    }

    public final boolean hasMapping(int x, int y, V value) {
        return hasMapping(IntCoord2D.of(x, y), value);
    }

    public final boolean hasMapping(IntCoord2D key, V value) {
        Objects.requireNonNull(key, "key");
        return (containsKey(key) && Objects.equals(get(key), value));
    }

    public final int[] extrema() {
        int[] out = null;
        for (IntCoord2D coord : keySet()) {
            final int x = coord.x();
            final int y = coord.y();
            if (out == null) {
                out = new int[]{x, y, x, y};
            } else {
                if (x < out[0]) {
                    out[0] = x;
                } else if (x > out[2]) {
                    out[2] = x;
                }
                if (y < out[1]) {
                    out[1] = y;
                } else if (y > out[3]) {
                    out[3] = y;
                }
            }
        }
        return out;
    }

    public final long containingGridArea() {
        final int[] extrema = extrema();
        if (extrema == null) {
            return 0;
        }
        final long width = 1L + ((long) extrema[2]) - ((long) extrema[0]);
        final long height = 1L + ((long) extrema[3]) - ((long) extrema[1]);
        return (width * height);
    }

    public final long containingRectangleArea() {
        final int[] extrema = extrema();
        if (extrema == null) {
            return 0;
        }
        final long width = ((long) extrema[2]) - ((long) extrema[0]);
        final long height = ((long) extrema[3]) - ((long) extrema[1]);
        return (width * height);
    }

    public final <U> IntGrid2D<U> remap(Function<V, U> valueTransformation) {
        final IntGrid2D<U> out = new IntGrid2D<>();
        for (Map.Entry<IntCoord2D, V> entry : entrySet()) {
            out.put(entry.getKey(), valueTransformation.apply(entry.getValue()));
        }
        return out;
    }

    public final <U> IntGrid2D<U> remap(BiFunction<IntCoord2D, V, U> keyValueTransformation) {
        final IntGrid2D<U> out = new IntGrid2D<>();
        for (Map.Entry<IntCoord2D, V> entry : entrySet()) {
            out.put(entry.getKey(), keyValueTransformation.apply(entry.getKey(), entry.getValue()));
        }
        return out;
    }

    public final IntGrid2D<V> translate(int x, int y) {
        return translate(IntCoord2D.of(x, y));
    }

    public final IntGrid2D<V> translate(IntCoord2D translation) {
        Objects.requireNonNull(translation, "translation");
        final IntGrid2D<V> out = new IntGrid2D<>();
        for (Map.Entry<IntCoord2D, V> entry : entrySet()) {
            out.put(entry.getKey().translate(translation), entry.getValue());
        }
        return out;
    }

    public final boolean overlaps(IntGrid2D<?> other) {
        if (other == null) {
            return false;
        }
        IntGrid2D<?> smaller = this;
        IntGrid2D<?> larger = other;
        if (this.size() > other.size()) {
            smaller = other;
            larger = this;
        }
        for (IntCoord2D coordinate : smaller.keySet()) {
            if (larger.containsKey(coordinate)) {
                return true;
            }
        }
        return false;
    }

    public final IntGrid2D<V> subGrid(int[] extrema) {
        if (Objects.requireNonNull(extrema, "extrema").length != 4) {
            throw new IllegalArgumentException(String.format("invalid extrema length: %d (expected 4)", extrema.length));
        }
        final IntCoord2D lowerLeft = IntCoord2D.of(extrema[0], extrema[1]);
        final IntCoord2D upperRight = IntCoord2D.of(extrema[2], extrema[3]);
        final NavigableMap<IntCoord2D, V> subMap = this.map.subMap(lowerLeft, true, upperRight, true);
        final IntGrid2D<V> out = new IntGrid2D<>();
        for (Map.Entry<IntCoord2D, V> entry : subMap.entrySet()) {
            final IntCoord2D key = entry.getKey();
            if (key.x() >= extrema[0] && key.x() <= extrema[2] && key.y() >= extrema[1] && key.y() <= extrema[3]) {
                out.put(key, entry.getValue());
            }
        }
        return out;
    }

    public final String toGridString() {
        return toGridString(true, null, " ");
    }

    public final String toGridString(boolean graphicsY) {
        return toGridString(graphicsY, null, " ");
    }

    public final String toGridString(boolean graphicsY, String elementFormat, String nullReplacement) {
        final Function<V, String> elementFormatter = (elementFormat == null ? String::valueOf : v -> String.format(elementFormat, v));
        final int[] extrema = extrema();
        if (extrema == null) {
            return "";
        }
        int startY = (graphicsY ? extrema[1] : extrema[3]);
        int limitY = (graphicsY ? extrema[3] + 1 : extrema[1] - 1);
        int dy = (graphicsY ? 1 : -1);
        final StringBuilder sb = new StringBuilder();
        final String NL = String.format("%n");
        for (int y = startY; y != limitY; y += dy) {
            if (y != startY) {
                sb.append(NL);
            }
            for (int x = extrema[0]; x <= extrema[2]; ++x) {
                final V v = get(x, y);
                if (v == null) {
                    sb.append(nullReplacement);
                } else {
                    sb.append(elementFormatter.apply(v));
                }
            }
        }
        return sb.toString();
    }

    @Override
    public final int size() {
        return this.map.size();
    }

    @Override
    public final boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public final boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public final boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public final V get(Object key) {
        return this.map.get(key);
    }

    @Override
    public final V put(IntCoord2D key, V value) {
        return this.map.put(key, value);
    }

    @Override
    public final V remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public final void putAll(Map<? extends IntCoord2D, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override
    public final void clear() {
        this.map.clear();
    }

    @Override
    public final Set<IntCoord2D> keySet() {
        return this.map.keySet();
    }

    @Override
    public final Collection<V> values() {
        return this.map.values();
    }

    Map<IntCoord2D, V> cache = new ConcurrentHashMap<>();

    public final V getExtendedValue(IntCoord2D coord) {
        V cached = cache.get(coord);
        if (cached != null) {
            return cached;
        } else {
            IntCoord2D key = IntCoord2D.of(Math.floorMod(coord.x(), maxX), Math.floorMod(coord.y(), maxY));
            cache.put(coord, (V)key);
            return map.get(key);
        }
    }

    @Override
    public final Set<Entry<IntCoord2D, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public final int hashCode() {
        return this.map.hashCode();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(Object o) {
        return this.map.equals(o);
    }

    @Override
    public final String toString() {
        return this.map.toString();
    }

    @Override
    public final V getOrDefault(Object key, V defaultValue) {
        return this.map.getOrDefault(key, defaultValue);
    }

    @Override
    public final void forEach(BiConsumer<? super IntCoord2D, ? super V> action) {
        this.map.forEach(action);
    }

    @Override
    public final void replaceAll(BiFunction<? super IntCoord2D, ? super V, ? extends V> function) {
        this.map.replaceAll(function);
    }

    @Override
    public final V putIfAbsent(IntCoord2D key, V value) {
        return this.map.putIfAbsent(key, value);
    }

    @Override
    public final boolean remove(Object key, Object value) {
        return this.map.remove(key, value);
    }

    @Override
    public final boolean replace(IntCoord2D key, V oldValue, V newValue) {
        return this.map.replace(key, oldValue, newValue);
    }

    @Override
    public final V replace(IntCoord2D key, V value) {
        return this.map.replace(key, value);
    }

    @Override
    public final V computeIfAbsent(IntCoord2D key, Function<? super IntCoord2D, ? extends V> mappingFunction) {
        return this.map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public final V computeIfPresent(IntCoord2D key, BiFunction<? super IntCoord2D, ? super V, ? extends V> remappingFunction) {
        return this.map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public final V compute(IntCoord2D key, BiFunction<? super IntCoord2D, ? super V, ? extends V> remappingFunction) {
        return this.map.compute(key, remappingFunction);
    }

    @Override
    public final V merge(IntCoord2D key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.map.merge(key, value, remappingFunction);
    }

    @Override
    @SuppressWarnings("unchecked")
    public IntGrid2D<V> clone() {
        try {
            final IntGrid2D<V> copy = (IntGrid2D<V>) super.clone();
            copy.map = (TreeMap<IntCoord2D, V>) this.map.clone();
            return copy;
        } catch (CloneNotSupportedException cns) {
            throw new InternalError(cns);
        }
    }

    public static IntGrid2D<Boolean> parse(boolean[][] array) {
        return parse(array, true);
    }

    public static IntGrid2D<Boolean> parse(boolean[][] array, boolean graphicsY) {
        final int rows = Objects.requireNonNull(array, "array").length;
        final IntUnaryOperator yOp = (graphicsY ? IntUnaryOperator.identity() : (r) -> rows - r - 1);
        final IntGrid2D<Boolean> grid = new IntGrid2D<>();
        for (int r = 0; r < rows; ++r) {
            final boolean[] row = array[r];
            if (row != null) {
                for (int c = 0; c < row.length; ++c) {
                    final IntCoord2D coord = IntCoord2D.of(c, yOp.applyAsInt(r));
                    grid.put(coord, array[r][c]);
                }
            }
        }
        return grid;
    }

    public static IntGrid2D<Integer> parse(int[][] array) {
        return parse(array, true);
    }

    public static IntGrid2D<Integer> parse(int[][] array, boolean graphicsY) {
        final int rows = Objects.requireNonNull(array, "array").length;
        final IntUnaryOperator yOp = (graphicsY ? IntUnaryOperator.identity() : (r) -> rows - r - 1);
        final IntGrid2D<Integer> grid = new IntGrid2D<>();
        for (int r = 0; r < rows; ++r) {
            final int[] row = array[r];
            if (row != null) {
                for (int c = 0; c < row.length; ++c) {
                    final IntCoord2D coord = IntCoord2D.of(c, yOp.applyAsInt(r));
                    grid.put(coord, array[r][c]);
                }
            }
        }
        return grid;
    }

    public static IntGrid2D<Character> parse(char[][] array) {
        return parse(" ", array, true);
    }

    public static IntGrid2D<Character> parse(String exclusions, char[][] array) {
        return parse(exclusions, array, true);
    }

    public static IntGrid2D<Character> parse(char[][] array, boolean graphicsY) {
        return parse(" ", array, true);
    }

    public static IntGrid2D<Character> parse(String exclusions, char[][] array, boolean graphicsY) {
        final int rows = Objects.requireNonNull(array, "array").length;
        final IntUnaryOperator yOp = (graphicsY ? IntUnaryOperator.identity() : (r) -> rows - r - 1);
        final IntGrid2D<Character> grid = new IntGrid2D<>();
        for (int r = 0; r < rows; ++r) {
            final char[] row = array[r];
            if (row != null) {
                for (int c = 0; c < row.length; ++c) {
                    final char value = array[r][c];
                    if (exclusions == null || exclusions.indexOf(value) < 0) {
                        final IntCoord2D coord = IntCoord2D.of(c, yOp.applyAsInt(r));
                        grid.put(coord, array[r][c]);
                    }
                }
            }
        }
        return grid;
    }

    public static IntGrid2D<Character> parse(String[] array) {
        return parse(" ", array, true);
    }

    public static IntGrid2D<Character> parse(String exclusions, String[] array) {
        return parse(exclusions, array, true);
    }

    public static IntGrid2D<Character> parse(String[] array, boolean graphicsY) {
        return parse(" ", array, graphicsY);
    }

    public static IntGrid2D<Character> parse(String exclusions, String[] array, boolean graphicsY) {
        final int rows = Objects.requireNonNull(array, "array").length;
        final char[][] cArray = new char[rows][];
        for (int r = 0; r < rows; ++r) {
            if (array[r] != null) {
                cArray[r] = array[r].toCharArray();
            }
        }
        return parse(exclusions, cArray, graphicsY);
    }

    public static <T> IntGrid2D<T> parse(T[][] array) {
        return parse(false, array, true);
    }

    public static <T> IntGrid2D<T> parse(boolean includeNullValues, T[][] array) {
        return parse(includeNullValues, array, true);
    }

    public static <T> IntGrid2D<T> parse(T[][] array, boolean graphicsY) {
        return parse(false, array, graphicsY);
    }

    public static <T> IntGrid2D<T> parse(boolean includeNullValues, T[][] array, boolean graphicsY) {
        final int rows = Objects.requireNonNull(array, "array").length;
        final IntUnaryOperator yOp = (graphicsY ? IntUnaryOperator.identity() : (r) -> rows - r - 1);
        final IntGrid2D<T> grid = new IntGrid2D<>();
        for (int r = 0; r < rows; ++r) {
            final T[] row = array[r];
            if (row != null) {
                for (int c = 0; c < row.length; ++c) {
                    final T value = array[r][c];
                    if (includeNullValues || value != null) {
                        final IntCoord2D coord = IntCoord2D.of(c, yOp.applyAsInt(r));
                        grid.put(coord, value);
                    }
                }
            }
        }
        return grid;
    }
}