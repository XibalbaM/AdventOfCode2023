package fr.xibalba.adventOfCode2023.problems

object DayNine : Problem(9) {

    override fun solvePartOne(): Any {
        val input = getInput().split(System.lineSeparator()).map { it.split(" ").map { number -> number.toInt() } }
        return input.sumOf { it.asSequence().predictNext() }
    }

    override fun solvePartTwo(): Any {
        val input = getInput().split(System.lineSeparator()).map { it.split(" ").map { number -> number.toInt() } }
        return input.sumOf { it.asSequence().predictPrevious() }
    }

    private fun Sequence<Int>.predictNext(): Int {
        val list = this.toList()
        if (list.all { it == 0 })
            return 0
        return list.last() +
                sequence {
                    for (i in 0..<(list.count() - 1)) {
                        val current = list[i]
                        val next = list[(i + 1)]
                        yield(next - current)
                    }
                }.predictNext()
    }

    private fun Sequence<Int>.predictPrevious(): Int {
        val list = this.toList()
        if (list.all { it == 0 })
            return 0
        return list.first() -
                sequence {
                    for (i in 0..<(list.count() - 1)) {
                        val current = list[i]
                        val next = list[(i + 1)]
                        yield(next - current)
                    }
                }.predictPrevious()
    }
}