fun main() {
    fun parse(str: String): List<String> {
        var output = str
        var openBracketCount = 0
        (str.indices).forEach { i ->
            if (str[i] == '[') openBracketCount++
            if (str[i] == ']') openBracketCount--
            if (str[i] == ',' && openBracketCount == 0) output = output.substring(0, i) + "\n" + output.substring(i + 1, output.length)
        }
        return output.split("\n")
    }

    fun readStringToModel(str: String): Day13Model {
        return if (str[0] == '[' && str[str.length - 1] == ']') {
            val members = mutableListOf<Day13Model>()
            val textToParse = str.substring(1, str.length - 1)
            if (textToParse.isNotEmpty()) {
                parse(textToParse).forEach { memberStr ->
                    members.add(readStringToModel(memberStr))
                }
                Day13Model(members)
            } else Day13Model()
        } else {
            Day13Model(str.toInt())
        }
    }

    fun part1(input: List<String>): Int {
        val allInput = input.joinToString(separator = "\n")
        var sumOfRightOrderIdx = 0
        allInput.split("\n\n").forEachIndexed { i, pair ->
            val (left, right) = pair.split("\n").map { readStringToModel(it) }
            if (left <= right) sumOfRightOrderIdx += i + 1
        }
        return(sumOfRightOrderIdx)
    }

    fun part2(input: List<String>): Int {
        val allInput = input.joinToString(separator = "\n")
        val firstDivider = readStringToModel("[[2]]")
        val secondDivider = readStringToModel("[[6]]")
        val packets = mutableListOf(firstDivider, secondDivider)
        allInput.split("\n\n").forEach { pair ->
            val (left, right) = pair.split("\n").map { readStringToModel(it) }
            packets.add(left)
            packets.add(right)
        }
        packets.sort()
        val x = packets.indexOf(firstDivider) + 1
        val y = packets.indexOf(secondDivider) + 1
        return(x*y)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

class Day13Model : Comparable<Day13Model> {
    private var members = mutableListOf<Day13Model>()
    private var value: Int? = null

    constructor()

    constructor(members: MutableList<Day13Model>) {
        this.members = members
    }

    constructor(value: Int) {
        this.value = value
    }

    override fun compareTo(other: Day13Model): Int {
        if (value == null && members.isEmpty() && other.value == null && other.members.isEmpty()) return 0
        else if (value == null && members.isEmpty()) return -1
        else if (other.value == null && other.members.isEmpty()) return 1
        else if (value != null && other.value != null) return value!!.compareTo(other.value!!)
        else {
            // compare first item
            if (members.size == 0) {
                members = mutableListOf(Day13Model(value!!))
                value = null
            }
            if (other.members.size == 0) {
                other.members = mutableListOf(Day13Model(other.value!!))
                other.value = null
            }
            val left = members[0]
            val right = other.members[0]
            val firstCompare = left.compareTo(right)
            return if (firstCompare == 0) {
                // compare the rest
                Day13Model(members.drop(1).toMutableList()).compareTo(Day13Model(other.members.drop(1).toMutableList()))
            } else firstCompare
        }
    }
}
