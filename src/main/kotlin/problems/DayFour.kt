package fr.xibalba.adventOfCode2023.problems

import fr.xibalba.adventOfCode2023.utils.exclude
import kotlin.math.pow

object DayFour : Problem(4) {

    private val input = getInput()
        .split(System.lineSeparator())
        .map { it.substringAfter(": ") }
        .map {
            it.substringBefore(" | ").split(" ").exclude("").map(String::toInt) to
                    it.substringAfter(" | ").split(" ").exclude("").map(String::toInt)
        }

    override fun solvePartOne(): Any {
        val score = input.sumOf { (winning, own) ->
            2.0.pow(countWins(own, winning) - 1.0).toInt().coerceAtLeast(0)
        }
        return score
    }

    override fun solvePartTwo(): Any {
        return countWonCards(input) + input.size
    }

    private fun countWonCards(cards: List<Pair<List<Int>, List<Int>>>, cardsToCheck: List<Pair<List<Int>, List<Int>>> = cards, founds: MutableMap<Int, Int> = mutableMapOf()): Int {
        var count = 0
        cardsToCheck.reversed().forEach{ (winning, own) ->
            val index = cards.indexOfFirst { it.first == winning && it.second == own }
            if (index in founds) {
                count += founds[index]!!
            } else {
                val wins = countWins(own, winning)
                if (wins > 0) {
                    count += wins
                    val check = countWonCards(cards, cards.subList(index + 1, index + wins + 1), founds)
                    count += check
                    founds[index] = check + wins
                } else {
                    founds[index] = 0
                }
            }
        }
        return count
    }

    fun countWins(ownedNumbers: List<Int>, winningNumbers: List<Int>) = ownedNumbers.count { it in winningNumbers }
}