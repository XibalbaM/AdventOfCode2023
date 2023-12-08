package fr.xibalba.adventOfCode2023.utils

fun <T> Iterable<T>.exclude(vararg elements: T): List<T> {
    return this.filter { it !in elements }
}