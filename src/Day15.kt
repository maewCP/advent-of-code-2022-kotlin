import kotlin.math.abs

fun main() {
    val testingPoints = mutableListOf<Pair<Int, Int>>()
    val data = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    val distances = mutableMapOf<Pair<Int, Int>, Int>()

    fun getDistance(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
        return abs(a.first - b.first) + abs(a.second - b.second)
    }

    fun findTestingPoints(a: Pair<Int, Int>, b: Pair<Int, Int>, xFrom: Int, xTo: Int, yFrom: Int, yTo: Int) {
        val xAddition = if (a.first < b.first) 1 else -1
        val yAddition = if (a.second < b.second) 1 else -1
        var x = a.first
        var y = a.second
        while (x != b.first && y != b.second) {
            if (x in (xFrom..xTo) && y in (yFrom..yTo)) {
                testingPoints.add(Pair(x, y))
            }
            x += xAddition
            y += yAddition
        }
    }

    fun getXRange(sensor: Pair<Int, Int>, distance: Int, y: Int): IntRange? {
        val ray = distance * if (sensor.second > y) -1 else 1
        if (abs(ray) < abs(sensor.second - y)) return null
        val diffY = abs(sensor.second + ray - y)
        return (sensor.first - diffY..sensor.first + diffY)
    }

    fun prepareInput(input: List<String>) {
        val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
        data.clear()
        distances.clear()
        input.forEach { line ->
            val groups = regex.find(line)!!.destructured.toList().map { it.toInt() }
            val sensor = Pair(groups[0], groups[1])
            val beacon = Pair(groups[2], groups[3])
            data[sensor] = beacon
        }
        // find distance
        data.forEach { (sensor, beacon) ->
            distances[sensor] = getDistance(sensor, beacon)
        }
    }

    fun part1(input: List<String>, y: Int): Int {
        prepareInput(input)
        val xs = mutableSetOf<Int>()
        data.forEach { (sensor, _) ->
            val xRange = getXRange(sensor, distances[sensor]!!, y)
            if (xRange != null) xs.addAll(xRange.toSet())
        }
        listOf(data.keys, data.values).map { points -> xs.removeAll(points.filter { it.second == y }.map { it.first }.toSet()) }
        return xs.size
    }

    fun part2(input: List<String>, xFrom: Int, xTo: Int, yFrom: Int, yTo: Int): Long {
        prepareInput(input)
        data.forEach { (sensor, _) ->
            val left = Pair(sensor.first - distances[sensor]!! - 1, sensor.second)
            val top = Pair(sensor.first, sensor.second - distances[sensor]!! - 1)
            val right = Pair(sensor.first + distances[sensor]!! + 1, sensor.second)
            val bottom = Pair(sensor.first, sensor.second + distances[sensor]!! + 1)

            findTestingPoints(left, top, xFrom, xTo, yFrom, yTo)
            findTestingPoints(top, right, xFrom, xTo, yFrom, yTo)
            findTestingPoints(right, bottom, xFrom, xTo, yFrom, yTo)
            findTestingPoints(bottom, left, xFrom, xTo, yFrom, yTo)

            testingPoints.forEach { point ->
                var detected = false
                run breakOSensor@{
                    distances.forEach { (oSensor, distance) ->
                        if (getDistance(oSensor, point) <= distance) {
                            detected = true
                            return@breakOSensor
                        }
                    }
                }
                if (!detected && point !in data.keys && point !in data.values) {
                    return(point.first * 4000000L + point.second)
                }
            }
            testingPoints.clear()
        }
        //
//    data.forEach { (sensor, beacon) ->
//        if (sensor.second == y) xRanges.remove(sensor.first)
//        if (beacon.second == y) xRanges.remove(beacon.first)
//    }
//    println(xRanges.sorted())
//    println(xRanges.size)
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 0, 20, 0, 20) == 56000011L)

    val input = readInput("Day15")
    part1(input, 2000000).println()
    part2(input, 0, 4000000, 0, 4000000).println()
}
