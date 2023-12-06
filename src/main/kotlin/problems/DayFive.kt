package fr.xibalba.adventOfCode2023.problems

object DayFive : Problem(5) {

    override fun solvePartOne(): Any {
        val input = getInput()
        val seeds = input.substringBefore(System.lineSeparator().repeat(2)).substringAfter(": ").split(" ").map { it.toLong() }
        val transformations = TransformationChain.fromString(input.substringAfter(System.lineSeparator().repeat(2)))
        return seeds.minOf { transformations.transform(it) }
    }

    override fun solvePartTwo(): Any {
        val input = getInput()
        val seeds = input
            .substringBefore(System.lineSeparator().repeat(2))
            .substringAfter(": ")
            .split(" ")
            .map { it.toLong() }
            .windowed(2, 2)
            .map { it.first()..(it.first() + it.last()) }
        val numbers = generateSequence(1L) { it + 1 }
        val transformations = TransformationChain.fromString(input.substringAfter(System.lineSeparator().repeat(2)))
        return numbers.first { val value = transformations.reverseTransform(it); seeds.any { range -> value in range} }
    }
}

class TransformationChain(private vararg val transformations: TransformationMap) {

    private fun transform(input: Long, transformations: List<TransformationMap>, reverse: Boolean): Long {
        return transformations.fold(input) { acc, transformation ->
            if (reverse) transformation.reverseTransform(acc)
            else transformation.transform(acc)
        }
    }

    fun transform(input: Long): Long {
        return transform(input, transformations.toList(), false)
    }

    fun reverseTransform(input: Long): Long {
        return transform(input, transformations.reversed(), true)
    }

    companion object {
        fun fromString(input: String): TransformationChain {
            val transformations = input.split(System.lineSeparator().repeat(2)).map {
                TransformationMap.fromString(it.substringAfter(System.lineSeparator()))
            }
            return TransformationChain(*transformations.toTypedArray())
        }
    }
}

class TransformationMap(vararg exceptions: Triple<Long, Long, Long>) {

    private val exceptions = exceptions.associate {
        (it.first..(it.first + it.third)) to it.second
    }

    private val reverseExceptions = exceptions.associate {
        (it.second..(it.second + it.third)) to it.first
    }

    private fun transform(input: Long, exceptions: Map<LongRange, Long>): Long {
        val exception = exceptions.entries.find { input in it.key }
        return exception?.let { it.value + (input - it.key.first) } ?: input
    }

    fun transform(input: Long): Long {
        return transform(input, exceptions)
    }

    fun reverseTransform(input: Long): Long {
        return transform(input, reverseExceptions)
    }

    companion object {
        fun fromString(input: String): TransformationMap {
            val exceptions = input.split(System.lineSeparator()).map { line ->
                val (to, from, size) = line.split(" ").map { it.toLong() }
                Triple(from, to, size)
            }
            return TransformationMap(*exceptions.toTypedArray())
        }
    }
}