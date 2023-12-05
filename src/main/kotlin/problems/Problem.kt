package fr.xibalba.adventOfCode2023.problems

import java.io.File

abstract class Problem(val day: Int) {

    abstract fun solvePartOne(): Any

    abstract fun solvePartTwo(): Any

    fun getInput(): String {
        return File("src/main/resources/input/$day.txt").readText()
    }

    fun getLink(): String {
        return "https://adventofcode.com/2023/day/$day"
    }
}