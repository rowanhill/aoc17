import java.io.File
import kotlin.math.max

data class Instruction(val target: String, val delta: Int, val reference: String, val refOp: String, val refVal: Int)

// ba dec 37 if znx != 0
fun main(args: Array<String>) {
    val instructions = File("src/input.txt").readLines().map { line ->
        val parts = line.split(" ")
        val target = parts[0]
        val incDec = parts[1]
        val change = parts[2].toInt()
        val delta = if (incDec == "inc") change else -change
        val ref = parts[4]
        val op = parts[5]
        val refVal = parts[6].toInt()
        Instruction(target, delta, ref, op, refVal)
    }

    val registers = HashMap<String, Int>()

    var maxEver = 0
    instructions.forEach { instr ->
        if (!registers.containsKey(instr.target)) {
            registers[instr.target] = 0
        }
        if (!registers.containsKey(instr.reference)) {
            registers[instr.reference] = 0
        }

        val refRegVal = registers[instr.reference]!!

        val refConditionTrue = when (instr.refOp) {
            ">" -> refRegVal > instr.refVal
            ">=" -> refRegVal >= instr.refVal
            "==" -> refRegVal == instr.refVal
            "!=" -> refRegVal != instr.refVal
            "<=" -> refRegVal <= instr.refVal
            "<" -> refRegVal < instr.refVal
            else -> throw Exception("Unknown op ${instr.refOp}")
        }

        if (refConditionTrue) {
            registers[instr.target] = registers[instr.target]!! + instr.delta
            if (registers[instr.target]!! > maxEver) {
                maxEver = registers[instr.target]!!
            }
        }
    }

    // part 1
    println(registers.maxBy { entry -> entry.value })

    // part 2
    println(maxEver)
}