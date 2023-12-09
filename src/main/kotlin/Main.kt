package fr.xibalba.adventOfCode2023

import fr.xibalba.adventOfCode2023.problems.DaySeven

fun main() {
    val problem = DaySeven
    println("Day ${problem.day} - ${problem.getLink()}")
    println("Part one: ${problem.solvePartOne()}")
    println("Part two: ${problem.solvePartTwo()}")
}