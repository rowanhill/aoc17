import java.io.File

fun main(args: Array<String>) {
    val instrs: MutableList<Int> = File("src/input.txt").readLines().map { it.toInt() }.toMutableList()

    var index = 0
    var stepCounter = 0
    while (index in 0 until instrs.size) {
        val current = instrs[index]

        // part 1: instrs[index] = current + 1
        // part 2:
        if (current >= 3) {
            instrs[index] = current - 1
        } else {
            instrs[index] = current + 1
        }
        index += current
        stepCounter += 1

        if (stepCounter % 100 == 0) println("So far: $stepCounter")
    }
    println(stepCounter)
}