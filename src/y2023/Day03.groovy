package y2023

import common.DayRunner
import common.FileAccess

class Day03 extends DayRunner {
    Day03() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day03().run();
    }

    def parseInput(FileAccess fileAccess) {
        List<String> input = fileAccess.lines
        List<List<Integer>> numberInput = []
        input.eachWithIndex { line, lineIndex ->
            List<Integer> parsedLine = []
            line.eachWithIndex { character, columnIndex ->
                if (character in ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0']) {
                    if (columnIndex != 0 && parsedLine[columnIndex - 1] && parsedLine[columnIndex - 1] >= 0) {
                        parsedLine << parsedLine.toList()[columnIndex - 1]
                    } else {
                        String subNumber = line ?[columnIndex..-1].split(/[a-zA-Z&_\.\*#%\+@\$\/=-]/)[0]
                        parsedLine << Integer.parseInt(subNumber)
                    }
                } else if (character == '.') {
                    parsedLine << 0
                } else {
                    parsedLine << -1
                }
            }
            numberInput << parsedLine
        }
        return numberInput
    }

    @Override
    Object partOne(FileAccess fileAccess) {
//        List<List<Integer>> parsedInput = parseInput(fileAccess)
//        List partNumbers = []
//        parsedInput.eachWithIndex { row, rowIndex ->
//            row.eachWithIndex { number, columnIndex ->
//                Set totalsToAdd = []
//                if (number == -1) {
//                    Set neighbors = findNeighbors(parsedInput, rowIndex, columnIndex).toSet()
//                    neighbors = neighbors - -1
//                    neighbors = neighbors - 0
//                    partNumbers.addAll(neighbors)
//                }
//            }
//        }
//        return partNumbers.sum()
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        def parsedInput = parseInput(fileAccess)
        List partNumbers = []
        parsedInput.eachWithIndex { row, rowIndex ->
            row.eachWithIndex { number, columnIndex ->
                Set totalsToAdd = []
                if (number == -1) {
                    Set neighbors = findNeighbors(parsedInput, rowIndex, columnIndex).toSet()
                    neighbors = neighbors - -1
                    neighbors = neighbors - 0
                    if(neighbors.size() == 2) {
                        partNumbers << neighbors[0] * neighbors[1]
                    }
                }
            }
        }
        return partNumbers.sum()
    }

    def List findNeighbors(List<List<Integer>> parsedInput, int row, int column) {
        List neighbors = []

        //row -1
        if (row+1 > 0) {
            if (column > 0) {
                neighbors << parsedInput[row - 1][column - 1]
            }
            neighbors << parsedInput[row - 1][column]
            neighbors << parsedInput[row - 1][column + 1]
        }

        if (column > 0) {
            neighbors << parsedInput[row][column - 1]
        }
        neighbors << parsedInput[row][column]
        neighbors << parsedInput[row][column + 1]

        if (row+1 < parsedInput.size()) {
            if (column > 0) {
                try {
                neighbors << parsedInput[row + 1][column - 1]
                } catch (any) {
                    true
                }
            }
            neighbors << parsedInput[row + 1][column]
            neighbors << parsedInput[row + 1][column + 1]
        }

        return neighbors
    }
}