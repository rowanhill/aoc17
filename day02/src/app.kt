import java.io.File

fun main(args: Array<String>) {
    val sheet = ArrayList<ArrayList<Int>>()

    File("src/spreadsheet.txt").useLines { lines ->
        lines.forEach { line ->
            val sheetLine = ArrayList<Int>()
            line.split("\t").forEach { value ->
                sheetLine.add(value.toInt())
            }
            sheet.add(sheetLine)
        }
    }

    // part 1
    var sum = 0
    sheet.forEach { line ->
        var min = Int.MAX_VALUE;
        var max = Int.MIN_VALUE;
        line.forEach { v ->
            if (v < min) {
                min = v;
            }
            if (v > max) {
                max = v;
            }
        }
        val diff = max - min
        sum += diff;
    }
    println(sum)


    // part 2
    println(sheet.map { line -> findEvenDivisors(line) }.sum())
}

fun findEvenDivisors(line: ArrayList<Int>): Int {
    var sum = 0
    line.subList(0, line.size - 1).forEachIndexed line@ { index, a ->
        line.subList(index + 1, line.size).forEach { b ->
            // println("a: $a, b: $b, a%b=${a%b} b%a=${b%a}")
            if (a % b == 0) {
                // println("$a / $b = ${a/b}")
                sum += a / b
                return@line
            } else if (b % a == 0) {
                // println("$b / $a =  ${b/a}")
                sum += b / a
                return@line
            }
        }
    }
    // println(sum)
    return sum
}