package fr.xibalba.adventOfCode2023.problems

import kotlin.math.max

object DayTen : Problem(10) {

    override fun solvePartOne(): Any {
        return PipeMap.fromString(getInput()).findAllLoopTiles().size / 2
    }

    override fun solvePartTwo(): Any {
        val loopTiles = PipeMap.fromString(getInput()).findAllLoopTiles()
        return max(
            area(loopTiles.filter { it.char in "LJ7FS" }.map { it.position }),
            area(loopTiles.filter { it.char in "LJ7FS" }.reversed().map { it.position })
        ) - loopTiles.size / 2 + 1
    }

    private data class PipeMap(val positons: List<PipeMapPosition>, val startingPoint: PipeMapPosition) {

        fun findAllLoopTiles(): List<PipeMapPosition> {
            var currentPosition = startingPoint
            var previousPosition: PipeMapPosition? = null
            val loopTiles = mutableListOf<PipeMapPosition>()
            for (direction in directions) {
                val nextPosition = currentPosition.plus(direction)
                if (nextPosition != null && nextPosition.connections?.has(currentPosition.position) == true) {
                    previousPosition = currentPosition
                    currentPosition = nextPosition
                    break
                }
            }
            loopTiles.add(currentPosition)
            while (currentPosition.position != startingPoint.position) {
                val nextPosition = currentPosition.connections.toListOrEmpty().find { it != previousPosition?.position }
                previousPosition = currentPosition
                currentPosition = positons.find { it.position == nextPosition } ?: throw Exception("No next position found for $currentPosition, $nextPosition")
                loopTiles.add(currentPosition)
            }
            return loopTiles
        }

        fun PipeMapPosition.plus(position: Pair<Int, Int>): PipeMapPosition? {
            val (x, y) = position
            return positons.find { it.position.first == this.position.first + x && it.position.second == this.position.second + y }
        }

        companion object {
            fun fromString(input: String): PipeMap {
                val lines = input.lines()
                val positions = mutableListOf<PipeMapPosition>()
                var startingPoint: PipeMapPosition? = null
                for ((x, line) in lines.iterator().withIndex()) {
                    for ((y, char) in line.iterator().withIndex()) {
                        when (char) {
                            '|' -> {
                                positions.add(PipeMapPosition(Pair(x, y), Pair(x - 1, y) to Pair(x + 1, y), char))
                            }
                            '-' -> {
                                positions.add(PipeMapPosition(Pair(x, y), Pair(x, y - 1) to Pair(x, y + 1), char))
                            }
                            'L' -> {
                                positions.add(PipeMapPosition(Pair(x, y), Pair(x - 1, y) to Pair(x, y + 1), char))
                            }
                            'J' -> {
                                positions.add(PipeMapPosition(Pair(x, y), Pair(x - 1, y) to Pair(x, y - 1), char))
                            }
                            '7' -> {
                                positions.add(PipeMapPosition(Pair(x, y), Pair(x + 1, y) to Pair(x, y - 1), char))
                            }
                            'F' -> {
                                positions.add(PipeMapPosition(Pair(x, y), Pair(x + 1, y) to Pair(x, y + 1), char))
                            }
                            '.' -> {
                                positions.add(PipeMapPosition(Pair(x, y), null, char))
                            }
                            'S' -> {
                                startingPoint = PipeMapPosition(Pair(x, y), null, char)
                                positions.add(startingPoint)
                            }
                        }
                    }
                }
                return PipeMap(positions, startingPoint!!)
            }
        }
    }

    fun area(points: List<Pair<Int, Int>>): Double {
        var area = 0.0
        for (i in points.indices) {
            area += points[i].first * points[(i + 1) % points.size].second - points[i].second * points[(i + 1) % points.size].first
        }
        return area / 2
    }

    private data class PipeMapPosition(val position: Pair<Int, Int>, val connections: Pair<Pair<Int, Int>, Pair<Int, Int>>?, val char: Char)

    private val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

    private fun <T> Pair<T, T>.has(element: T): Boolean {
        return this.first == element || this.second == element
    }

    private fun <T> Pair<T, T>?.toListOrEmpty() = this?.toList() ?: emptyList()
}