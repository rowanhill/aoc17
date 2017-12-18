import java.io.File

class Scheduler {
    private val programA: Program
    private val programB: Program

    init {
        val instrParts = File("src/input.txt").readLines().map { it.split(" ") }

        programA = Program(0, instrParts, this)
        programB = Program(1, instrParts, this)
    }

    fun sendMessageFrom(programID: Int, value: Long) {
        when (programID) {
            0 -> programB.addToMessageQueue(value)
            1 -> programA.addToMessageQueue(value)
            else -> throw Exception("Unexpected program ID $programID")
        }
    }

    fun numMessagesSentByProgramB(): Int {
        return programB.numValuesSent
    }

    fun process() {
        var didAdvance: Boolean

        do {
            didAdvance = false
            didAdvance = programA.processInstr() || didAdvance
            didAdvance = programB.processInstr() || didAdvance
        } while (didAdvance)

        println(numMessagesSentByProgramB())
    }
}

class Program(private val programID: Int, private val instrParts: List<List<String>>, private val scheduler: Scheduler) {
    var numValuesSent = 0

    private val registers = HashMap<String, Long>()
    private val messageQueue = ArrayList<Long>()
    private var curInstr = 0

    init {
        registers.put("p", programID.toLong())
    }

    private fun getValue(valOrReg: String): Long {
        return if (valOrReg.toLongOrNull() == null) {
            registers.getOrDefault(valOrReg, 0L)
        } else {
            valOrReg.toLong()
        }
    }

    fun addToMessageQueue(value: Long) {
        this.messageQueue.add(value)
    }

    fun processInstr(): Boolean {
        if (curInstr < 0 || curInstr > instrParts.lastIndex) {
            return false // outside program
        }

        var jumped = false
        val parts = instrParts[curInstr]

        when (parts[0]) {
            "snd" -> {
                scheduler.sendMessageFrom(programID, getValue(parts[1]))
                numValuesSent++
            }
            "set" -> {
                registers.put(parts[1], getValue(parts[2]))
            }
            "add" -> {
                registers.put(parts[1], getValue(parts[1]) + getValue(parts[2]))
            }
            "mul" -> {
                registers.put(parts[1], getValue(parts[1]) * getValue(parts[2]))
            }
            "mod" -> {
                registers.put(parts[1], getValue(parts[1]) % getValue(parts[2]))
            }
            "rcv" -> {
                if (messageQueue.isEmpty()) {
                    return false // blocking on empty message queue
                } else {
                    registers.put(parts[1], messageQueue.removeAt(0))
                }
            }
            "jgz" -> {
                if (getValue(parts[1]) > 0) {
                    curInstr += getValue(parts[2]).toInt()
                    jumped = true
                }
            }
        }

        if (!jumped) {
            curInstr += 1
        }

        return true
    }
}

fun part1() {
    val registers = HashMap<String, Long>()
    var lastPlayedFreq: Long? = null
    var firstRecoveredFreq: Long? = null

    fun getValue(valOrReg: String): Long {
        return if (valOrReg.toLongOrNull() == null) {
            registers.getOrDefault(valOrReg, 0L)
        } else {
            valOrReg.toLong()
        }
    }

    val instrParts = File("src/input.txt").readLines().map { it.split(" ") }

    var curInstr = 0

    loop@ while (curInstr >= 0 && curInstr <= instrParts.lastIndex) {
        var jumped = false
        val parts = instrParts[curInstr]

        when (parts[0]) {
            "snd" -> {
                lastPlayedFreq = getValue(parts[1])
            }
            "set" -> {
                registers.put(parts[1], getValue(parts[2]))
            }
            "add" -> {
                registers.put(parts[1], getValue(parts[1]) + getValue(parts[2]))
            }
            "mul" -> {
                registers.put(parts[1], getValue(parts[1]) * getValue(parts[2]))
            }
            "mod" -> {
                registers.put(parts[1], getValue(parts[1]) % getValue(parts[2]))
            }
            "rcv" -> {
                if (getValue(parts[1]) != 0L) {
                    if (firstRecoveredFreq == null) {
                        firstRecoveredFreq = lastPlayedFreq
                        println(firstRecoveredFreq)
                        break@loop
                    }
                }
            }
            "jgz" -> {
                if (getValue(parts[1]) > 0) {
                    curInstr += getValue(parts[2]).toInt()
                    jumped = true
                }
            }
        }

        if (!jumped) {
            curInstr += 1
        }
    }
}

fun main(args: Array<String>) {
    part1()

    // part 2
    val scheduler = Scheduler()
    scheduler.process()
}