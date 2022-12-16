fun main() {
    val regex = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex()

    fun part1(input: List<String>): Int {
        var containCount = 0
        input.forEach { line ->
            val g = regex.find(line)!!.destructured.toList().map { it.toInt() }
            if ((g[0] <= g[2] && g[1] >= g[3]) || (g[2] <= g[0] && g[3] >= g[1])) containCount++
        }
        return containCount
    }

    fun part2(input: List<String>): Int {
        var overlapCount = 0
        input.forEach { line ->
            val g = regex.find(line)!!.destructured.toList().map { it.toInt() }
            if (!(g[2] > g[1] || g[0] > g[3])) overlapCount++
        }
        return overlapCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
