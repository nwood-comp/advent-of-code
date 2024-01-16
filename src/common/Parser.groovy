package common

class Parser {

    static IntGrid2D<Integer> parseInput(FileAccess fileAccess) {
        return IntGrid2D<Integer>.parse(fileAccess.collect { it.collect { Integer.parseInt(it) } } as Integer[][])
    }
}
