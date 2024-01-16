package y2023

import common.DayRunner
import common.FileAccess

class Day06 extends DayRunner {
    Day06() {
        super(null, true);
    }

    static void main(String[] args) {
        new Day06().run();
    }

    def parseInput(FileAccess fileAccess) {
        return null
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        List times = [51, 92, 68, 90]
        List distances = [222, 2031, 1126, 1225]
        List wins = []
        times.eachWithIndex { time, index ->
            List options = (1..time)
            List winningOptions = options.findAll { secondsHeld ->
                Integer secondsActive = time - secondsHeld
                long totalDistance = secondsActive * secondsHeld
                return totalDistance > distances[index]
            }

            wins << winningOptions.size()
        }
        long total = 1
        wins.each {
            total *= it
        }
        return total
    }

    @Override
    Object partTwo(FileAccess fileAccess) {

        List<Long> times = [51926890]
        List<Long> distances = [222203111261225]
        Long wins = 0
        times.eachWithIndex { time, index ->
            List options = (1..time)
            options.each { secondsHeld ->
                Long secondsActive = time - secondsHeld
                long totalDistance = secondsActive * secondsHeld
                if (totalDistance > distances[index]) {
                    wins++
                }
            }
        }
        return wins
    }
}