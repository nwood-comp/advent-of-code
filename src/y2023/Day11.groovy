package y2023

import common.DayRunner
import common.FileAccess
import common.IntCoord2D
import common.IntGrid2D

class Day11 extends DayRunner {
    Day11() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day11().run();
    }

    IntGrid2D<String> parseInput(FileAccess fileAccess) {
        List<List<String>> stringGalaxy = fileAccess.lines.collect { it.toList() }
        List<List<String>> newStringGalaxy = []
        stringGalaxy.eachWithIndex { List<String> line, int i ->
            newStringGalaxy << line
            if (!line.contains('#')) {
                newStringGalaxy << line
            }
        }

        List<List<String>> flipped = newStringGalaxy.transpose()
        List<List<String>> newFlipped = []

        LinkedHashSet set = [].first()
        set.first

        flipped.eachWithIndex { List<String> column, int i ->
            newFlipped << column
            if (!column.contains('#')) {
                newFlipped << column
            }
        }

        stringGalaxy = newFlipped.transpose()
        IntGrid2D<String> galaxy = IntGrid2D.parse(stringGalaxy.toArray())
        int counter = 1
        galaxy.eachWithIndex { Map.Entry<IntCoord2D, java.lang.String> entry, int i ->
            if (entry.value == '#') {
                galaxy[entry.key] = counter.toString()
                counter++
            }
        }
        return galaxy
    }

    Tuple3<IntGrid2D<String>, List<Integer>, List<Integer>> parseInput2(FileAccess fileAccess) {
        List<List<String>> stringGalaxy = fileAccess.lines.collect { it.toList() }

        List<Integer> expandedRows = []
        List<Integer> expandedColumns = []
        stringGalaxy.eachWithIndex { List<String> line, int i ->
            if (!line.contains('#')) {
                expandedRows << i
            }
        }

        stringGalaxy.transpose().eachWithIndex { List<String> column, int i ->
            if (!column.contains('#')) {
                expandedColumns << i
            }
        }

        IntGrid2D<String> galaxy = IntGrid2D.parse(stringGalaxy.toArray())
        int counter = 1
        galaxy.eachWithIndex { Map.Entry<IntCoord2D, java.lang.String> entry, int i ->
            if (entry.value == '#') {
                galaxy[entry.key] = counter.toString()
                counter++
            }
        }

        return new Tuple3<>(galaxy, expandedRows, expandedColumns)
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        def expansion = 1000000
        Tuple3<IntGrid2D<String>, List<Integer>, List<Integer>> parsedInput = parseInput2(fileAccess)
        Set<IntCoord2D> galaxies = parsedInput.v1.findAll {
            it.value ==~ /\d+/
        }.keySet()

        List<Integer> expandedRows = parsedInput.v2
        List<Integer> expandedCol = parsedInput.v3

        galaxies.sum { startGalaxy ->
            galaxies.sum { endGalaxy ->
                Long distance = startGalaxy.manhattanDistanceTo(endGalaxy)
                distance += (startGalaxy.x()..endGalaxy.x()).intersect(expandedCol).size() * expansion
                distance += (startGalaxy.y()..endGalaxy.y()).intersect(expandedRows).size() * expansion
                return distance
            }
        } / 2
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        def parsedInput = parseInput(fileAccess)
    }
}