fun main() {
    val verbose = false

    fun _process(input: List<String>) : Day07Folder {
        val root = Day07Folder("/")
        var currentFolder = root
        val cdCmd = "\\$ cd (.+)".toRegex()
        val folderOutput = "dir (.+)".toRegex()
        val fileOutput = "(\\d+) .+".toRegex()
        input.forEach { line ->
            val cdMatch = cdCmd.find(line)
            if (cdMatch != null) {
                val folderName = cdMatch.destructured.toList()[0]
                if (folderName == "/") {
                    currentFolder = root
                } else if (folderName == "..") {
                    currentFolder = currentFolder.parent!!
                } else {
                    var folder = currentFolder.subFolders.find { it.name == "folderName" }
                    if (folder == null) {
                        folder = Day07Folder(folderName)
                        folder.parent = currentFolder
                        currentFolder.subFolders.add(folder)
                    }
                    currentFolder = folder
                }
                return@forEach
            }

            val folderMatch = folderOutput.find(line)
            if (folderMatch != null) {
                val folderName = folderMatch.destructured.toList()[0]
                var folder = currentFolder.subFolders.find { it.name == "folderName" }
                if (folder == null) {
                    folder = Day07Folder(folderName)
                    folder.parent = currentFolder
                    currentFolder.subFolders.add(folder)
                }
                return@forEach
            }

            val fileMatch = fileOutput.find(line)
            if (fileMatch != null) {
                val fileSize = fileMatch.destructured.toList()[0].toInt()
                currentFolder.fileSize += fileSize
                return@forEach
            }
        }
        root.calcAllFileSize()
        return root
    }
    fun part1(input: List<String>): Int {
        val root = _process(input)
        return root.part1Calc(0, verbose)
    }

    fun part2(input: List<String>): Int {
        val root = _process(input)
        return root.part2Calc(0, verbose, Int.MAX_VALUE)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

data class Day07Folder(val name: String) {
    var parent: Day07Folder? = null
    val subFolders = mutableListOf<Day07Folder>()
    var fileSize = 0
    var allFileSize = 0

    fun part1Calc(indent: Int, verbose: Boolean) : Int {
        var result = 0
        if (verbose) repeat(indent) { print(" ") }
        if (verbose) println("dir $name (file size = $fileSize) (all file size = $allFileSize)")
        if (allFileSize < 100000) result += allFileSize
        subFolders.forEach { folder ->
            result += folder.part1Calc(indent + 4, verbose)
        }
        return result
    }

    fun part2Calc(indent: Int, verbose: Boolean, smallestBigEnough: Int) : Int {
        var result = smallestBigEnough
        if (verbose) repeat(indent) { print(" ") }
        if (verbose) println(allFileSize.toString() + " " + (allFileSize >= 8381165).toString() + " " + (allFileSize < result).toString())
        if (allFileSize >= 8381165 && allFileSize < result) result = allFileSize
        subFolders.forEach { folder ->
            result = folder.part2Calc(indent + 4, verbose, result)
        }
        return result
    }

    fun calcAllFileSize(): Int {
        allFileSize = fileSize + subFolders.sumOf { folder -> folder.calcAllFileSize() }
        return allFileSize
    }
}
