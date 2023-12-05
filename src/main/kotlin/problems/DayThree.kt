package fr.xibalba.adventOfCode2023.problems

object DayThree : Problem(3) {

    override fun solvePartOne(): Any {
        val input = getInput().replace(Char(13), '.').split("\n")
        val numbers = input.flatMapIndexed { index, line -> Regex("(\\d+)").findAll(line).map { Triple(index, it.range, it.value.toLong()) } }
        val numbersAdjacent = numbers.filter { (line, position, _) ->
            allAdjacentCharacters(input, line, position).any { it.third != '.'}
        }
        return numbersAdjacent.sumOf { it.third }
    }

    override fun solvePartTwo(): Any {
        val input = getInput().replace(Char(13), '.').split("\n")
        val numbers = input.flatMapIndexed { index, line -> Regex("(\\d+)").findAll(line).map { Triple(index, it.range, it.value.toLong()) } }
        val numbersAdjacentsToGears = numbers.map { it to allAdjacentCharacters(input, it.first, it.second) }.filter { list -> list.second.any { it.third == '*' } }
        val numbersWithGear = numbersAdjacentsToGears.map { it.first to it.second.first { charWithPos -> charWithPos.third == '*' } }
        val numbersByGear = numbersWithGear.groupBy { it.second }.mapValues { list -> list.value.map { it.first.third } }.filter { it.value.size > 1 }
        val numbersByGearWithProduct = numbersByGear.map { it.key to it.value.reduce { acc, l -> acc * l } }
        return numbersByGearWithProduct.sumOf { it.second }
    }

    fun allAdjacentCharacters(input: List<String>, line: Int, position: IntRange): List<Triple<Int, Int, Char>> {
        val width = input[0].length
        val height = input.size
        val charactersAboveOrUnder = position.plus(position.first - 1).plus(position.last + 1).filter { it in 0..width }.flatMap {
            val characters = mutableListOf<Triple<Int, Int, Char>>()
            if ( line != 0) {
                if (input[line - 1][it] != '.') {
                    characters.add(Triple(line -1, it, input[line - 1][it]))
                }
            }
            if (line != height - 1) {
                if (input[line + 1][it] != '.') {
                    characters.add(Triple(line + 1, it, input[line + 1][it]))
                }
            }
            characters
        }.toMutableList()
        if (position.first != 0) {
            if (input[line][position.first - 1] != '.') {
                charactersAboveOrUnder.add(Triple(line, position.first - 1, input[line][position.first - 1]))
            }
        }
        if (position.last != width - 1) {
            if (input[line][position.last + 1] != '.') {
                charactersAboveOrUnder.add(Triple(line, position.last + 1, input[line][position.last + 1]))
            }
        }
        return charactersAboveOrUnder
    }
}