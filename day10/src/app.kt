fun main(args: Array<String>) {
    fun reverseLength(currentPosition: Int, length: Int, string: MutableList<Int>) {
        for (i in 0..((length-1)/2)) {
            val firstIndex = (currentPosition + i) % string.size
            val secondIndex = (currentPosition + length - 1 - i) % string.size

            val tmp = string[firstIndex]
            string[firstIndex] = string[secondIndex]
            string[secondIndex] = tmp
        }
    }

    fun knotHash(lengths: Array<Int>, string: MutableList<Int>, times: Int) {
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

    // part 1
    run {
        // prepare
        val string = (0..255).toMutableList()
        val lengths = arrayOf(187, 254, 0, 81, 169, 219, 1, 190, 19, 102, 255, 56, 46, 32, 2, 216)

        // process
        knotHash(lengths, string, 1)

        // print
        println(string[0] * string[1])
    }

    // part 2
    run {
        // prepare
        val string = (0..255).toMutableList()
        val lengths = "187,254,0,81,169,219,1,190,19,102,255,56,46,32,2,216"
                .map { it.toInt() }.toMutableList()
        lengths.addAll(arrayOf(17, 31, 73, 47, 23))

        // process
        knotHash(lengths.toTypedArray(), string, 64)

        // post-process
        val hexes = string
                .chunked(16)
                .map { it.reduce { a, b -> a.xor(b) } }
                .joinToString("") { String.format("%1$02X", it) }

        // print
        println(hexes)
    }
}