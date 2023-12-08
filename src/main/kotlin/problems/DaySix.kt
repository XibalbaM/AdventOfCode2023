package fr.xibalba.adventOfCode2023.problems

object DaySix : Problem(6) {

    override fun solvePartOne(): Any {
        val input = getInput()
        val races = (input.substringBefore(System.lineSeparator()) to input.substringAfter(System.lineSeparator()))
            .toList()
            .map { it.substringAfter(':') }
            .map { it.split(" ") }
            .map { it.filterNot { string -> string.isEmpty() } }
            .map { it.map { number -> number.toInt() } }
            .let { it[0].zip(it[1]) }
            .toMap()
        val timeRanges = races.map { (time, distance) ->
            val numbers = (0..time).filter { distanceForPressingXMs(it, time) > distance }
            numbers.first()..numbers.last()
        }
        println(timeRanges)
        return timeRanges.map { it.toList().size }.reduce { acc, i -> i * acc }
    }

    private fun distanceForPressingXMs(x: Int, time: Int) = (time - x) * x

    override fun solvePartTwo(): Any {
        val input = getInput()
        val (time, distance) = input.replace(" ", "")
            .split(System.lineSeparator())
            .map { it.substringAfter(":") }
            .map { it.toLong() }
            .let { it[0] to it[1] }
        val numbers = (0..time).filter { distanceForPressingXMs(it, time) > distance }
        return numbers.last() - numbers.first() + 1
    }

    private fun distanceForPressingXMs(x: Long, time: Long) = (time - x) * x
}