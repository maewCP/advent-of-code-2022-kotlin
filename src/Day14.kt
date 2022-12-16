fun main() {

    fun prepareInput(input: List<String>) : Pair<MutableMap<Pair<Int, Int>, Char>, Int> {
        val map = mutableMapOf<Pair<Int, Int>, Char>()
        var maxDepth = 0
        map.clear()
        input.forEach { line ->
            val points = line.split(" -> ").map { point ->
                val (x, y) = point.split(",").map { it.toInt() }
                maxDepth = maxDepth.coerceAtLeast(y)
                Pair(x, y)
            }.toMutableList()
            (1 until points.size).forEach { i ->
                if (points[i].first == points[i - 1].first) {
                    val range = if (points[i].second < points[i - 1].second) (points[i].second..points[i - 1].second) else (points[i].second downTo points[i - 1].second)
                    range.forEach { y -> map[Pair(points[i].first, y)] = '#' }
                } else {
                    val range = if (points[i].first < points[i - 1].first) (points[i].first..points[i - 1].first) else (points[i].first downTo points[i - 1].first)
                    range.forEach { x -> map[Pair(x, points[i].second)] = '#' }
                }
            }
        }
        return Pair(map, maxDepth)
    }

    fun part1(input: List<String>): Int {
        val (map, maxDepth) = prepareInput(input)

        var x = 500
        var y = 0
        var sandCount = 0
        while (y < maxDepth) {
            if (map[Pair(x, y + 1)] == null) {
                y++
            } else {
                if (map[Pair(x - 1, y + 1)] == null) {
                    x--
                    y++
                } else if (map[Pair(x + 1, y + 1)] == null) {
                    x++
                    y++
                } else {
                    map[Pair(x, y)] = 'O'
                    sandCount++
                    x = 500
                    y = 0
                }
            }
        }
        return sandCount
    }

    fun part2(input: List<String>): Int {
        val (map, maxDepth) = prepareInput(input)

        var x = 500
        var y = 0
        var sandCount = 0
        var rested = false
        while (!rested) {
            if (y == maxDepth + 1) {
                map[Pair(x, y)] = 'O'
                sandCount++
                x = 500
                y = 0
            }

            if (map[Pair(x, y + 1)] == null) {
                y++
            } else {
                if (map[Pair(x - 1, y + 1)] == null) {
                    x--
                    y++
                } else if (map[Pair(x + 1, y + 1)] == null) {
                    x++
                    y++
                } else {
                    map[Pair(x, y)] = 'O'
                    sandCount++
                    if (x == 500 && y == 0) rested = true
                    x = 500
                    y = 0
                }
            }
        }
        return sandCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
