package fr.xibalba.adventOfCode2023

import fr.xibalba.adventOfCode2023.problems.DayEleven

fun main() {
    val problem = DayEleven
    println("Day ${problem.day} - ${problem.getLink()}")
    println("Part one: ${problem.solvePartOne()}")
    println("Part two: ${problem.solvePartTwo()}")
}