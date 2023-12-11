package fr.xibalba.adventOfCode2023.problems

import java.math.BigInteger

object DayEight : Problem(8) {

    override fun solvePartOne(): Any {
        val graph = getInput().substringAfter(System.lineSeparator().repeat(2)).split(System.lineSeparator())
            .map { it.split(" = ") }
            .associate { it[0] to it[1].substring(1, it[1].length - 1).split(", ").filter { edge -> edge != it[0] } }
            .let { AdjacencyList.fromMap(it) }
        val steps = getInput().substringBefore(System.lineSeparator().repeat(2)).toCharArray().map {
            when (it) {
                'L' -> 0
                'R' -> 1
                else -> throw IllegalArgumentException("Invalid input")
            }
        }
        var stepCount = 0
        val stepsSequence = sequence {
            while (true) {
                yield(steps[(stepCount % steps.size)])
                stepCount++
            }
        }
        graph.adjacencyMap.keys.find { it.data == "AAA" }!!.followSteps(stepsSequence.iterator())
        return stepCount + 1
    }

    override fun solvePartTwo(): Any {
        val graph = getInput().substringAfter(System.lineSeparator().repeat(2)).split(System.lineSeparator())
            .map { it.split(" = ") }
            .associate { it[0] to it[1].substring(1, it[1].length - 1).split(", ").filter { edge -> edge != it[0] } }
            .let { AdjacencyList.fromMap(it) }
        val steps = getInput().substringBefore(System.lineSeparator().repeat(2)).toCharArray().map {
            when (it) {
                'L' -> 0
                'R' -> 1
                else -> throw IllegalArgumentException("Invalid input")
            }
        }
        var stepCount = 0L
        val stepsSequence = sequence {
            while (true) {
                yield(steps[(stepCount % steps.size).toInt()])
                stepCount++
            }
        }
        return graph.adjacencyMap.keys.filter { it.data.endsWith("A") }.followStepsSynchronousAndReturnSteps(stepsSequence.iterator().withIndex())
    }

    private data class Vertex<T>(val index: Int, val data: T, val graph: AdjacencyList<T>) {

        fun addEdge(to: Vertex<T>) = graph.addDirectedEdge(this, to)

        fun edges() = graph.adjacencyMap[this] ?: mutableListOf()

        fun followSteps(steps: Iterator<Int>): Vertex<T> {
            var currentVertex = this
            var step: Int
            while (currentVertex.data != "ZZZ") {
                step = steps.next()
                currentVertex = currentVertex.edges()[step].to
            }
            return currentVertex
        }
    }

    private fun List<Vertex<String>>.followStepsSynchronousAndReturnSteps(steps: Iterator<IndexedValue<Int>>): BigInteger {
        var currentVertexes = this
        val founds = mutableListOf<Pair<Vertex<String>, Long>>()
        while (currentVertexes.isNotEmpty()) {
            val step = steps.next()
            currentVertexes = currentVertexes.map { it.edges()[step.value].to }
            for (vertex in currentVertexes) {
                if (vertex.data.endsWith("Z")) {
                    founds.add(vertex to step.index + 1L)
                    currentVertexes = currentVertexes.filter { it.data != vertex.data }
                }
            }
        }
        return lcm(founds.map { BigInteger.valueOf(it.second) }.toTypedArray())
    }

    private data class Edge<T>(val from: Vertex<T>, val to: Vertex<T>)

    private class AdjacencyList<T> {

        val adjacencyMap = mutableMapOf<Vertex<T>, MutableList<Edge<T>>>()

        fun createVertex(data: T): Vertex<T> {
            val vertex = Vertex(adjacencyMap.count(), data, this)
            adjacencyMap[vertex] = mutableListOf()
            return vertex
        }

        fun addDirectedEdge(from: Vertex<T>, to: Vertex<T>) {
            val edge = Edge(from, to)
            adjacencyMap[from]?.add(edge)
        }

        override fun toString(): String {
            return buildString {
                adjacencyMap.forEach { (vertex, edges) ->
                    val edgeString = edges.joinToString { it.to.data.toString() }
                    append("${vertex.data} ---> [ $edgeString ]\n")
                }
            }
        }

        companion object {

            fun <T> fromMap(map: Map<T, List<T>>): AdjacencyList<T> {
                val adjacencyList = AdjacencyList<T>()
                val vertices = map.map { adjacencyList.createVertex(it.key) }
                map.forEach { (source, targets) ->
                    val sourceVertex = vertices.find { it.data == source } ?: error("Something went wrong")
                    targets.forEach { target ->
                        val targetVertex = vertices.find { it.data == target } ?: error("Something went wrong")
                        sourceVertex.addEdge(targetVertex)
                    }
                }
                return adjacencyList
            }
        }
    }

    private fun gcd(a: BigInteger, b: BigInteger): BigInteger {
        var a = a
        var b = b
        while (b > BigInteger.ZERO) {
            val temp = b
            b = a % b
            a = temp
        }
        return a
    }

    private fun lcm(a: BigInteger, b: BigInteger): BigInteger {
        return a * (b / gcd(a, b))
    }

    private fun lcm(input: Array<BigInteger>): BigInteger {
        var result = input[0]
        for (i in 1..<input.size) result = lcm(result, input[i])
        return result
    }
}