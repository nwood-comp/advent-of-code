package y2023

import common.DayRunner
import common.FileAccess
import common.IntCoord2D
import common.IntGrid2D

class Day10 extends DayRunner {
    Day10() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day10().run();
    }

    class PipeGrid<V> extends IntGrid2D<V> {
        PipeGrid() {
            super()
        }

        IntCoord2D findNextLocation(IntCoord2D location, IntCoord2D lastLocation) {
            String type = (this[location] as Pipe).type

            switch (type) {
//                case 'S': return returnNew(location, lastLocation, IntCoord2D.L, IntCoord2D.D)
                case 'S': return returnNew(location, lastLocation, IntCoord2D.L, IntCoord2D.D)
                case '|': return returnNew(location, lastLocation, IntCoord2D.U, IntCoord2D.D)
                case '-': return returnNew(location, lastLocation, IntCoord2D.L, IntCoord2D.R)
                case 'J': return returnNew(location, lastLocation, IntCoord2D.L, IntCoord2D.U)
                case '7': return returnNew(location, lastLocation, IntCoord2D.L, IntCoord2D.D)
                case 'F': return returnNew(location, lastLocation, IntCoord2D.D, IntCoord2D.R)
                case 'L': return returnNew(location, lastLocation, IntCoord2D.R, IntCoord2D.U)
            }
        }

        IntCoord2D returnNew(IntCoord2D location, IntCoord2D lastLocation, IntCoord2D translation1, IntCoord2D translation2) {
            if (location.translate(translation1) == lastLocation) {
                return location.translate(translation2)
            } else {
                return location.translate(translation1)
            }
        }
    }

    class Pipe {
        String type

        Pipe(String type) {
            this.type = type
        }
    }

    PipeGrid<Pipe> parseInput(FileAccess fileAccess) {
        PipeGrid<Pipe> grid = new PipeGrid<Pipe>()
        fileAccess.eachWithIndex { String line, int rowIndex ->
            line.eachWithIndex { String pipeString, int columnIndex ->
                grid.put(columnIndex, fileAccess.size() - rowIndex - 1, new Pipe(pipeString))
            }
        }

        return grid
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        PipeGrid<Pipe> pipes = parseInput(fileAccess)
        IntCoord2D start = pipes.find { IntCoord2D location, Pipe pipe ->
            pipe.type == 'S'
        }.key
        IntCoord2D current = IntCoord2D.of(start.x, start.y)
        IntCoord2D last = IntCoord2D.of(current.x, current.y)
        int counter = 0
        do {
            IntCoord2D found = pipes.findNextLocation(current, last)
            last = IntCoord2D.of(current.x, current.y)
            current = IntCoord2D.of(found.x, found.y)
            counter++
        } while (current.x != start.x || current.y != start.y)

        return counter / 2
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        PipeGrid<Pipe> pipes = parseInput(fileAccess)
        IntCoord2D start = pipes.find { IntCoord2D location, Pipe pipe ->
            pipe.type == 'S'
        }.key
        IntCoord2D current = IntCoord2D.of(start.x, start.y)
        IntCoord2D last = IntCoord2D.of(current.x, current.y)
        List<IntCoord2D> loop = [] as List<IntCoord2D>
        do {
            IntCoord2D found = pipes.findNextLocation(current, last)
            loop << found
            last = IntCoord2D.of(current.x, current.y)
            current = IntCoord2D.of(found.x, found.y)
        } while (current.x != start.x || current.y != start.y)

        HashSet<IntCoord2D> inside = [] as HashSet<IntCoord2D>
        current = IntCoord2D.of(start.x, start.y)
        last = IntCoord2D.of(current.x, current.y)

        loop.eachWithIndex { coord, index ->
            IntCoord2D translation = loop[(index + 1) % loop.size()].subtract(coord)
            IntCoord2D rotated = translation.rotate90()
            if (coord.translate(rotated) !in loop) {
                inside << coord.translate(rotated)
            }
        }

        loop.eachWithIndex { coord, index ->
            IntCoord2D translation = loop[(index - 1) % loop.size()].subtract(coord)
            IntCoord2D rotated = translation.rotate270()
            if (coord.translate(rotated) !in loop) {
                inside << coord.translate(rotated)
            }
        }

        boolean modified = true
        while (modified) {
            modified = false
            int originalSize = inside.size()

            LinkedHashSet<IntCoord2D> insideNew = [] as LinkedHashSet<IntCoord2D>
            inside.each {
                insideNew.addAll(it.getNeighbors(true).findAll {
                    it !in loop
                })
            }

            inside.addAll(insideNew)
            modified = originalSize != inside.size()
        }
        return inside.size()
    }

    boolean pathFromLocationToOutsideUsingLocations(IntCoord2D current, Set<IntCoord2D> unvisited, FileAccess fileAccess, PipeGrid<Pipe> pipes) {
        List neighbors = current.getNeighbors(false)
        Set<IntCoord2D> unvisitedCopy = []
        unvisitedCopy.addAll(unvisited)

        if (neighbors.any {
            isPastEdge(fileAccess, it)
        }) {
            return neighbors.find {
                isPastEdge(fileAccess, it)
            }
        } else {
            unvisited.remove(current)
        }

        List candidateNeighbors = neighbors.findAll {
            it in unvisited
        }

        candidateNeighbors.find {
            pathFromLocationToOutsideUsingLocations(it, unvisited, fileAccess, null)
        }
    }

    boolean isPastEdge(FileAccess file, IntCoord2D location) {
        return location.x() >= file.lines[0].size() || location.x() < 0 || location.y() < 0 || location.y() >= file.lines.size()
    }
}