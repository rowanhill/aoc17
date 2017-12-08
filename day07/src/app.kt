import java.io.File

data class Program(
        val name: String,
        val weight: Int,
        val childNames: List<String>?,
        var parent: String? = null,
        var towerWeight: Int? = null
)

fun main(args: Array<String>) {
    val pattern = Regex("(\\S+) \\((\\d+)\\)(?: -> (.+))?")
    val programs = HashMap<String, Program>()

    File("src/input.txt").readLines().forEach { line ->
        val result = pattern.matchEntire(line)
        if (result != null) {
            val name = result.groups[1]!!.value
            val weight = result.groups[2]!!.value.toInt()
            val childNames = result.groups[3]?.value?.split(", ")
            val program = Program(name, weight, childNames)
            //println("$program")

            programs.put(name, program)
        }
    }

    programs.forEach { name, program ->
        program.childNames?.forEach { childName ->
            programs[childName]!!.parent = name
        }
    }

    var program = programs.entries.first().value
    while (program.parent != null) {
        program = programs[program.parent!!]!!
    }
    println(program)

    fun setTowerWeight(start: Program): Int {
        val childrenWeight = start.childNames?.map { programs[it]!! }?.map { setTowerWeight(it) }?.sum() ?: 0
        start.towerWeight = start.weight + childrenWeight
        return start.towerWeight!!
    }

    fun printCorrectedWeight(prog: Program, delta: Int) {
        if (prog.childNames == null) {
            println("Reached leaf program ${prog.name} with delta $delta. Corrected weight is ${prog.weight + delta}")
        } else {
            val groups = prog.childNames.map { programs[it]!! }.groupBy { it.towerWeight }
            if (groups.size == 1) {
                println("Reached leaf program ${prog.name} with delta $delta. Corrected weight is ${prog.weight + delta}")
            } else {
                val wrongWeightProg = groups.filter { entry -> entry.value.size == 1 }.values.first()[0]
                val correctTowerWeight = groups.filter { entry -> entry.value.size > 1 }.values.first()[0].towerWeight!!
                val delta = correctTowerWeight - wrongWeightProg.towerWeight!!
                printCorrectedWeight(wrongWeightProg, delta)
            }
        }
    }

    setTowerWeight(program)
    printCorrectedWeight(program, 0)
}