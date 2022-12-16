fun main() {

    fun _process(inputStack: List<String>, inputCmd: List<String>, reverse: Boolean) : String {
        val stacks = mutableListOf<MutableList<Char>>()
        inputStack.forEachIndexed { lineIdx, line ->
            (0 until (line.length + 1) / 4).forEach { i ->
                if (lineIdx == 0) {
                    // initial list
                    stacks.add(mutableListOf())
                }
                val item = line[4 * i + 1]
                if (item != ' ') stacks[i].add(item)
            }
        }

        inputCmd.forEach { line ->
            val match = "move (\\d+) from (\\d+) to (\\d+)".toRegex().find(line)!!
            val (qty, from, to) = match.destructured.toList().map { it.toInt() }
            val pop = stacks[from - 1].subList(0, qty).toMutableList()
            if (reverse) pop.reverse()
            stacks[from - 1] = stacks[from - 1].subList(qty, stacks[from - 1].size).toMutableList()
            pop.addAll(stacks[to - 1])
            stacks[to - 1] = pop
        }
        return stacks.map { it[0] }.joinToString(separator = "")
    }

    fun part1(inputStack: List<String>, inputCmd: List<String>): String {
        return _process(inputStack, inputCmd, true)
    }

    fun part2(inputStack: List<String>, inputCmd: List<String>): String {
        return _process(inputStack, inputCmd, false)
    }

    // test if implementation meets criteria from the description, like:
    val testInputStack = readInput("Day05_stack_test")
    val testInputCmd = readInput("Day05_cmd_test")
    check(part1(testInputStack, testInputCmd) == "CMZ")
    check(part2(testInputStack, testInputCmd) == "MCD")

    val inputStack = readInput("Day05_stack")
    val inputCmd = readInput("Day05_cmd")
    part1(inputStack, inputCmd).println()
    part2(inputStack, inputCmd).println()
}
