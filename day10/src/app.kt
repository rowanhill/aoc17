fun main(args: Array<String>) {

    var string = (0..255).toMutableList()

    var skipSize = 0
    var currentPosition = 0

    fun reverseLength(length: Int) {
        for (i in 0..((length-1)/2)) {
            val firstIndex = (currentPosition + i) % string.size
            val secondIndex = (currentPosition + length - 1 - i) % string.size

            val tmp = string[firstIndex]
            string[firstIndex] = string[secondIndex]
            string[secondIndex] = tmp
        }
    }

    // part 1
    val lengths = arrayOf(187,254,0,81,169,219,1,190,19,102,255,56,46,32,2,216)

    for (length in lengths) {
        reverseLength(length)
        currentPosition = (currentPosition + length + skipSize) % string.size
        skipSize += 1
    }
    println(string[0] * string[1])

    // part 2
    val lengths2 = "187,254,0,81,169,219,1,190,19,102,255,56,46,32,2,216"
            .map { it.toInt() }.toMutableList()
    lengths2.addAll(arrayOf(17, 31, 73, 47, 23))

    string = (0..255).toMutableList()
    skipSize = 0
    currentPosition = 0

    (0 until 64).forEach {
        for (length in lengths2) {
            reverseLength(length)
            currentPosition = (currentPosition + length + skipSize) % string.size
            skipSize += 1
        }
    }

    val hexes = string
            .windowed(16, 16)
            .map { it.reduce { a,b -> a.xor(b) } }
            .joinToString("") { String.format("%1$02X",it) }

    println(hexes)
}