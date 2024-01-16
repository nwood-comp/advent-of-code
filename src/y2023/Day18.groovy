package y2023

import common.DayRunner
import common.FileAccess
import common.IntCoord2D
import common.IntGrid2D

class Day18 extends DayRunner {
    Day18() {
        super(null, true);
    }

    static void main(String[] args) {
        new Day18().run();
    }

    List<Tuple2<String, Integer>> parseInput(FileAccess fileAccess) {
        return fileAccess.lines.collect {
            List<String> split = it.split(/ /)
            String direction = split[0]
            String distance = split[1]
            return new Tuple2<>(direction, distance)
        }
    }

    List<Tuple2<String, Integer>> parseInput2(FileAccess fileAccess) {
        return fileAccess.lines.collect {
            List<String> split = it.split(/\(/)
            Integer distance = Integer.parseInt(split[1][1..5], 16)
            String direction = null
            switch (split[1][6]) {
                case '0': direction = 'R'; break;
                case '1': direction = 'D'; break;
                case '2': direction = 'L'; break;
                case '3': direction = 'U'; break;
                default: break;
            }

            return new Tuple2<>(direction, distance)
        }
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        List<Tuple2<String, Integer>> parsedInput = parseInput(fileAccess)
        List<List<String>> tempGrid = []
//        int size = 50
        int size = 850
        int halfSize = size / 2
        (0..size).each {
            {
                List<String> row = []
                (0..size).each {
                    row << '.'
                }
                tempGrid << row
            }
        }
        IntGrid2D<String> grid = IntGrid2D<String>.parse(tempGrid.collect { it as String[] } as String[][])
        IntCoord2D current = IntCoord2D.of(halfSize, halfSize)
        grid[current] = '#'
        List<IntCoord2D> insideEdges = []
        parsedInput.each { it, String direction = it.v1, String distance = it.v2->
            IntCoord2D coordDirection
            List<IntCoord2D> circleDirection = [IntCoord2D.D, IntCoord2D.R, IntCoord2D.U, IntCoord2D.L]
            switch (direction) {
                case 'U': coordDirection = IntCoord2D.D; break;
                case 'D': coordDirection = IntCoord2D.U; break;
                case 'L': coordDirection = IntCoord2D.L; break;
                case 'R': coordDirection = IntCoord2D.R; break;
                default: break;
            }
            Integer numDistance = Integer.parseInt(distance)
            (1..numDistance).each {
                current = current.translate(coordDirection)
                if (!grid.containsKey(current)) {
                    throw new RuntimeException("ope, too small: $current")
                }
                grid[current] = '#'

                if (it != numDistance) {
                    IntCoord2D insideDirection = null
                    switch (coordDirection) {
                        case IntCoord2D.D: insideDirection = IntCoord2D.R; break;
                        case IntCoord2D.U: insideDirection = IntCoord2D.L; break;
                        case IntCoord2D.R: insideDirection = IntCoord2D.U; break;
                        case IntCoord2D.L: insideDirection = IntCoord2D.D; break;
                    }
                    insideEdges << current.translate(insideDirection)
                }
            }
        }
        while (insideEdges.size()) {
            IntCoord2D insideCoord = insideEdges.remove(0)
            List<IntCoord2D> neighbors = insideCoord.getNeighbors(false)
            boolean modified
            neighbors.each {
                if (grid[it] == '.') {
                    insideEdges << it
                    grid[it] = '#'
                }
            }
        }
        println(grid.toGridString(true))
        return grid.values().count { it == '#' }
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        List<Tuple2<String, Integer>> parsedInput = parseInput2(fileAccess)

        long edgeLengths = 0
        List<IntCoord2D> loop = []
        IntCoord2D current = IntCoord2D.of(0, 0)

        parsedInput.each { it, String direction = it.v1, String distance = it.v2 ->
            Integer numDistance = Integer.parseInt(distance)
            IntCoord2D coordDirection = null

            switch (direction) {
                case 'U': coordDirection = IntCoord2D.D; break;
                case 'D': coordDirection = IntCoord2D.U; break;
                case 'L': coordDirection = IntCoord2D.L; break;
                case 'R': coordDirection = IntCoord2D.R; break;
                default: break;
            }
            IntCoord2D coordDirectionMult = IntCoord2D.of(coordDirection.x()*numDistance, coordDirection.y()*numDistance)
            current = current.translate(coordDirectionMult)
            edgeLengths += numDistance
            loop.add(current)

        }

        long size= 0

        loop.eachWithIndex { IntCoord2D coord, int i ->
            IntCoord2D nextCoord = loop[(i+1) % (loop.size())]
            size += ((long)coord.x()*(long)nextCoord.y()) - ((long)coord.y()*(long)nextCoord.x())
        }

//        loop.eachWithIndex { IntCoord2D coord, int i ->
//            if (i == 0) return
//            if (loop[i + 1].x() == coord.x()) return
//
//            List<IntCoord2D> pointsToLeftList = loop.findAll { it.x() < coord.x() }
//            TreeMap<Integer, IntRange> pointsToLeft = new TreeMap<Integer, IntRange>(Collections.reverseOrder())
//            pointsToLeft.putAll(
//                    pointsToLeftList.groupBy { it.x() }
//                            .values()
//                            .collectEntries {
//                                if (it.size() > 2) {
//                                    throw new RuntimeException('ope, multiple identical x pairs')
//                                }
//                                return [(it[0].x()): new IntRange(it[0].y(), it[1].y())]
//                            }
//            )
//
//            int linesToLeft = pointsToLeft.findAll { it.value.containsWithinBounds(coord.y()) }.size()
//            int coordsToLeft = pointsToLeftList.count { it.y() == coord.y() && it.x() < coord.x() }
//            int modLinesCount = linesToLeft - coordsToLeft
//
//            if (modLinesCount % 2 == 0) return
//
//            IntRange yRange = new IntRange(coord.y(),)
//        }

        return size/2 + edgeLengths/2 + 1
        //1072888051
        //952408144115
        //7598546778
    }
}