fun main() {
    val verbose = false
    val shortestPath = mutableMapOf<Pair<Int, Int>, Day12Solution>()
    val queue = mutableListOf<Day12Solution>()
    val map = mutableMapOf<Pair<Int, Int>, Char>()
    val aPositions = mutableListOf<Pair<Int, Int>>()
    lateinit var start: Pair<Int, Int>
    lateinit var goal: Pair<Int, Int>
    var maxR = 0
    var maxC = 0

    fun printSolution(solution: Day12Solution) {
        (0..maxR).forEach { r ->
            (0..maxC).forEach { c ->
                val curr = Pair(r, c)
                if (curr !in solution.path) print(".")
                else {
                    val currStep = solution.path.indexOf(curr)
                    if (currStep == solution.path.size - 1) print("E")
                    else {
                        val next = solution.path[currStep + 1]
                        if (curr.first < next.first) print("v")
                        else if (curr.first > next.first) print("^")
                        else if (curr.second < next.second) print(">")
                        else print("<")
                    }
                }
            }
            println("")
        }
    }

    fun printMap() {
        (0..maxR).forEach { r ->
            (0..maxC).forEach { c ->
                print(map[Pair(r, c)])
            }
            println("")
        }
    }

    fun tryNext(solution: Day12Solution, relativeR: Int, relativeC: Int) {
        val curr = solution.path[solution.path.size - 1]
        if (curr.first + relativeR < 0 || curr.first + relativeR > maxR) return
        if (curr.second + relativeC < 0 || curr.second + relativeC > maxC) return
        val next = Pair(curr.first + relativeR, curr.second + relativeC)
        if (next in solution.path) return
        if (map[next]!! - map[curr]!! > 1) return
        val newPath = solution.path.toMutableList()
        newPath.add(next)
        queue.add(Day12Solution(newPath))
    }

    fun search() {
        val solution = queue.removeAt(0)
        val curr = solution.path[solution.path.size - 1]
        val bSolution = shortestPath[curr]
        if (bSolution == null) shortestPath[curr] = solution
        else if (solution.path.size < shortestPath[curr]!!.path.size) shortestPath[curr] = solution
        else return

        if (curr == goal) return
        tryNext(solution, -1, 0)
        tryNext(solution, 1, 0)
        tryNext(solution, 0, 1)
        tryNext(solution, 0, -1)
    }

    fun prepareInput(input: List<String>) {
        aPositions.clear()
        input.forEachIndexed { r, line ->
            line.toList().forEachIndexed { c, h ->
                if (h == 'S') {
                    start = Pair(r, c)
                } else if (h == 'E') {
                    goal = Pair(r, c)
                }
                if (h == 'a' || h == 'S') aPositions.add(Pair(r, c))
                map[Pair(r, c)] = if (h == 'S') 'a' else if (h == 'E') 'z' else h
                maxC = maxC.coerceAtLeast(c)
            }
            maxR = r
        }
    }

    fun _process(startPoints: List<Pair<Int, Int>>) : Int {
        var globalSolution: Day12Solution? = null
        startPoints.forEach { startPoint: Pair<Int, Int> ->
            shortestPath.clear()
            queue.add(Day12Solution(mutableListOf(startPoint)))
            do {
                search()
            } while (queue.size > 0)
            if (shortestPath[goal] == null) return@forEach
            if (globalSolution == null || globalSolution!!.path.size > shortestPath[goal]!!.path.size) {
                globalSolution = shortestPath[goal]!!
            }
        }

        val solution = globalSolution!!
        if (verbose) println(solution.path)
        if (verbose) printMap()
        if (verbose) println("")
        if (verbose) printSolution(solution)
        if (verbose) println("")
        if (verbose) println(solution.path.size - 1)
        return solution.path.size - 1
    }

    fun part1(input: List<String>): Int {
        prepareInput(input)
        return _process(listOf(start))
    }

    fun part2(input: List<String>): Int {
        prepareInput(input)
        return _process(aPositions)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

data class Day12Solution(val path: MutableList<Pair<Int, Int>>)
