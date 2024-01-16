package y2023

import common.DayRunner
import common.FileAccess

class Day13 extends DayRunner {
    Day13() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day13().run();
    }

    List<List<List<Character>>> parseInput(FileAccess fileAccess) {
        return fileAccess.text.split(/\n\n/).collect {
            it.split(/\n/).collect {
                it.toCharArray().toList()
            }
        }
    }

    Integer findReflection(List<List<Character>> grid) {
        Integer firstIndex = null
        int i = 0
        while (!firstIndex && i < grid.size()) {
            if (i == 0 && grid[i] == grid[i + 1]) {
                firstIndex = 0
                break
            } else if (i == grid.size() - 1) {
                break
            }
            if (i < (grid.size() - 1) / 2) {
                List<List<Character>> backwardsFromIndex = grid[i..0]
                if (backwardsFromIndex == grid[i + 1..(i + backwardsFromIndex.size())]) {
                    firstIndex = i
                }
            } else {
                List<List<Character>> forwardsFromIndex = grid[(i + 1)..-1]
                if (forwardsFromIndex == grid[i..i - forwardsFromIndex.size() + 1]) {
                    firstIndex = i
                }
            }
            i++
        }
        return firstIndex
    }

    Integer findReflectionWithSmudge(List<List<Character>> grid, Integer reflection) {
        Integer firstIndex = null
        int i = 0
        while (!firstIndex && i < grid.size()) {
            if (i != reflection) {
                if (i == 0 && isOneDifference(grid[i..i], grid[(i + 1)..(i + 1)])) {
                    firstIndex = 0
                    break
                } else if (i == grid.size() - 1) {
                    break
                }
                if (i < (grid.size() - 1) / 2) {
                    List<List<Character>> backwardsFromIndex = grid[i..0]
                    if (isOneDifference(backwardsFromIndex, grid[i + 1..(i + backwardsFromIndex.size())])) {
                        firstIndex = i
                    }
                } else {
                    List<List<Character>> forwardsFromIndex = grid[(i + 1)..-1]
                    if (isOneDifference(forwardsFromIndex, grid[i..i - forwardsFromIndex.size() + 1])) {
                        firstIndex = i
                    }
                }
            }
            i++
        }
        return firstIndex
    }

    boolean isOneDifference(List<List<Character>> list1, List<List<Character>> list2) {
        int differences = 0
        list1.eachWithIndex { row, index ->
            row.eachWithIndex { char entry, int column ->
                if (entry != list2[index][column]) {
                    differences++
                }
            }
        }

        return differences == 1
    }

    Long getCount(Integer reflection) {
        return reflection + 1
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        List<List<List<Character>>> parsedInput = parseInput(fileAccess)
        return parsedInput.sum {
            Integer rowReflection = findReflection(it)
            if (rowReflection != null) {
                Long value = getCount(rowReflection) * 100
                return value
            } else {
                Integer columnReflection = findReflection(it.transpose())
                Long value = getCount(columnReflection)
                return value
            }
        }
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        List<List<List<Character>>> parsedInput = parseInput(fileAccess)
        return parsedInput.sum {
            Integer rowReflection = findReflection(it)
            Integer columnReflection = findReflection(it.transpose())

            Integer rowReflectionSmudge = findReflectionWithSmudge(it, rowReflection)
            Integer columnReflectionWithSmudge = findReflectionWithSmudge(it.transpose(), columnReflection)

            if (rowReflectionSmudge != null) {
                Long value = getCount(rowReflectionSmudge) * 100
                return value
            } else {
                Long value = getCount(columnReflectionWithSmudge)
                return value
            }
        }
    }
}