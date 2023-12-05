package fr.xibalba.adventOfCode2023

fun <T> Iterable<T>.exclude(vararg elements: T): List<T> {
    return this.filter { it !in elements }
}