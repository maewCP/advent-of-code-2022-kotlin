fun main() {

    fun adjacent(coordinate: Triple<Int, Int, Int>): List<Triple<Int, Int, Int>> {
        val (x, y, z) = coordinate.toList()
        return listOf(
            Triple(x - 1, y, z),
            Triple(x + 1, y, z),
            Triple(x, y - 1, z),
            Triple(x, y + 1, z),
            Triple(x, y, z - 1),
            Triple(x, y, z + 1)
        )
    }

    fun prepareInput(input: List<String>): MutableSet<Triple<Int, Int, Int>> {
        val droplets = mutableSetOf<Triple<Int, Int, Int>>()
        input.forEach { line ->
            val (x, y, z) = line.split(",").map { it.toInt() }
            droplets.add(Triple(x, y, z))
        }
        return droplets
    }

    fun part1(input: List<String>): Int {
        val droplets = prepareInput(input)
        return droplets.sumOf { droplet ->
            adjacent(droplet).sumOf { adjacentDroplet -> (if (droplets.contains(adjacentDroplet)) 0 else 1) as Int }
        }
    }

    fun part2(input: List<String>): Int {
        val droplets = prepareInput(input)
        val xRange = (droplets.minOf { it.first } - 1..droplets.maxOf { it.first } + 1)
        val yRange = (droplets.minOf { it.second } - 1..droplets.maxOf { it.second } + 1)
        val zRange = (droplets.minOf { it.third } - 1..droplets.maxOf { it.third } + 1)
        val waterLeaked = mutableSetOf<Triple<Int, Int, Int>>()
        val checkQueue = mutableListOf(Triple(xRange.toList().first(), yRange.toList().first(), zRange.toList().first()))
        var leakedSurfaceCount = 0

        while (checkQueue.size > 0) {
            val checkNext = checkQueue.removeAt(0)
            waterLeaked.add(checkNext)
            adjacent(checkNext).forEach { adjacent ->
                if (droplets.contains(adjacent)) {
                    leakedSurfaceCount++
                } else if (!waterLeaked.contains(adjacent) &&
                    !checkQueue.contains(adjacent) &&
                    adjacent.first in xRange &&
                    adjacent.second in yRange &&
                    adjacent.third in zRange
                ) {
                    checkQueue.add(adjacent)
                }
            }
        }
        return leakedSurfaceCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
