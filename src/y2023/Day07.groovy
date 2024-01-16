package y2023

import common.DayRunner
import common.FileAccess

class Day07 extends DayRunner {
    Day07() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day07().run();
    }

    def parseInput(FileAccess fileAccess) {
        return fileAccess.lines
    }

    List strengths = [7, 6, 5, 4, 3, 2, 1]
    List<String> cards = ['A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'].reverse()

    class Hand implements Comparable {
        String hand
        Integer strength
        Integer bid

        Hand(String input) {
            hand = input.split(/ /)[0]
            bid = Integer.parseInt(input.split(/ /)[1])
            List<Character> characters = hand.toCharArray().toList()
            if (!characters.contains('J' as char)) {
                if (hand.count(characters[0].toString()) == 5) {
                    this.strength = strengths[0]//five
                } else if (hand.count(characters[0].toString()) == 4 || hand.count(characters[1].toString()) == 4) {
                    this.strength = strengths[1]//four
                } else if (characters.unique().size() == 2 && (hand.count(characters[0].toString()) == 3 || hand.count(characters[0].toString()) == 2)) {
                    this.strength = strengths[2]//full
                } else if (hand.count(characters[0].toString()) == 3 || hand.count(characters[1].toString()) == 3 || hand.count(characters[2].toString()) == 3) {
                    this.strength = strengths[3]//three
                } else if (characters.unique().size() == 3 && characters.every { hand.count(it.toString()) == 2 || hand.count(it.toString()) == 1 }) {
                    this.strength = strengths[4]//two
                } else if (characters.unique().size() == 4) {
                    this.strength = strengths[5] //one
                } else {
                    this.strength = strengths[6]//high
                }
            } else {
                int jokerCount = characters.count('J' as char)
                if (jokerCount == 5) {
                    this.strength = strengths[0]//five
                } else {
                    characters.remove('J' as char); characters.remove('J' as char); characters.remove('J' as char);
                    characters.remove('J' as char);
                    int bestCardCount = characters.count(characters[(0..(4 - jokerCount)).max {
                        characters.count(characters[it])
                    }])
                    if (bestCardCount + jokerCount == 5) {
                        this.strength = strengths[0]//five
                    } else if (bestCardCount + jokerCount == 4) {
                        this.strength = strengths[1]//four
                    } else if (bestCardCount + jokerCount == 3 && characters.unique().size() == 2) {
                        this.strength = strengths[2]//full
                    } else if (bestCardCount + jokerCount == 3) {
                        this.strength = strengths[3]//three
                    } else if (bestCardCount + jokerCount == 2) {
                        this.strength = strengths[5]//pair
                    } else {
                        throw new RuntimeException('ope2')
                    }
                }
            }
            true
        }

        @Override
        int compareTo(Object o) {
            assert o instanceof Hand
            if (this.strength == o.strength) {
                if (this.hand[0] != o.hand[0]) {
                    return cards.indexOf(this.hand[0]) <=> cards.indexOf(o.hand[0])
                } else if (this.hand[1] != o.hand[1]) {
                    return cards.indexOf(this.hand[1]) <=> cards.indexOf(o.hand[1])
                } else if (this.hand[2] != o.hand[2]) {
                    return cards.indexOf(this.hand[2]) <=> cards.indexOf(o.hand[2])
                } else if (this.hand[3] != o.hand[3]) {
                    return cards.indexOf(this.hand[3]) <=> cards.indexOf(o.hand[3])
                } else if (this.hand[4] != o.hand[4]) {
                    return cards.indexOf(this.hand[4]) <=> cards.indexOf(o.hand[4])
                } else {
                    throw new RuntimeException('ope')
                }
            } else {
                return this.strength <=> o.strength
            }
        }
    }

    @Override
    Object partOne(FileAccess fileAccess) {
//        List<String> parsedInput = parseInput(fileAccess)
//        List<Hand> handsPoints = parsedInput.collect { new Hand(it) }
//        handsPoints.sort(true)
//
//        long sum = 0
//        handsPoints.eachWithIndex { handPoint, index ->
//            sum += handPoint.bid * (index + 1)
//        }
//        return sum
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        long sum = 0
        fileAccess.lines.collect { new Hand(it) }.sort().eachWithIndex { Hand hand, int index, int mult = index + 1 ->
            sum += hand.bid * mult
        }
        return sum
    }
}