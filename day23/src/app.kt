import java.io.File

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

fun main(args: Array<String>) {
    part1()
}