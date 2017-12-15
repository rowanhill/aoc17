import java.math.BigInteger

enum class SquareState {
    USED_UNMARKED, USED_MARKED, UNUSED
}

fun main(args: Array<String>) {
    val key = "ugkiagan"
    //val key = "flqrgnkx"

    fun reverseLength(currentPosition: Int, length: Int, string: MutableList<Int>) {
        for (i in 0..((length-1)/2)) {
            val firstIndex = (currentPosition + i) % string.size
            val secondIndex = (currentPosition + length - 1 - i) % string.size

            val tmp = string[firstIndex]
            string[firstIndex] = string[secondIndex]
            string[secondIndex] = tmp
        }
    }

    fun knotHashSparse(lengths: Array<Int>, string: MutableList<Int>, times: Int) {
        var skipSize = 0
        var currentPosition = 0

        repeat(times) {
            for (length in lengths) {
                reverseLength(currentPosition, length, string)
                currentPosition = (currentPosition + length + skipSize) % string.size
                skipSize += 1
            }
        }
    }

    fun knotHashDense(input: String): List<Int> {
        // prepare
        val string = (0..255).toMutableList()
        val lengths = input
                .map { it.toInt() }.toMutableList()
        lengths.addAll(arrayOf(17, 31, 73, 47, 23))

        // process
        knotHashSparse(lengths.toTypedArray(), string, 64)

        // post-process
        return string
                .chunked(16)
                .map { it.reduce { a, b -> a.xor(b) } }
    }

    fun knotHashString(input: String): String {
        return knotHashDense(input)
                .joinToString("") { String.format("%1$02X", it) }
    }

    var totalNumberOfOnes = 0
    //val grid = Array<Array<Boolean?>>(128, {_ -> Array(128, { _ -> null})})
    val grid = (0 until 128).map { index ->
        val lengthString = key + "-" + index.toString()

        val hexString = knotHashString(lengthString)
        val binaryString = BigInteger(hexString, 16).toString(2).padStart(128, '0')
//        println(binaryString)
        totalNumberOfOnes += binaryString.toCharArray().count { it == '1' }

        binaryString.toCharArray().map { char ->
            if (char == '1') {
                SquareState.USED_UNMARKED
            } else {
                SquareState.UNUSED
            }
        }.toTypedArray()
    }.toTypedArray()

    // part 1
    println(totalNumberOfOnes)

    val queue = ArrayList<Pair<Int, Int>>()
    var groupCount = 0
    var filledCount = 0
    while (filledCount < totalNumberOfOnes) {
        run {
            val rowIndex = grid.indexOfFirst { row -> row.indexOfFirst { it == SquareState.USED_UNMARKED } > -1 }
            val colIndex = grid[rowIndex].indexOfFirst { it == SquareState.USED_UNMARKED }
            groupCount += 1
            queue.add(Pair(rowIndex, colIndex))
        }

        while (queue.isNotEmpty()) {
            val (rowIndex, colIndex) = queue.removeAt(0)
            if (grid[rowIndex][colIndex] == SquareState.USED_UNMARKED) {
                filledCount += 1
                grid[rowIndex][colIndex] = SquareState.USED_MARKED

                arrayOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1)).forEach { (dx, dy) ->
                    val x = rowIndex+dx
                    val y = colIndex+dy
                    if (x < 0 || x >= 128 || y < 0 || y >= 128) {
                        // continue
                    } else if (grid[x][y] == SquareState.USED_UNMARKED) {
                        queue.add(Pair(x, y))
                    }
                }
            }
        }
    }

    // part 2
    println(groupCount)
}