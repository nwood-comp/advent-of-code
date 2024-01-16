package y2023

import common.DayRunner
import common.FileAccess
import groovy.transform.EqualsAndHashCode

class Day15 extends DayRunner {
    Day15() {
        super(null, true);
    }

    static void main(String[] args) {
        new Day15().run();
    }

    List<List<Character>> parseInput(FileAccess fileAccess) {
//        List<List<Character>> steps = fileAccess.text.split(/,/)*.replaceAll(/=\d+/, '')*.toCharArray()*.toList()
        List<List<Character>> steps = fileAccess.text.split(/,/)*.toCharArray()*.toList()
        return steps
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        List<List<Character>> parsedInput = parseInput(fileAccess)
        return parsedInput.sum {
            hashCharacters(it)
        }
    }

    int hashCharacters(List<Character> it) {
        it.inject(0) { acc, character ->
            acc += (int) character
            acc *= 17
            acc = acc % 256
            return acc
        }
    }

    long calculateFocalPower(LinkedHashMap<Integer, List<Box>> boxes) {
        boxes.collect { it }.sum {
            long sum = 0
            long num1 = 1 + it.key
            it.value.eachWithIndex { box, i ->
                sum += num1 * (i + 1) * box.focalLength
            }
            return sum
        }
    }

    @EqualsAndHashCode(excludes = ['focalLength'])
    class Box {
        List<Character> characters
        int focalLength

        Box(List<Character> characters) {
            this.characters = characters
        }

        Box(List<Character> characters, int focalLength) {
            this.characters = characters
            this.focalLength = focalLength
        }
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        List<List<Character>> parsedInput = parseInput(fileAccess)

        HashMap<Integer, List<Box>> boxes = [:]
        parsedInput.each { instruction ->
            List<Character> hash = instruction.findAll { it ==~ /[a-z,A-Z]/ }

            List<Box> boxesAtHash = boxes.computeIfAbsent(hashCharacters(hash), (a) -> new ArrayList<Box>())
            if (instruction.contains('-' as Character)) {
                def boxToRemove = new Box(hash)
                boxesAtHash.remove(boxToRemove)
            } else {
                List<Character> focalLength = instruction.findAll { it ==~ /\d/ }
                def boxToAdd = new Box(hash, Integer.valueOf(focalLength.join('')))
                int oldBoxLocation = boxesAtHash.indexOf(boxToAdd)
                if (oldBoxLocation > -1) {
                    boxesAtHash[oldBoxLocation] = boxToAdd
                } else {

                    boxesAtHash.add(boxToAdd)
                }
            }
        }

        return calculateFocalPower(boxes)
    }
}