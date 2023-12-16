package fr.xibalba.adventOfCode2023.problems

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

object DayEleven : Problem(11) {

    private val universe = Grid(getInput().lines().map {
        it.toCharArray().map { char ->
            when (char) {
                '.' -> false
                '#' -> true
                else -> throw Exception("Invalid char $char")
            }
        }.toMutableList()
    }.toMutableList()) {
        !it
    }

    private val galaxies by lazy {
        val galaxies = mutableListOf<Pair<Int, Int>>()
        universe.rows().forEachIndexed { x, row ->
            row.forEachIndexed { y, value ->
                if (value) {
                    galaxies.add(Pair(x, y))
                }
            }
        }
        galaxies
    }

    private val pairs = galaxies.allPossiblePairs()

    override fun solvePartOne(): Any {
        universe.emptyLineFactor = 2
        val distances = pairs.map { universe.stepsToGoFromTo(it.first, it.second) }
        return distances.sum()
    }

    override fun solvePartTwo(): Any {
        universe.emptyLineFactor = 1_000_000
        val distances = pairs.map { universe.stepsToGoFromTo(it.first, it.second) }
        return distances.sum()
    }

    private class Grid<T>(private val list: MutableList<MutableList<T>>, var emptyLineFactor: Long = 2, private val emptinessCriterion: (T) -> Boolean) : List<List<T>> by list {

        init {
            require(list.isNotEmpty()) { "Grid must not be empty" }
            require(list.all { it.size == list[0].size }) { "Grid must be rectangular" }
        }

        val emptyRows = this.rows().mapIndexed { index, row -> index to row }.filter { it.second.all { emptinessCriterion(it) } }.map { it.first }
        val emptyColumns = this.columns().mapIndexed { index, column -> index to column }.filter { it.second.all { emptinessCriterion(it) } }.map { it.first }

        fun get(x: Int, y: Int): T {
            return list[x][y]
        }

        fun rows(): List<List<T>> {
            return list
        }

        fun columns(): List<List<T>> {
            return list[0].indices.map { x -> list.map { it[x] } }
        }

        fun insertRow(row: List<T>, index: Int) {
            require(row.size == list[0].size) { "Row must have the same size as the grid" }
            list.apply { add(index, row.toMutableList()) }
        }

        fun insertColumn(column: List<T>, index: Int) {
            require(column.size == list.size) { "Column must have the same size as the grid" }
            column.forEachIndexed { i, value -> list[i].add(index, value) }
        }

        fun stepsToGoFromTo(start: Pair<Int, Int>, end: Pair<Int, Int>): Long {
            val (startX, startY) = start
            val (endX, endY) = end
            var emptyLineCount = 0L
            for (i in min(startX, endX)..max(startX, endX)) {
                if (i in emptyRows) {
                    emptyLineCount += emptyLineFactor - 1
                }
            }
            for (i in min(startY, endY)..max(startY, endY)) {
                if (i in emptyColumns) {
                    emptyLineCount += emptyLineFactor - 1
                }
            }
            return (startX - endX).absoluteValue.toLong() + (startY - endY).absoluteValue.toLong() + emptyLineCount
        }

        override fun toString(): String {
            return list.joinToString("\n") { it.joinToString(", ") }
        }
    }

    private fun Grid<Boolean>.toChars(): String {
        return this.joinToString("\n") { it.joinToString("") { if (it) "#" else "." } }
    }

    private fun <T> List<T>.allPossiblePairs(): List<Pair<T, T>> {
        val pairs = mutableListOf<Pair<T, T>>()
        for (i in this.indices) {
            for (j in i + 1..<this.size) {
                if (i != j)
                    pairs.add(Pair(this[i], this[j]))
            }
        }
        return pairs
    }
}