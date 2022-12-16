import java.math.BigInteger

fun main() {

    fun readMonkeys(strs: List<String>): MutableList<Day11Monkey> {
        val monkeys = mutableListOf<Day11Monkey>()
        val regex = "Monkey (\\d+):\\n.*Starting items: (.*)\\n.*Operation: new = old (.*)\\n.*Test: divisible by (\\d+)\\n.*If true: throw to monkey (\\d+)\\n.*If false: throw to monkey (\\d+)".toRegex()
        strs.forEach { str ->
            val groups = regex.find(str)!!.destructured.toList()
            val (operation, value) = groups[2].split(" ")
            monkeys.add(
                Day11Monkey(
                    no = groups[0].toInt(),
                    items = groups[1].split(", ").map { Day11Item(it.toBigInteger(), it.toBigInteger()) }.toMutableList(),
                    operation = operation,
                    value = if (value == "old") -1 else value.toInt(),
                    divisibleBy = groups[3].toInt(),
                    whenTrue = groups[4].toInt(),
                    whenFalse = groups[5].toInt()
                )
            )
        }
        return monkeys
    }

    fun _process(input: List<String>, divideWorry: Boolean, noOfRound: Int): List<Day11Monkey> {
        val allInput = input.joinToString(separator = "\n")
        var monkeys = readMonkeys(allInput.split("\n\n"))
        var superDiv = 1
        monkeys.forEach { monkey ->
            superDiv *= monkey.divisibleBy
        }
        (0 until noOfRound).forEach { round ->
            monkeys.forEach { m ->
                (0 until m.items.size).forEach { i ->
                    val item = m.items[0]
                    m.items = m.items.drop(1).toMutableList()
                    when (m.operation) {
                        "+" -> {
//                        item.realValue = item.realValue + if (m.value == -1) item.realValue else m.value.toBigInteger()
                            item.modded = item.modded + if (m.value == -1) item.modded else m.value.toBigInteger()
                        }

                        "*" -> {
//                        item.realValue = item.realValue * (if (m.value == -1) item.realValue else m.value.toBigInteger())
                            item.modded = item.modded * (if (m.value == -1) item.modded else m.value.toBigInteger())
                        }
                    }
                    if (divideWorry) item.modded /= 3.toBigInteger()
                    item.modulo(superDiv)
                    if (item.divisibleBy(m.divisibleBy)) {
                        monkeys[m.whenTrue].items.add(item)
                    } else {
                        monkeys[m.whenFalse].items.add(item)
                    }
                    m.counting++
                }
            }
//            monkeys.forEach { monkey ->
//                println(monkey)
//            }
        }
        monkeys = monkeys.sortedByDescending { it.counting }.toMutableList()
        return monkeys
    }

    fun part1(input: List<String>): Long {
        val monkeys = _process(input, true, 20)
        return monkeys[0].counting * monkeys[1].counting
    }

    fun part2(input: List<String>): Long {
        val monkeys = _process(input, false, 10000)
        return monkeys[0].counting * monkeys[1].counting
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

data class Day11Monkey(
    val no: Int,
    var items: MutableList<Day11Item>,
    val operation: String,
    val value: Int,
    val divisibleBy: Int,
    val whenTrue: Int,
    val whenFalse: Int,
    var counting: Long = 0
)

data class Day11Item(var modded: BigInteger, var realValue: BigInteger) {
    fun modulo(superDiv: Int) {
        modded = modded.mod(superDiv.toBigInteger())
    }

    fun divisibleBy(value: Int): Boolean {
        return modded.mod(value.toBigInteger()).equals(BigInteger.ZERO)
    }
}
