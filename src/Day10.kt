fun main() {

    fun printToCrt(crt: String, cycle: Int, x: Int): String {
        var cycleCount = (cycle % 40)
        if (cycleCount == 0) cycleCount = 40
        val output = if ((cycleCount - x) in listOf(0, 1, 2)) "#" else "."
        return crt + output
    }

    fun _process(input: List<String>): Pair<Int, String> {
        var cycle = 1
        var x = 1
        var carry: Int? = null
        var signalStrength = 0
        var cmds = input
        var crt = ""
        run breakWhile@{
            while (true) {
                if ((cycle - 20) % 40 == 0 && cycle <= 220) {
                    signalStrength += x * cycle
                }
                if ((cycle - 1) % 40 == 0) crt += "\n"
                crt = printToCrt(crt, cycle, x)
                if (carry != null) {
                    x += carry!!
                    carry = null
                } else {
                    if (cmds.isEmpty()) return@breakWhile
                    val line = cmds.take(1).first()
                    cmds = cmds.drop(1)

                    if (line.substring(0, 4) == "addx") {
                        val regex = "(.+) (-?\\d*)".toRegex()
                        val (_, valueStr) = regex.find(line)!!.destructured.toList()
                        carry = valueStr.toInt()
                    }
                }
                cycle++
            }
        }
        return Pair(signalStrength, crt)
    }

    fun part1(input: List<String>): Int {
        return _process(input).first
    }

    fun part2(input: List<String>): String {
        return _process(input).second
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
