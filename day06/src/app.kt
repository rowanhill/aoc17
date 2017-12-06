fun main(args: Array<String>) {
    val banks = intArrayOf(10,3,15,10,5,15,5,15,9,2,5,8,5,2,3,6)
    val numBanks = 16
    val seenConfigs = HashSet<Int>()

    fun maxIndex(): Int {
        var maxIndex = 0
        var max = 0
        banks.forEachIndexed { index, i ->
            if (i > max) {
                max = i
                maxIndex = index
            }
        }
        return maxIndex
    }

    fun redistributeBlocksFrom(index: Int) {
        var remaining = banks[index]
        banks[index] = 0
        var curIndex = index
        while (remaining > 0) {
            curIndex = (curIndex + 1) % numBanks
            banks[curIndex] += 1
            remaining -= 1
        }
    }

    // part 1
    var count = 0
    do {
        seenConfigs.add(banks.contentHashCode())

        val maxIndex = maxIndex()
        redistributeBlocksFrom(maxIndex)

        count += 1
    } while (!seenConfigs.contains(banks.contentHashCode()))
    println(count)

    // part 2
    val targetHashCode = banks.contentHashCode()
    count = 0
    do {
        val maxIndex = maxIndex()
        redistributeBlocksFrom(maxIndex)

        count += 1
    } while (banks.contentHashCode() != targetHashCode)
    println(count)
}