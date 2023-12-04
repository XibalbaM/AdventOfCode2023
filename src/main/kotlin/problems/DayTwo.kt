package fr.xibalba.adventOfCode2023.problems

object DayTwo : Problem(2) {
    override fun solvePartOne(): Any {
        val input = getInput().split("\n")
        val games = input.map { Game.fromString(it) }
        val possibleGames = games.filter { it.maxDrawnValues().eachColorUnder(Draw(12, 13, 14)) }
        return possibleGames.sumOf { it.id }
    }

    override fun solvePartTwo(): Any {
        val input = getInput().split("\n")
        val games = input.map { Game.fromString(it) }
        val powers = games.map { it.maxDrawnValues().power() }
        return powers.sum()
    }
}

data class Game(val id: Int, val draws: List<Draw>) {

    fun maxDrawnValues(): Draw {
        return Draw(draws.maxOf { it.red }, draws.maxOf { it.green }, draws.maxOf { it.blue })
    }

    companion object {
        fun fromString(input: String): Game {
            val id = input.substringAfter("Game ").substringBefore(":").toInt()
            val draws = input.substringAfter("Game $id: ").split("; ").map { Draw.fromString(it) }
            return Game(id, draws)
        }
    }
}

data class Draw(val red: Int, val green: Int, val blue: Int) {

    fun eachColorUnder(other: Draw): Boolean {
        return red <= other.red && green <= other.green && blue <= other.blue
    }

    fun power(): Int {
        return red * green * blue
    }

    companion object {
        fun fromString(input: String): Draw {
            val colors = input.split(", ")
            var red = 0
            var green = 0
            var blue = 0
            colors.forEach {
                when (it.substringAfter(" ")) {
                    "red" -> red = it.substringBefore(" ").toInt()
                    "green" -> green = it.substringBefore(" ").toInt()
                    "blue" -> blue = it.substringBefore(" ").toInt()
                }
            }
            return Draw(red, green, blue)
        }
    }
}