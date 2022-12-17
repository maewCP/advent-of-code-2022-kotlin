import java.util.*

fun main() {

    val verbose = false

    val rockTypes = listOf(
        """..@@@@.""",
        """...@...
          |..@@@..
          |...@...
        """.trimMargin(),
        """....@..
          |....@..
          |..@@@..
        """.trimMargin(),
        """..@....
          |..@....
          |..@....
          |..@....
        """.trimMargin(),
        """..@@...
          |..@@...
        """.trimMargin()
    )

    var tunnel = TreeMap<Long, String>()

    fun getRockHeight(): Long {
        var height = 0L
        tunnel.forEach { (h, line) ->
            if (line.contains("#")) height = h + 1L
        }
        return height
    }

    fun addRock(type: Int) {
        var currRockHeight = getRockHeight()
        (currRockHeight until currRockHeight + 3).forEach { h ->
            tunnel[h] = "......."
        }
        currRockHeight += 3
        val rockLines = rockTypes[type].split("\n")
        rockLines.asReversed().forEachIndexed { i, line ->
            tunnel[currRockHeight + i] = line
        }
    }

    fun mergeLine(line1: String, line2: String): String {
        var merged = ""
        line1.toList().forEachIndexed { j, char ->
            merged += if (char == '.' && line2[j] == '@') '@' else char
        }
        return merged
    }

    fun rockCanNotFall(): Boolean {
        var prevLine = "#######"
        tunnel.forEach { (_, currLine) ->
            currLine.toList().forEachIndexed { j, char ->
                if (char == '@' && prevLine[j] == '#') return true
            }
            prevLine = currLine
        }
        return false
    }

    fun allRockSettled(): Boolean {
        return !(tunnel.any { (_, line) -> line.contains("@") })
    }

    fun reduceTunnelSize() {
        val newTunnel = TreeMap<Long, String>()
        val heights = tunnel.keys.toMutableList()
        var prevSim = "*******"
        var starCount = 0
        var prevStarCount = -1
        run breakForEach@{
            heights.asReversed().forEach { h ->
                newTunnel[h] = tunnel[h]!!
                if (starCount == prevStarCount) return@breakForEach // when no star increases, no room for rock entering
                prevStarCount = starCount
                var currSim = tunnel[h]!!
                // stars fall from above
                (0..6).forEach { j ->
                    if (currSim[j] == '.' && prevSim[j] == '*') {
                        val chars = currSim.toCharArray()
                        chars[j] = '*'
                        currSim = String(chars)
                        starCount++
                    }
                }
                // stars expand horizontal
                var beforeExpand = -1
                while (starCount != beforeExpand) {
                    beforeExpand = starCount
                    (0..6).forEach { j ->
                        if (currSim[j] == '.' && ((j != 0 && currSim[j - 1] == '*') || (j != 6 && currSim[j + 1] == '*'))) {
                            val chars = currSim.toCharArray()
                            chars[j] = '*'
                            currSim = String(chars)
                            starCount++
                        }
                    }
                }
                prevSim = currSim
            }
        }
        tunnel = newTunnel
        return
    }

    fun fall() {
        if (rockCanNotFall()) {
            tunnel.forEach { (h, line) ->
                tunnel[h] = line.replace("@", "#")
            }
        } else {
            var prevLine = "#######"
            var i = 0
            tunnel.forEach { (h, currLine) ->
                var currLineAfterFalling = currLine
                if (currLine.contains("@")) {
                    if (i > 0) tunnel[h - 1] = mergeLine(prevLine, currLine)
                    currLineAfterFalling = currLine.replace("@", ".")
                }
                prevLine = currLineAfterFalling
                tunnel[h] = currLineAfterFalling
                i++
            }
            tunnel[tunnel.keys.max()] = "......."
        }
    }

    fun runCmd(cmd: Char) {
        var canRunCmd = true
        run breakForEach@{
            tunnel.forEach { (_, line) ->
                val edgeRockCol = if (cmd == '<') line.toList().indexOf('@') else line.toList().lastIndexOf('@')
                if (edgeRockCol >= 0) {
                    if (cmd == '<' && (edgeRockCol == 0 || (line[edgeRockCol - 1] == '#'))) {
                        canRunCmd = false
                        return@breakForEach
                    }
                    if (cmd == '>' && (edgeRockCol == 6 || (line[edgeRockCol + 1] == '#'))) {
                        canRunCmd = false
                        return@breakForEach
                    }
                }
            }
        }
        if (canRunCmd) {
            tunnel.forEach { (h, line) ->
                var lineAfterShifting = line
                val toDot = if (cmd == '<') line.toList().lastIndexOf('@') else line.toList().indexOf('@')
                val toAt = if (cmd == '<') line.toList().indexOf('@') - 1 else line.toList().lastIndexOf('@') + 1
                if (line.contains('@')) {
                    lineAfterShifting = lineAfterShifting.substring(0, toDot) + '.' + lineAfterShifting.substring(toDot + 1, line.length)
                    lineAfterShifting = lineAfterShifting.substring(0, toAt) + '@' + lineAfterShifting.substring(toAt + 1, line.length)
                    tunnel[h] = lineAfterShifting
                }
            }
        }
    }

    fun printTunnel(last: Long = 50000L) {
        val from = (tunnel.keys.max() - last).coerceAtLeast(tunnel.keys.min())
        (from..tunnel.keys.max()).forEach { h ->
            println("${tunnel[h]} ${h + 1}")
        }
        println("")
    }

    fun _process(input: List<String>, noOfItemDrop: Long): Long {
        val patternMap = mutableMapOf<String, MutableMap<Pair<Int, Int>, Pair<Long, Long>>>()
        var patternFound = false
        tunnel.clear()
        var itemRunning = 0L
        var cmdRunning = 0L
        val cmds = input[0].toList()
        var cmdIdx: Int
        var itemIdx: Int
        while (itemRunning < noOfItemDrop) {
            if (verbose) println("itemRunning: $itemRunning")
            // Pattern ----------------------------------
            itemIdx = (itemRunning % 5L).toInt()
            cmdIdx = (cmdRunning % cmds.size).toInt()
            reduceTunnelSize()
            if (!patternFound) {
                val pattern = tunnel.values.joinToString(separator = "\n")
                patternMap[pattern] = if (patternMap[pattern] != null) patternMap[pattern]!! else mutableMapOf()
                if (patternMap[pattern] != null) {
                    // Pattern seek
                    val foundAt = patternMap[pattern]!![Pair(cmdIdx, itemIdx)]
                    if (foundAt != null) {
                        if (verbose) println("Pattern found")
                        patternFound = true
                        val foundPatternAtHeight = foundAt.first
                        val foundPatternAtItemRunning = foundAt.second
                        val patternHeight = getRockHeight() - foundPatternAtHeight
                        val itemRun = itemRunning - foundPatternAtItemRunning
                        val noOfItemRunMultiple = ((noOfItemDrop - 1) - itemRunning) / itemRun
                        itemRunning += noOfItemRunMultiple * itemRun
                        val newTunnel = TreeMap<Long, String>()
                        tunnel.forEach { (h, _) ->
                            newTunnel[h + patternHeight * noOfItemRunMultiple] = tunnel[h]!!
                        }
                        tunnel = newTunnel
                    } else {
                        // Pattern save
                        patternMap[pattern]!![Pair(cmdIdx, itemIdx)] = Pair(getRockHeight(), itemRunning)
                    }
                }
            }

            addRock(itemIdx)
            if (verbose) printTunnel()
            while (!allRockSettled()) {
                val cmd = cmds[(cmdRunning++ % cmds.size).toInt()]
                runCmd(cmd)
                fall()
            }
            itemRunning++
        }
        if (verbose) printTunnel()
        if (verbose) println(getRockHeight())
        return getRockHeight()
    }

    fun part1(input: List<String>): Long {
        return _process(input, 2022L)
    }

    fun part2(input: List<String>): Long {
        return _process(input, 1000000000000L)
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 3068L)
    check(part2(testInput) == 1514285714288L)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
