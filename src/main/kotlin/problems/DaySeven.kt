package fr.xibalba.adventOfCode2023.problems

val cardsNames = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2".split(", ").map { it[0] }

object DaySeven : Problem(7) {

    override fun solvePartOne(): Any {
        val handsWithBids = getInput().split(System.lineSeparator()).map { line ->
            val (hand, bid) = line.split(" ")
            val cards = hand.toCharArray().map { Card(it) }
            Hand(cards) to bid.toInt()
        }
        val sorted = handsWithBids.sortedBy { it.first }
        return sorted.mapIndexed { index, handWithBid -> handWithBid.second * (index + 1) }.sum()
    }

    override fun solvePartTwo(): Any {
        val handsWithBids = getInput().split(System.lineSeparator()).map { line ->
            val (hand, bid) = line.split(" ")
            val cards = hand.toCharArray().map { Card(it) }
            HandWithJoker(cards) to bid.toInt()
        }
        val sorted = handsWithBids.sortedBy { it.first }
        println(sorted.map { it.first to it.first.type }.joinToString("\n"))
        return sorted.mapIndexed { index, handWithBid -> handWithBid.second * (index + 1) }.sum()
    }

    @JvmInline
    private value class Hand(private val cards: List<Card>) : Comparable<Hand> {

        init {
            require(cards.size == 5) { "A hand must have 5 cards" }
        }

        private val type: HandType
            get() = when (cards.distinct().size) {
                1 -> HandType.FIVE_OF_A_KIND
                2 -> {
                    if (cards.groupBy { it }.values.any { it.size == 4 }) {
                        HandType.FOUR_OF_A_KIND
                    } else {
                        HandType.FULL_HOUSE
                    }
                }
                3 -> {
                    if (cards.groupBy { it }.values.any { it.size == 3 }) {
                        HandType.THREE_OF_A_KIND
                    } else {
                        HandType.TWO_PAIRS
                    }
                }

                4 -> HandType.PAIR
                else -> HandType.HIGH_CARD
            }

        override fun compareTo(other: Hand): Int {
            if (this.type.compareTo(other.type) == 0) {
                for (i in 0..<5) {
                    if (this.cards[i].compareTo(other.cards[i]) != 0) {
                        return -this.cards[i].compareTo(other.cards[i])
                    }
                }
                return 0
            } else {
                return this.type.compareTo(other.type)
            }
        }

        override fun toString(): String {
            return cards.joinToString("")
        }
    }

    @JvmInline
    private value class HandWithJoker(private val cards: List<Card>) : Comparable<HandWithJoker> {

        init {
            require(cards.size == 5) { "A hand must have 5 cards" }
        }

        val type: HandType
            get() = when (cards.filter { it != Card('J') }.distinct().size) {
                0, 1 -> HandType.FIVE_OF_A_KIND
                2 -> {
                    if (cards.groupBy { it }.values.any { it.size == 4 - cards.count { card -> card == Card('J') } }) {
                        HandType.FOUR_OF_A_KIND
                    } else {
                        HandType.FULL_HOUSE
                    }
                }
                3 -> {
                    if (cards.groupBy { it }.values.any { it.size == 3 - cards.count { card -> card == Card('J') } }) {
                        HandType.THREE_OF_A_KIND
                    } else {
                        HandType.TWO_PAIRS
                    }
                }
                4 -> HandType.PAIR
                else -> HandType.HIGH_CARD
            }

        override fun compareTo(other: HandWithJoker): Int {
            if (this.type.compareTo(other.type) == 0) {
                for (i in 0..<5) {
                    if (this.cards[i].compareTo(other.cards[i]) != 0) {
                        return if (this.cards[i] == Card('J'))
                            -1
                        else if (other.cards[i] == Card('J'))
                            1
                        else
                            -this.cards[i].compareTo(other.cards[i])
                    }
                }
                return 0
            } else {
                return this.type.compareTo(other.type)
            }
        }

        override fun toString(): String {
            return cards.joinToString("")
        }
    }

    private enum class HandType {
        HIGH_CARD,
        PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }

    @JvmInline
    private value class Card(private val value: Char) : Comparable<Card> {

        init {
            require(value in cardsNames) { "Invalid card value: $value" }
        }

        override fun compareTo(other: Card): Int {
            return cardsNames.indexOf(this.value) - cardsNames.indexOf(other.value)
        }

        override fun toString(): String {
            return value.toString()
        }
    }
}