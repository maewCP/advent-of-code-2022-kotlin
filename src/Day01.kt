fun main() {

    fun prepareInput(allInput: String): MutableList<Int> {
        val elfCalories = mutableListOf<Int>()
        allInput.split("\n\n").forEach { elf ->
            elfCalories.add(elf.split("\n").sumOf { cal -> cal.toInt() })
        }
        return elfCalories
    }

    fun part1(input: List<String>): Int {
        val allInput = input.joinToString(separator = "\n")
        val elfCalories = prepareInput(allInput)
        return elfCalories.maxOf { it }
    }

    fun part2(input: List<String>): Int {
        val allInput = input.joinToString(separator = "\n")
        val elfCalories = prepareInput(allInput)
        elfCalories.sortDescending()
        return (0..2).sumOf { elfCalories[it] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
