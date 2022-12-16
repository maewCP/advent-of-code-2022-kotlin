fun main() {

    fun getPriority(itemType: Char): Int {
        return if (itemType >= 'a') itemType - 'a' + 1 else itemType - 'A' + 27
    }

    fun part1(input: List<String>): Int {
        var priorities = 0
        input.forEach { line ->
            val first = line.substring(0, line.length / 2)
            val second = line.substring(line.length / 2, line.length)
            val itemType = first.toSet().intersect(second.toSet()).first()
            priorities += getPriority(itemType)
        }
        return priorities
    }

    fun part2(input: List<String>): Int {
        var priorities = 0
        val input2 = input.toMutableList()
        while (input2.size > 0) {
            val first = input2.removeAt(0)
            val second = input2.removeAt(0)
            val third = input2.removeAt(0)
            val itemType = first.toSet().intersect(second.toSet()).intersect(third.toSet()).first()
            priorities += getPriority(itemType)
        }
        return priorities
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
