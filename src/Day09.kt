import kotlin.math.abs

fun main() {

    fun _process(input: List<String>, noOfKnots: Int): Int {
        val visited = mutableSetOf<Pair<Int, Int>>()
        visited.add(Pair(0, 0))
        val knots = mutableListOf<Pair<Int, Int>>()
        (0..noOfKnots).forEach { _ -> knots.add(Pair(0, 0)) }
        input.forEachIndexed { cmdIdx, line ->
            val regex = "(.) (\\d+)".toRegex()
            val (cmd, value) = regex.find(line)!!.destructured.toList()
            (1..value.toInt()).forEach { step ->
                when (cmd) {
                    "U" -> knots[0] = Pair(knots[0].first + 1, knots[0].second)
                    "D" -> knots[0] = Pair(knots[0].first - 1, knots[0].second)
                    "L" -> knots[0] = Pair(knots[0].first, knots[0].second - 1)
                    "R" -> knots[0] = Pair(knots[0].first, knots[0].second + 1)
                }

                (1..noOfKnots).forEach { i ->
                    var r = knots[i].first
                    var c = knots[i].second
                    if ((abs(knots[i - 1].first - knots[i].first) > 1 || abs(knots[i - 1].second - knots[i].second) > 1) &&
                        (knots[i - 1].first != knots[i].first && knots[i - 1].second != knots[i].second)
                    ) {
                        r += (knots[i - 1].first - knots[i].first) / abs(knots[i - 1].first - knots[i].first)
                        c += (knots[i - 1].second - knots[i].second) / abs(knots[i - 1].second - knots[i].second)
                    } else if (abs(knots[i - 1].first - knots[i].first) > 1) {
                        r += (knots[i - 1].first - knots[i].first) / abs(knots[i - 1].first - knots[i].first)
                    } else if (abs(knots[i - 1].second - knots[i].second) > 1) {
                        c += (knots[i - 1].second - knots[i].second) / abs(knots[i - 1].second - knots[i].second)
                    }
                    knots[i] = Pair(r, c)
                }
                visited.add(knots[noOfKnots])
            }
        }
        return visited.size
    }

    fun part1(input: List<String>): Int {
        return _process(input, 1)
    }

    fun part2(input: List<String>): Int {
        return _process(input, 9)
    }

    // test if implementation meets criteria from the description, like:
    val testInputPart1 = readInput("Day09_part1_test")
    val testInputPart2 = readInput("Day09_part2_test")
    check(part1(testInputPart1) == 13)
    check(part2(testInputPart2) == 36)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
