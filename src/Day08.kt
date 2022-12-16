fun main() {
    val verbose = false

    fun part1(input: List<String>): Int {
        var result = 0
        val trees = mutableListOf<MutableList<Pair<Int, Boolean>>>()
        input.forEach { line ->
            val treeRow = mutableListOf<Pair<Int, Boolean>>()
            line.toList().forEach { height ->
                treeRow.add(Pair(height.toString().toInt(), false))
            }
            trees.add(treeRow)
        }

        (0 until trees.size).forEach { i ->
            var maxHeight = -1
            (0 until trees[i].size).forEach { j ->
                if (trees[i][j].first > maxHeight) {
                    trees[i][j] = Pair(trees[i][j].first, true)
                }
                maxHeight = maxHeight.coerceAtLeast(trees[i][j].first)
            }
            maxHeight = -1
            (0 until trees[i].size).reversed().forEach { j ->
                if (trees[i][j].first > maxHeight) {
                    trees[i][j] = Pair(trees[i][j].first, true)
                }
                maxHeight = maxHeight.coerceAtLeast(trees[i][j].first)
            }
        }
        (0 until trees[0].size).forEach { j ->
            var maxHeight = -1
            (0 until trees.size).forEach { i ->
                if (trees[i][j].first > maxHeight) {
                    trees[i][j] = Pair(trees[i][j].first, true)
                }
                maxHeight = maxHeight.coerceAtLeast(trees[i][j].first)
            }
            maxHeight = -1
            (0 until trees.size).reversed().forEach { i ->
                if (trees[i][j].first > maxHeight) {
                    trees[i][j] = Pair(trees[i][j].first, true)
                }
                maxHeight = maxHeight.coerceAtLeast(trees[i][j].first)
            }
        }

        trees.forEach { row ->
            row.forEach { tree ->
                if (verbose) print(if (tree.second) "1" else "0")
                if (tree.second) result++
            }
            if (verbose) println()
        }

        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0
        val trees = mutableListOf<MutableList<Pair<Int, Int>>>()
        input.forEach { line ->
            val treeRow = mutableListOf<Pair<Int, Int>>()
            line.toList().forEach { height ->
                treeRow.add(Pair(height.toString().toInt(), 0))
            }
            trees.add(treeRow)
        }

        (0 until trees.size).forEach { i ->
            (0 until trees[i].size).forEach { j ->
                var u = 0
                var d = 0
                var l = 0
                var r = 0
                if (i > 0) run breakFor@{
                    (i - 1 downTo 0).forEach { a ->
                        u++
                        if (trees[a][j].first >= trees[i][j].first) return@breakFor
                    }
                }
                if (i < trees.size - 1) run breakFor@{
                    (i + 1 until trees.size).forEach { a ->
                        d++
                        if (trees[a][j].first >= trees[i][j].first) return@breakFor
                    }
                }
                if (j > 0) run breakFor@{
                    (j - 1 downTo 0).forEach { a ->
                        l++
                        if (trees[i][a].first >= trees[i][j].first) return@breakFor
                    }
                }
                if (j < trees[0].size - 1) run breakFor@{
                    (j + 1 until trees[0].size).forEach { a ->
                        r++
                        if (trees[i][a].first >= trees[i][j].first) return@breakFor
                    }
                }
                if (verbose) println("[$i][$j] U$u D$d L$l R$r")
                trees[i][j] = Pair(trees[i][j].first, u * d * l * r)
            }
        }

        trees.forEach { row ->
            row.forEach { tree ->
                if (verbose) print("[" + tree.second + "]")
                result = result.coerceAtLeast(tree.second)
            }
            if (verbose) println()
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
