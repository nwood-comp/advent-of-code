package common


import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Function

@SuppressWarnings("unused")
abstract class DayRunner implements Runnable {

    @SuppressWarnings("SpellCheckingInspection")
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxx");

    private final FileAccess fileAccess;

    DayRunner() {
        this(null, false);
    }

    DayRunner(String fileName, boolean testData) {
        this.fileAccess = fileAccess(fileName == null ? dataFileName(getClass().getSimpleName(), testData) : canonicalizeFileName(fileName));
    }

    @Override
    final void run() {
        doPartOne();
        doPartTwo();
    }

    abstract Object partOne(FileAccess fileAccess);

    abstract Object partTwo(FileAccess fileAccess);

    private void doPartOne() {
        doPart(1, this::partOne);
    }

    private void doPartTwo() {
        doPart(2, this::partTwo);
    }

    private void doPart(int part, Function<FileAccess, Object> method) {
        try {
            System.out.printf("%n[%s] %s: Part %d: Beginning...%n", ts(), getClass().getSimpleName(), part);
            final long startNanos = System.nanoTime();
            final Object answer = method.apply(this.fileAccess);
            final double durationSeconds = 1e-9d * (double) (System.nanoTime() - startNanos);
            if (answer != null) {
                System.out.printf("%n[%s] %s: Part %d: [%.3f sec] Answer: %s%n", ts(), getClass().getSimpleName(), part, durationSeconds, answer);
            } else {
                System.out.printf("%n[%s] %s: Part %d: No answer (or null result).%n", ts(), getClass().getSimpleName(), part);
            }
        } catch (Exception e) {
            System.err.printf("%n[%s] %s: Part %d: Caught: %s%n", ts(), getClass().getSimpleName(), part, e);
            e.printStackTrace(System.err);
        } finally {
            System.out.flush();
            delay(25L);
            System.err.flush();
            delay(50L);
        }
    }

    protected static String ts() {
        return TS_FORMAT.format(ZonedDateTime.now().toOffsetDateTime());
    }

    protected static void delay(long ms) {
        if (ms > 0L) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static FileAccess fileAccess(String fileName) {
        try {
            return new FileAccess(fileName);
        } catch (IOException ioe) {
            throw new IllegalStateException("Unable to access file: " + fileName, ioe);
        }
    }

    private static String dataFileName(String prefix, boolean testData) {
        String testDataString = testData ? '-test' : ''
        final String name = "../resources/" +  prefix + testDataString + "-input.txt";
//        final String name = "./src/resources/" +  prefix + testDataString + "-input.txt";
        return canonicalizeFileName(name);
    }

    private static String canonicalizeFileName(String fileName) {
        final File file = new File(fileName);
        try {
            return file.getCanonicalFile().getAbsoluteFile().toString();
        } catch (IOException ioe) {
            return file.getAbsoluteFile().toString();
        }
    }
}