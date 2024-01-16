package y2023

import common.DayRunner
import common.FileAccess

class Day08 extends DayRunner {
    Day08() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day08().run();
    }

    def parseInput(FileAccess fileAccess) {
        List<String> lines = fileAccess.lines[2..-1]
        Map<String, List<String>> movements = [:]
        lines.each({
            String key = it.split(/ = /)[0]
            List<String> value = it.split(/ = /)[1].replace('(', '').replace(')', '').split(/,/)*.trim()
            movements.put(key, value)
        })
        return movements
    }

    @Override
    Object partOne(FileAccess fileAccess) {
//        List<Character> instructions = fileAccess.lines.first().toCharArray().toList()
//        LinkedHashMap<String, List<String>> parsedInput = parseInput(fileAccess)
//        String position = 'AAA'
//        int index = 0
//        int moves = 0
//        do {
//            position = move(position, instructions[index], parsedInput)
//            index = (index + 1) % instructions.size()
//            moves++
//        } while (position != 'ZZZ')
//        return moves
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        List<Character> instructions = fileAccess.lines.first().toCharArray().toList()
        LinkedHashMap<String, List<String>> parsedInput = parseInput(fileAccess)
        List<String> positions = parsedInput.keySet().findAll {
            it.endsWith('A')
        }.toList()
        List<Integer> timesToZ = []
        positions.each { position ->
            //find time until end
            long index = 0
            int moves = 0
            int timeToZ = -1
            int cycles = 0

            do {
                char instruction = instructions[index]
                position = move(position, instruction, parsedInput)
                moves++
                index = (index + 1) % instructions.size()

                if (index == 0) {
                    cycles++
                    if (position.endsWith('Z')) {
                        timeToZ = cycles
                        timesToZ << timeToZ
                    }
                }
            } while (timeToZ == -1)
        }

        return lcm_of_array_elements(timesToZ as int[]) * instructions.size()
        //53715412391
    }

    String move(String position, char instruction, LinkedHashMap<String, List<String>> parsedInput) {
        int leftRight = (instruction == 'L' as char) ? 0 : 1
        return parsedInput[position][leftRight]
    }

    public static long lcm_of_array_elements(int[] element_array)
    {
        long lcm_of_array_elements = 1;
        int divisor = 2;

        while (true) {
            int counter = 0;
            boolean divisible = false;

            for (int i = 0; i < element_array.length; i++) {

                // lcm_of_array_elements (n1, n2, ... 0) = 0.
                // For negative number we convert into
                // positive and calculate lcm_of_array_elements.

                if (element_array[i] == 0) {
                    return 0;
                }
                else if (element_array[i] < 0) {
                    element_array[i] = element_array[i] * (-1);
                }
                if (element_array[i] == 1) {
                    counter++;
                }

                // Divide element_array by devisor if complete
                // division i.e. without remainder then replace
                // number with quotient; used for find next factor
                if (element_array[i] % divisor == 0) {
                    divisible = true;
                    element_array[i] = element_array[i] / divisor;
                }
            }

            // If divisor able to completely divide any number
            // from array multiply with lcm_of_array_elements
            // and store into lcm_of_array_elements and continue
            // to same divisor for next factor finding.
            // else increment divisor
            if (divisible) {
                lcm_of_array_elements = lcm_of_array_elements * divisor;
            }
            else {
                divisor++;
            }

            // Check if all element_array is 1 indicate
            // we found all factors and terminate while loop.
            if (counter == element_array.length) {
                return lcm_of_array_elements;
            }
        }
    }
}