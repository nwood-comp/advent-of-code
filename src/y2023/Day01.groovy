package y2023

import common.DayRunner
import common.FileAccess

class Day01 extends DayRunner {
    Day01() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day01().run();
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        Map dict = ['one'  : '1',
                    'two'  : '2',
                    'three': '3',
                    'four' : '4',
                    'five' : '5',
                    'six'  : '6',
                    'seven': '7',
                    'eight': '8',
                    'nine' : '9']
        fileAccess.lines.sum { text ->
            boolean earlyReturn = false
            String firstInt

            text.eachWithIndex { letter, index ->
                if (earlyReturn) {
                    return
                } else if (letter in dict.values()) {
                    firstInt = letter
                    earlyReturn = true
                } else if (letter in ['o', 't', 'f', 's', 'e', 'n']) {
                    Set matching = dict.keySet().findAll { it ->
                        it[0] == letter
                    }
                    matching.each {
                        boolean result = true
                        it.eachWithIndex { String entry, int i ->
                            if (result) {
                                result = entry == text[index + i]
                            }
                        }
                        if (result) {
                            firstInt = dict[it]
                            earlyReturn = true
                        }
                    }
                }
            }

            String lastInt
            earlyReturn = false
            text.reverse().eachWithIndex { letter, index ->
                if (earlyReturn) {
                    return
                } else if (letter in dict.values()) {
                    lastInt = letter
                    earlyReturn = true
                } else if (letter in ['o', 't', 'f', 's', 'e', 'n', 'r', 'x']) {
                    Set matching = dict.keySet().findAll { it ->
                        it.reverse()[0] == letter
                    }
                    matching.each {
                        boolean result = true
                        it.reverse().eachWithIndex { String entry, int i ->
                            if (result) {
                                result = entry == text.reverse()[index + i]
                            }
                        }
                        if (result) {
                            lastInt = dict[it]
                            earlyReturn = true
                        }
                    }
                }
            }
            return Integer.parseInt(firstInt + lastInt)
        }
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        return null
    }
}