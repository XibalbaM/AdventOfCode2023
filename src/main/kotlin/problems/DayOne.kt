package fr.xibalba.adventOfCode2023.problems

import java.io.File

object DayOne : Problem(1) {

    override fun solvePartOne(): Any {
        val data = getInput().readLines()
        val numbers = data.map { it.first { char -> char.isDigit() } + "" + it.last { char -> char.isDigit() } }
        return numbers.sumOf { it.toInt() }
    }

    override fun solvePartTwo(): Any {
        val digitMap = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9"
        )
        val data = getInput().readLines()
        val firstData = data.map { it.replaceMapFirst(digitMap) }
        val lastData = data.map { it.replaceMapLast(digitMap) }
        val numbers = List(data.size) { index -> firstData[index].first { char -> char.isDigit() } + "" + lastData[index].last { char -> char.isDigit() } }
        println(numbers)
        return numbers.sumOf { it.toInt() }
    }
}

fun String.replaceMapFirst(map: Map<String, String>): String {
    var result = this
    val indexes = map.map { it to this.indexOf(it.key) }.filter { it.second != -1 }.sortedBy { it.second }.map { it.first }
    indexes.forEach { result = result.replace(it.key, it.value) }
    return result
}

fun String.replaceMapLast(map: Map<String, String>): String {
    var result = this
    val indexes = map.map { it to this.lastIndexOf(it.key) }.filter { it.second != -1 }.sortedBy { it.second }.reversed().map { it.first }
    indexes.forEach { result = result.replace(it.key, it.value) }
    return result
}