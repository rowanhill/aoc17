import java.io.File

data class Scanner(val depth: Int, val range: Int)

fun main(args: Array<String>) {
    val scanners = ArrayList<Scanner>()
    File("src/input.txt").readLines().map { it.split(": ") }.forEach { (depth, range) ->
        scanners.add(Scanner(depth.toInt(), range.toInt()))
    }

    fun calcSeverityForDelay(delay: Int): Int? {
        var severity: Int? = null
        scanners.forEach { scanner ->
            val position = (scanner.depth + delay) % (scanner.range * 2 - 2)
            if (position == 0) {
                if (severity == null) {
                    severity = 0
                }
                severity = severity!! + scanner.depth * scanner.range
            }
        }
        return severity
    }

    // part 1
    println(calcSeverityForDelay(0))

    // part 2
    var i = 1
    while (true) {
        val severity = calcSeverityForDelay(i)
        if (severity == null) {
            println(i)
            break
        }
        i++
    }
}