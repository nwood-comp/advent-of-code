package y2023

import common.DayRunner
import common.FileAccess
import common.IntCoord2D
import common.IntGrid2D

import java.util.concurrent.ConcurrentHashMap

class Day21 extends DayRunner {
    Day21() {
        super(null, true);
    }

    static void main(String[] args) {
        new Day21().run();
    }

    IntGrid2D<Character> parseInput(FileAccess fileAccess) {
        char[][] garden = fileAccess.lines.collect {
            it.collect { it }
        } as char[][]
        def grid = IntGrid2D<Character>.parse(garden)
        return grid
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        IntGrid2D<Character> parsedInput = parseInput(fileAccess)
        IntCoord2D start = parsedInput.find { it.value == 'S' as Character }.key
        parsedInput[start] = '.' as Character
        Set<IntCoord2D> plots = []
        plots << start

        int steps = 6
        (1..steps).each {
            List<IntCoord2D> newPlots = []
            plots.each {
                List<IntCoord2D> neighbors = it.getNeighbors(false)
                newPlots.addAll(neighbors.findAll { parsedInput[it] == '.' as Character })
            }
            plots = newPlots
        }

        return plots.size()
    }

    //0 = never, 1 = odd, 2 = even, 3 = both
    Map<IntCoord2D, Integer> checked = [:]

    @Override
    Object partTwo(FileAccess fileAccess) {
        IntGrid2D<Character> parsedInput = parseInput(fileAccess)
        IntCoord2D start = parsedInput.find { it.value == 'S' as Character }.key
        parsedInput[start] = '.' as Character
        parsedInput.cacheExtrema();
        Set<IntCoord2D> finalPlots = ConcurrentHashMap.newKeySet()
        Map<IntCoord2D, IntCoord2D> plotsAndPrevious = new ConcurrentHashMap<>()
        plotsAndPrevious.put(start, start)

        int steps = 5000
        (1..steps).each { count ->
            Map<IntCoord2D, IntCoord2D> newPlots = new ConcurrentHashMap<>()
            plotsAndPrevious.entrySet().parallelStream().forEach(entry -> {
                IntCoord2D plot = entry.key
                Integer cached = checked[plot] ?: 0
                if (cached) {
                    if (cached == 3) {
                        return
                    }
                    boolean even = count % 2 == 0
                    if (cached == 2 && even) {
                        return
                    } else if (cached == 1 && !even) {
                        return
                    }
                } else {
                    boolean even = count % 2 == 0
                    if (!even) {
                        finalPlots.add(plot)
                    }
                    checked[plot] = even ? cached + 2 : cached + 1
                    plot.getNeighbors(false).parallelStream()
                            .filter { !finalPlots.contains(it) }
                            .filter { it != entry.value }
                            .filter { parsedInput.getExtendedValue(it) == '.' as Character }
                            .forEach {
                                newPlots.put(it, entry.value)
                            }
                }
            })
            plotsAndPrevious = newPlots
        }

        finalPlots.addAll(plotsAndPrevious.keySet())

        return finalPlots.size()
    }
}