package common

import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.Stream

@SuppressWarnings("unused")
class FileAccess implements Iterable<String> {

    private static final String NL = String.format("%n")

    private final Path path
    private final List<String> lines

    FileAccess(Path path) throws IOException {
        this.path = path
        this.lines = read(path)
    }

    FileAccess(File file) throws IOException {
        this(file.toPath())
    }

    FileAccess(String fileName) throws IOException {
        this(new File(fileName))
    }

    @Override
    final Iterator<String> iterator() {
        return this.lines.iterator()
    }

    final Stream<String> stream() {
        return this.lines.stream()
    }

    final <T> Stream<T> stream(Function<String, T> lineTranslator) {
        return stream().map(lineTranslator)
    }

    final int[] asIntArray() {
        return stream().mapToInt(Integer::parseInt).toArray()
    }

    final long[] asLongArray() {
        return stream().mapToLong(Long::parseLong).toArray()
    }

    final List<Integer> asIntegerList() {
        return stream(Integer::valueOf).collect(Collectors.toList())
    }

    final List<Long> asLongList() {
        return stream(Long::valueOf).collect(Collectors.toList())
    }

    final Path getPath() {
        return this.path
    }

    final File getFile() {
        return this.path.toFile()
    }

    final String getFileName() {
        return getFile().toString()
    }

    final String getText() {
        return String.join(NL, this.lines)
    }

    final List<String> getLines() {
        return this.lines
    }

    private static List<String> read(Path path) throws IOException {
        return List.copyOf(Files.readAllLines(path))
    }
}