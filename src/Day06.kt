fun main() {

    fun _process(input: List<String>, distinctCount: Int): Int {
        val l = input.first().toList()
        l.forEachIndexed { i, c ->
            val mem = l.subList(i, i + distinctCount)
            if (mem.distinct().size == distinctCount) {
                return(i + distinctCount)
            }
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        return _process(input, 4)
    }

    fun part2(input: List<String>): Int {
        return _process(input, 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
