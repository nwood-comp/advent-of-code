package y2023

import common.DayRunner
import common.FileAccess

class Day09 extends DayRunner {
    Day09() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day09().run();
    }

    List<List<Integer>> parseInput(FileAccess fileAccess) {
        List<List<Integer>> histories = fileAccess.lines.collect {
            it.split(/ /)*.toInteger()
        }
        return histories
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        List<List<Integer>> histories = parseInput(fileAccess)
        histories.sum { history ->
            List<List<Integer>> subHistories = [history]
            List<Integer> lastHistory = history
            do {
                List<Integer> newHistory = []
                lastHistory.eachWithIndex { int entry, int i ->
                    if ((i + 1) < lastHistory.size()) {
                        newHistory << lastHistory[i + 1] - lastHistory[i]
                    }
                }

                lastHistory = newHistory
                subHistories << lastHistory
            } while (lastHistory.unique(false) != [0])

            return subHistories*.last().inject(0) {acc, value ->
                return value + acc
            }
        }
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        List<List<Integer>> histories = parseInput(fileAccess)
        histories.sum { history ->
            List<List<Integer>> subHistories = [history]
            List<Integer> lastHistory = history
            do {
                List<Integer> newHistory = []
                lastHistory.eachWithIndex { int entry, int i ->
                    if ((i + 1) < lastHistory.size()) {
                        newHistory << lastHistory[i + 1] - lastHistory[i]
                    }
                }
                lastHistory = newHistory
                subHistories << lastHistory
            } while (lastHistory.unique(false) != [0])

            return subHistories.reverse()*.first().inject(0) {acc, value ->
                return value - acc
            }
        }
    }

}