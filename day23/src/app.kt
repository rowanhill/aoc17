import java.io.File
import java.lang.Math.floor
import java.lang.Math.sqrt

fun part1() {
    val registers = HashMap<String, Long>()

    fun getValue(valOrReg: String): Long {
        return if (valOrReg.toLongOrNull() == null) {
            registers.getOrDefault(valOrReg, 0L)
        } else {
            valOrReg.toLong()
        }
    }

    val instrParts = File("src/input.txt").readLines().map { it.split(" ") }

    var curInstr = 0
    var mulCounter = 0

    loop@ while (curInstr >= 0 && curInstr <= instrParts.lastIndex) {
        var jumped = false
        val parts = instrParts[curInstr]

        when (parts[0]) {
            "set" -> {
                registers.put(parts[1], getValue(parts[2]))
            }
            "sub" -> {
                registers.put(parts[1], getValue(parts[1]) - getValue(parts[2]))
            }
            "mul" -> {
                mulCounter++
                registers.put(parts[1], getValue(parts[1]) * getValue(parts[2]))
            }
            "mod" -> {
                registers.put(parts[1], getValue(parts[1]) % getValue(parts[2]))
            }
            "jnz" -> {
                if (getValue(parts[1]) != 0L) {
                    curInstr += getValue(parts[2]).toInt()
                    jumped = true
                }
            }
        }

        if (!jumped) {
            curInstr += 1
        }
    }

    println(mulCounter)
}

fun part2concise(): Int {
    val min = 93L * 100 + 100000
    val max = min + 17000
    return (min..max step 17)
            .filter {
                // Optimisation: only need to check factors up to the square root
                val maxFactor = floor(sqrt(it.toDouble())).toLong()
                // Optimisation: only need to find one factor
                (2..maxFactor).any { d -> it % d == 0L }
            }
            .count()
}

fun part2(): Long {
    // val a = 1L // not needed (in part 2)
    var b = 93L // 0
    val c: Long
    var d: Long
    // var e = 0L // not needed
    var f: Long
    // var g = 0L // not needed
    var h = 0L

    // c = b // 1 - always overwritten (in part 2)
    // if (a != 0L) { // 2, 3 - always true (in part 2)
        b = (b * 100) + 100000 // 4, 5
        c = b + 17000 // 6, 7
    // }

    while (true) { // 31
        f = 1 // 8
        d = 2 // 9
        // e = 2 // 10 - not needed

        // 11 - 23 : check if b has any factors (is not prime), set f = 0 if so
        do {
            // 11 - 19 : check if d is a factor of b, set f = 0 if so
            /*
            do {
                if (d * e == b) { // if => 14, d*e == b => 11, 12, 13
                    f = 0 // 15
                }
                e++ // 16
            } while (e != b) // while => 19, e != b => 17, 18
            */
            if (b % d == 0L) { // 11 - 19 : check if d is a factor of b
                f = 0 // 14, 15 : set f to 0 if so
                break // optimisation: once we've found a divisor, no need to carry on
            }

            d++ // 20
        } while (d != b) // while => 23, d != b => 21, 22

        if (f == 0L) { // 24 : count the number of non-prime bs we see
            h++ // 25
        }

        if (b == c) { // if => 28, b == c => 26, 27
            return h // 29
        }

        b += 17 // 30
    } // while(true) => 31
}

fun main(args: Array<String>) {
    part1()
    println(part2())
    println(part2concise())
}