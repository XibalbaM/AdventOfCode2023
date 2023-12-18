package fr.xibalba.adventOfCode2023.problems

object DayTwelve : Problem(12) {

    override fun solvePartOne(): Any {
        val rows = getInput().lines().map { line ->
            line.split(" ").let {(report, stats) ->
                Row(report, stats.split(",").map { it.toInt() })
            }
        }
        return rows.sumOf { it.allPossibleArrangements().also { println(it) } }
    }

    override fun solvePartTwo(): Any {
        val rows = getInput().lines().map { line ->
            line.split(" ").let {(report, stats) ->
                Row("$report?".repeat(5).dropLast(1), stats.split(",").map { it.toInt() }.repeat(5))
            }
        }
        return rows.sumOf { it.allPossibleArrangements().also { println(it) } }
    }
}

private typealias Row = Pair<String, List<Int>>

private fun Row.allPossibleArrangements(): Long {
    val cache = mutableMapOf<String, Long>()
    val (springs, hints) = this
    val knownBroken = springs.indices.filter { springs[it] == '#' }
    fun recursiveSearch(hintIndex: Int = 0, startIndex: Int = 0, ranges: List<IntRange> = emptyList()): Long {
        val key = "$hintIndex-$startIndex"
        cache[key]?.let { return it }
        if (hintIndex == hints.size) return 0
        val minLength = hints.subList(hintIndex, hints.size).sum() + hints.size - hintIndex - 1
        val rightBound = springs.length - minLength
        if (startIndex > rightBound) return 0

        val endIndex = startIndex + hints[hintIndex]
        val possibleWorkingSprings = springs.substring(startIndex, endIndex)
        val options = if (
            possibleWorkingSprings.all { it == '#' || it == '?' } &&
            (startIndex == 0 || springs[startIndex - 1] != '#') &&
            (endIndex == springs.length || springs[endIndex] != '#')
        ) {
            val possibleRanges = ranges + listOf(startIndex until endIndex)
            (if (hintIndex == hints.size - 1
                && knownBroken.all {brokenSpring -> possibleRanges.any { brokenSpring in it } })
                1 else 0) + recursiveSearch(
                hintIndex + 1,
                endIndex + 1,
                possibleRanges
            )
        } else 0
        val total = options + recursiveSearch(hintIndex, startIndex + 1, ranges)
        cache[key] = total
        return total
    }
    return recursiveSearch()
}

private fun <T> List<T>.repeat(n: Int): List<T> {
    val list = mutableListOf<T>()
    for (i in 1..n) list.addAll(this)
    return list
}