import java.util.*

fun main(args: Array<String>) {
    val stepSize = 348

    run {
        val buffer = LinkedList<Int>()
        buffer.add(0)

        val rounds = 2017

        var currentIndex = 0

        (1..rounds).forEach { stepNum ->
            currentIndex = (currentIndex + stepSize) % buffer.size + 1
            buffer.add(currentIndex, stepNum)
        }

        // part 1
        println(buffer[currentIndex + 1])
    }

    run {
        var zeroIndex = 0
        var afterZeroValue = 0

        val rounds = 50_000_000

        var currentIndex = 0

        (1..rounds).forEach { stepNum ->
            currentIndex = (currentIndex + stepSize) % stepNum + 1

            if (currentIndex <= zeroIndex) {
                zeroIndex += 1
            } else if (currentIndex == zeroIndex + 1) {
                afterZeroValue = stepNum
            }
        }

        //part 2
        println(afterZeroValue)
    }
}