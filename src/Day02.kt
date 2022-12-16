fun main() {

    fun calcScore(a: Int, b: Int): Int {
        // draw
        if (a == b) return 3 + b
        // win
        if ((a == 1 && b == 2) || (a == 2 && b == 3) || (a == 3 && b == 1)) return 6 + b
        // lose
        return b
    }

    fun followCmd(a: Int, command: String): Int {
        return when (command) {
            "WIN" -> when (a) {
                1 -> 2
                2 -> 3
                3 -> 1
                else -> throw RuntimeException()
            }

            "DRAW" -> a
            "LOSE" -> when (a) {
                1 -> 3
                2 -> 1
                3 -> 2
                else -> throw RuntimeException()
            }

            else -> throw RuntimeException()
        }
    }

    fun part1(input: List<String>): Int {
        var score = 0
        input.forEach { match ->
            val op = when (match[0]) {
                'A' -> 1
                'B' -> 2
                'C' -> 3
                else -> throw RuntimeException("Wrong input")
            }
            val me = when (match[2]) {
                'X' -> 1
                'Y' -> 2
                'Z' -> 3
                else -> throw RuntimeException("Wrong input")
            }
            score += calcScore(op, me)
        }
        return score
    }

    fun part2(input: List<String>): Int {
        var score = 0
        input.forEach { match ->
            val op = when (match[0]) {
                'A' -> 1
                'B' -> 2
                'C' -> 3
                else -> throw RuntimeException("Wrong input")
            }
            val cmd = when (match[2]) {
                'X' -> "LOSE"
                'Y' -> "DRAW"
                'Z' -> "WIN"
                else -> throw RuntimeException("Wrong input")
            }
            val me2 = followCmd(op, cmd)
            score += calcScore(op, me2)
        }
        return score
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
