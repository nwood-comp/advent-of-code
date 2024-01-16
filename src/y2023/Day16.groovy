package y2023

import common.DayRunner
import common.FileAccess
import common.IntCoord2D
import common.IntGrid2D
import groovy.transform.CompileStatic

@CompileStatic
class Day16 extends DayRunner {
    Day16() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day16().run();
    }

    IntGrid2D<Tile> parseInput(FileAccess fileAccess) {
        return IntGrid2D<Tile>.parse(fileAccess.lines.collect {
            it.collect {
                new Tile(it as Character)
            }
        } as Tile[][])
    }

    Set<Beam> propogate(Beam beam, IntGrid2D<Tile> tileIntGrid2D) {
        Tile tile = tileIntGrid2D.get(beam.location)
        if (!tile) {
            return [] as Set
        }
        List<Beam> newBeams = tile.createBeams(beam)
        tile.setBeam(beam)
        return newBeams.findAll {
            tileIntGrid2D.get(it.location) != null
        } as Set
    }

    class Beam {
        IntCoord2D direction
        IntCoord2D location

        Beam(IntCoord2D direction, IntCoord2D location) {
            this.direction = direction
            this.location = location
        }

        Beam nextBeam() {
            return new Beam(direction, location.translate(direction))
        }

        Beam nextBeam(IntCoord2D continueDirection) {
            return new Beam(continueDirection, location.translate(continueDirection))
        }
    }

    class Tile {
        String type
        Set<IntCoord2D> beamDirections = []

        Tile(Character type) {
            this.type = type.toString()
        }

        void setBeam(Beam beam) {
            beamDirections << beam.direction
        }

        List<Beam> createBeams(Beam beam) {
            if (beamDirections.contains(beam.direction)) {
                return []
            }
            if (type == '.') {
                return [beam.nextBeam()]
            } else if (type == '-') {
                if (beam.direction in [IntCoord2D.L, IntCoord2D.R]) {
                    return [beam.nextBeam()]
                } else {
                    return [beam.nextBeam(IntCoord2D.L), beam.nextBeam(IntCoord2D.R)]
                }
            } else if (type == '|') {
                if (beam.direction in [IntCoord2D.U, IntCoord2D.D]) {
                    return [beam.nextBeam()]
                } else {
                    return [beam.nextBeam(IntCoord2D.U), beam.nextBeam(IntCoord2D.D)]
                }
            } else if (type == '/') {
                switch (beam.direction) {
                    case (IntCoord2D.D): return [beam.nextBeam(IntCoord2D.R)]
                    case (IntCoord2D.R): return [beam.nextBeam(IntCoord2D.D)]
                    case (IntCoord2D.U): return [beam.nextBeam(IntCoord2D.L)]
                    case (IntCoord2D.L): return [beam.nextBeam(IntCoord2D.U)]
                }
            } else if (type == '\\') {
                switch (beam.direction) {
                    case (IntCoord2D.D): return [beam.nextBeam(IntCoord2D.L)]
                    case (IntCoord2D.R): return [beam.nextBeam(IntCoord2D.U)]
                    case (IntCoord2D.U): return [beam.nextBeam(IntCoord2D.R)]
                    case (IntCoord2D.L): return [beam.nextBeam(IntCoord2D.D)]
                }
            } else {
                throw new RuntimeException('ope')
            }

            return null
        }
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        IntGrid2D<Tile> parsedInput = parseInput(fileAccess)
        Beam originBeam = new Beam(IntCoord2D.R, IntCoord2D.of(0, 0))

        Set<Beam> beams = [originBeam] as Set
        while (beams.size()) {
            Set<Beam> newBeams = [] as Set
            beams.each {
                newBeams.addAll(propogate(it, parsedInput))
            }
            beams = newBeams
        }

        return parsedInput.values().count { it.beamDirections.size() }
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        IntGrid2D<Tile> parsedInput = parseInput(fileAccess)
        int[] extrema = parsedInput.extrema()
        Set<Beam> allBeams = []
        (extrema[0]..extrema[2]).each {
            allBeams << new Beam(IntCoord2D.U, IntCoord2D.of(it, 0))
            allBeams << new Beam(IntCoord2D.D, IntCoord2D.of(it, extrema[3]))
        }
        (extrema[1]..extrema[3]).each {
            allBeams << new Beam(IntCoord2D.R, IntCoord2D.of(0, it))
            allBeams << new Beam(IntCoord2D.D, IntCoord2D.of(extrema[2], it))
        }

        Beam originBeam = allBeams.max {beam ->
            parsedInput.values()*.beamDirections*.clear()
            Set<Beam> beams = [beam] as Set
            while (beams.size()) {
                Set<Beam> newBeams = [] as Set
                beams.each {
                    newBeams.addAll(propogate(it, parsedInput))
                }
                beams = newBeams
            }

            def count = parsedInput.values().count { it.beamDirections.size() }
            return count

        }

        parsedInput.values()*.beamDirections*.clear()
        Set<Beam> beams = [originBeam] as Set
        while (beams.size()) {
            Set<Beam> newBeams = [] as Set
            beams.each {
                newBeams.addAll(propogate(it, parsedInput))
            }
            beams = newBeams
        }

        return parsedInput.values().count { it.beamDirections.size() }

        //10913
    }
}