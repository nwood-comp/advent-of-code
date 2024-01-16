package y2023

import common.DayRunner
import common.FileAccess
import common.IntCoord2D
import common.IntGrid2D

class Day14 extends DayRunner {
    Day14() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day14().run();
    }

    def parseInput(FileAccess fileAccess) {
        return IntGrid2D<Character>.parse(fileAccess.lines*.toCharArray() as char[][])
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        IntGrid2D<Character> parsedInput = parseInput(fileAccess)
        int columns = parsedInput.max { it.key.x() }.key.x()
        int rows = parsedInput.max { it.key.y() }.key.y()
        (0..rows).each {
            (1..rows).each { rowIndex ->
                (0..columns).each { columnIndex ->
                    if (parsedInput.get(columnIndex, rowIndex) == 'O' as Character && parsedInput.get(columnIndex, rowIndex - 1) == '.' as Character) {
                        parsedInput.put(columnIndex, rowIndex - 1, 'O' as Character)
                        parsedInput.put(columnIndex, rowIndex, '.' as Character)
                    }
                }
            }
        }

        int sum = (0..rows).sum { rowIndex ->
            (0..columns).sum { columnIndex ->
                int weight = rows + 1 - rowIndex
                if (parsedInput.get(columnIndex, rowIndex) == 'O') {
                    return weight
                } else {
                    return 0
                }
            }
        }

        println(parsedInput.toGridString(true))
        return sum
    }

    HashMap<String, List<Long>> positions = new HashMap<>()

    @Override
    Object partTwo(FileAccess fileAccess) {
        IntGrid2D<Character> parsedInput = parseInput(fileAccess)
        long maxCycles = 1000000000L
        long cycles = 0
        int columns = parsedInput.max { it.key.x() }.key.x()
        int rows = parsedInput.max { it.key.y() }.key.y()
        while (cycles < maxCycles) {
            List<Long> pattern = positions.computeIfAbsent(parsedInput.toString(), (a) -> new ArrayList<Integer>())
            pattern << cycles
            if(pattern.size() > 2) {
                long patternCycle = pattern[2] - pattern[1]
                while (cycles + patternCycle < maxCycles) {
                    cycles += patternCycle
                }
                println(patternCycle)
                positions.clear()
                continue
            }
            Map map = [:]
            map.values()
            (0..3).each { counter ->
                IntCoord2D translation
                switch (counter) {
                    case 0: translation = IntCoord2D.S; break
                    case 1: translation = IntCoord2D.W; break
                    case 2: translation = IntCoord2D.N; break
                    case 3: translation = IntCoord2D.E; break
                }
                (0..rows).each {
                    (0..rows).each { rowIndex ->
                        if (rowIndex == 0 && translation == IntCoord2D.S) return;
                        if (rowIndex == rows && translation == IntCoord2D.N) return;
                        (0..columns).each { columnIndex ->
                            if (columnIndex == 0 && translation == IntCoord2D.W) return;
                            if (columnIndex == columns && translation == IntCoord2D.E) return;
                            IntCoord2D position = IntCoord2D.of(columnIndex, rowIndex)
                            IntCoord2D translatedPosition = translation.translate(position)
                            if (parsedInput.get(position) == 'O' as Character && parsedInput.get(translatedPosition) == '.' as Character) {
                                parsedInput.put(translatedPosition, 'O' as Character)
                                parsedInput.put(position, '.' as Character)
                            }
                        }
                    }
                }
            }
            cycles++
        }

        return (0..rows).sum { rowIndex ->
            (0..columns).sum { columnIndex ->
                int weight = rows + 1 - rowIndex
                if (parsedInput.get(columnIndex, rowIndex) == 'O') {
                    return weight
                } else {
                    return 0
                }
            }
        }

        //89167
    }
}