package y2023

import common.DayRunner
import common.FileAccess

class Day04 extends DayRunner {
    Day04() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day04().run();
    }

    Tuple2<List<List<Integer>>, List<List<Integer>>> parseInput(FileAccess fileAccess) {
        List<String> cards = fileAccess.lines.collect {
            it.split(/:/)[1].trim()
        }

        "abc".split(/^abc\|efg$/)


        List<List<Integer>> leftCards = cards.collect {
            it.split(/\|/)[0].trim().split(/ +/).collect{
                Integer.parseInt(it.trim())
            }
        }

        List<List<Integer>> rightCards = cards.collect {
            it.split(/\|/)[1].trim().split(/ +/)collect{
                Integer.parseInt(it.trim())
            }
        }

        return new Tuple2(leftCards, rightCards)
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        Tuple2<List<List<Integer>>, List<List<Integer>>> parsedInput = parseInput(fileAccess)
        List<List<Integer>> leftCards = parsedInput.v1
        List<List<Integer>> rightCards = parsedInput.v2

        Integer totalCount = 0

        leftCards.eachWithIndex { leftCard, index ->
            totalCount += leftCard.intersect(rightCards[index]).inject(1) { acc, entry ->
                return acc *= 2
            } / 2
        }

        return totalCount
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        Tuple2<List<List<Integer>>, List<List<Integer>>> parsedInput = parseInput(fileAccess)
        List<List<Integer>> leftCards = parsedInput.v1
        List<List<Integer>> rightCards = parsedInput.v2
        List<Integer> cardInstances = leftCards.collect { 1 }

        leftCards.eachWithIndex { leftCard, index ->
            leftCard.intersect(rightCards[index]).eachWithIndex { item, matchIndex ->
                cardInstances[index + matchIndex + 1] = cardInstances[index + matchIndex + 1] + cardInstances[index]
            }
        }

        return cardInstances.sum()
    }
}