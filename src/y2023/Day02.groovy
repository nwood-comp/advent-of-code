package y2023

import common.DayRunner
import common.FileAccess

class Day02 extends DayRunner {
    Day02() {
        super(null, false);
    }

    static void main(String[] args) {
        new Day02().run();
    }

    private List<List<String>> parseInput(FileAccess fileAccess) {
        List<String> games = fileAccess.lines
        List<String> trimmedGames = games.collect { it.split(/:/)[1].trim() }
        trimmedGames.collect {
            it.split(/[;,]/).collect {
                it.trim()
            }
        }
    }

    @Override
    Object partOne(FileAccess fileAccess) {
        // [["3 blue", "4 red", "1 red", "2 green", "6 blue", "2 green"],...]
        List<List<String>> gamePulls = parseInput(fileAccess)

        Map restriction = [
                'red'  : 12,
                'green': 13,
                'blue' : 14
        ]

        Integer acc = 0
        gamePulls.eachWithIndex { game, index ->
            boolean validGame = game.every { pull ->
                def (String count, String color) = pull.split(/ /)
                return Integer.parseInt(count) <= restriction[color]
            }
            acc += validGame ? index + 1 : 0
        }
        return acc
    }

    @Override
    Object partTwo(FileAccess fileAccess) {
        List<List<String>> gamePulls = parseInput(fileAccess)

        return gamePulls.sum { game ->
            Map maxCounts = ['red': 0, 'green': 0, 'blue': 0]

            game.each { pull ->
                def (String count, String color) = pull.split(/ /)
                maxCounts[color] = Math.max(maxCounts[color], Integer.parseInt(count))
            }

            return maxCounts['red'] * maxCounts['green'] * maxCounts['blue']
        }
    }
}