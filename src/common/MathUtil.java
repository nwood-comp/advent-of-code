package common;

public class MathUtil {

    public static int binaryGcd(int a, int b) {
        // Adapted from https://en.wikipedia.org/wiki/Binary_GCD_algorithm
        // Generally faster than traditional Euclidean algorithm.
        int aa = Math.abs(a);
        int bb = Math.abs(b);
        if (aa == 0) {
            return bb;
        } else if (bb == 0) {
            return aa;
        }
        final int i = Integer.numberOfTrailingZeros(aa);
        aa >>= i;
        final int j = Integer.numberOfTrailingZeros(bb);
        bb >>= j;
        int k = Math.min(i, j);
        while (true) {
            if (aa > bb) {
                int temp = aa;
                aa = bb;
                bb = temp;
            }
            bb -= aa;
            if (bb == 0) {
                return (aa << k);
            }
            bb >>= Integer.numberOfTrailingZeros(bb);
        }
    }
}