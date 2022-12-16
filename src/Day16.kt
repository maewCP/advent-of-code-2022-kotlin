import kotlin.random.Random

fun main() {
    val verbose = false
    val nodes = mutableMapOf<String, Day16Node>()
    var usableNodes = mutableListOf<Day16Node>()
    val map = mutableMapOf<Day16Node, List<Day16Node>>()
    val distances = mutableMapOf<Set<Day16Node>, Int>()

    val shortestPath = mutableMapOf<Day16Node, Int>()
    val queue = mutableListOf<Day16ShortestPath>()
    lateinit var goal: Day16Node

   fun printSteps1(valveSteps: MutableList<Day16Node>) {
        val prev = nodes["AA"]!!
        print("${prev.name}(${prev.rate})")
        (0 until valveSteps.size).forEach { i ->
            print(" -> ${distances[setOf(prev, valveSteps[i])]} -> ")
            print("${valveSteps[i].name}(${valveSteps[i].rate})")
        }
        println("")
    }

    fun printSteps2(valveSteps: MutableList<Day16Node>) {
        printSteps1(valveSteps.subList(0, valveSteps.size / 2))
        printSteps1(valveSteps.subList(valveSteps.size / 2, valveSteps.size))
    }

    fun calcScore1(valveSteps: MutableList<Day16Node>, minutes: Int): Int {
        var endOfMin = 0
        var score = 0
        var rate = 0
        var curr = nodes["AA"]
        run breakForEach@{
            valveSteps.forEach { node ->
                var minPassed = distances[setOf(curr, node)]!! + 1
                if (endOfMin + minPassed > minutes) minPassed = minutes - endOfMin
                score += minPassed * rate
                rate += node.rate
                endOfMin += minPassed
                curr = node
                if (endOfMin >= minutes) return@breakForEach
            }
        }
        if (endOfMin < minutes) {
            score += (minutes - endOfMin) * rate
        }
        return score
    }

    fun calcScore2(valveSteps: MutableList<Day16Node>, minutes: Int): Int {
        return calcScore1(valveSteps.subList(0, valveSteps.size / 2), minutes) + calcScore1(valveSteps.subList(valveSteps.size / 2, valveSteps.size), minutes)
    }

    fun tryNext(shortestPathSolution: Day16ShortestPath, next: Day16Node) {
        if (next in shortestPathSolution.path) return
        val newPath = shortestPathSolution.path.toMutableList()
        newPath.add(next)
        queue.add(Day16ShortestPath(newPath))
    }

    fun search() {
        val solution = queue.removeAt(0)
        val curr = solution.path[solution.path.size - 1]
        val bSolution = shortestPath[curr]
        if (bSolution == null) shortestPath[curr] = solution.path.size
        else if (solution.path.size < shortestPath[curr]!!) shortestPath[curr] = solution.path.size
        else return

        if (curr == goal) return

        map[curr]!!.forEach { next ->
            tryNext(solution, next)
        }
    }

    fun prepareInput(input: List<String>) {
        nodes.clear()
        usableNodes.clear()
        map.clear()
        distances.clear()
        val regex = "Valve (.+) has flow rate=(\\d+); tunnels? leads? to valves? (.+)".toRegex()
        input.forEach { line ->
            val groups = regex.find(line)!!.destructured.toList()
            nodes[groups[0]] = Day16Node(groups[0], groups[1].toInt())
        }
        input.forEach { line ->
            val groups = regex.find(line)!!.destructured.toList()
            val node = nodes[groups[0]]!!
            val connections = groups[2].split(", ").map { nodes[it]!! }
            map[node] = connections
        }
        if (verbose) println("Node size: ${nodes.size}")
        usableNodes = nodes.values.filter { node -> node.rate > 0 || node.name == "AA" }.toMutableList()
        usableNodes.forEach { node ->
            usableNodes.filter { it != node }.forEach { targetNode ->
                shortestPath.clear()
                queue.clear()
                queue.add(Day16ShortestPath(mutableListOf(node)))
                goal = targetNode
                do {
                    search()
                } while (queue.size > 0)
                distances[setOf(node, targetNode)] = shortestPath[targetNode]!! - 1
            }
        }
        usableNodes.remove(nodes["AA"])
        if (verbose) println("Usable Node Size: ${usableNodes.size}")
    }

    fun part1(input: List<String>): Int {
        prepareInput(input)
        usableNodes.shuffled()
        var global = usableNodes.toMutableList()
        (1..30).forEach { i ->
            var local = usableNodes.shuffled().toMutableList()
            var noBetter = 0
            while (noBetter <= 100000) {
                val trySteps = local.toMutableList()
                val nodeIdx = Random.nextInt(0, usableNodes.size - 1)
                val insertIntoIdx = Random.nextInt(0, usableNodes.size - 2)
                val node = trySteps.removeAt(nodeIdx)
                trySteps.add(insertIntoIdx, node)
                if (calcScore1(local, 30) < calcScore1(trySteps, 30)) local = trySteps
                else noBetter++
            }
            if (verbose) println("***************************")
            if (verbose) printSteps1(local)
            if (verbose) println("Local score $i = ${calcScore1(local, 30)}")
            if (calcScore1(local, 30) > calcScore1(global, 30)) global = local
        }
        if (verbose) println("***************************")
        if (verbose) printSteps1(global)
        if (verbose) println("Global score: ${calcScore1(global, 30)}")
        return calcScore1(global, 30)
    }

    fun part2(input: List<String>): Int {
        prepareInput(input)
        usableNodes.shuffled()
        var global = usableNodes.toMutableList()
        (1..30).forEach { i ->
            var local = usableNodes.shuffled().toMutableList()
            var noBetter = 0
            while (noBetter <= 1000000) {
                val trySteps = local.toMutableList()
                val nodeIdx = Random.nextInt(0, usableNodes.size - 1)
                val insertIntoIdx = Random.nextInt(0, usableNodes.size - 2)
                val node = trySteps.removeAt(nodeIdx)
                trySteps.add(insertIntoIdx, node)
                if (calcScore2(local, 26) < calcScore2(trySteps, 26)) local = trySteps
                else noBetter++
            }
            if (verbose) println("***************************")
            if (verbose) printSteps2(local)
            if (verbose) println("Local score $i = ${calcScore2(local, 26)}")
            if (calcScore2(local, 26) > calcScore2(global, 26)) global = local
        }
        if (verbose) println("***************************")
        if (verbose) printSteps2(global)
        if (verbose) println("Global score: ${calcScore2(global, 26)}")
        return calcScore2(global, 26)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

data class Day16Node(val name: String, val rate: Int)
data class Day16ShortestPath(val path: MutableList<Day16Node>)
