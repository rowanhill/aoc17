import java.io.File

fun main(args: Array<String>) {
    val pipes = HashMap<String, MutableSet<String>>().withDefault { HashSet<String>() }
    val programs = HashSet<String>()

    File("src/input.txt").readLines().forEach { line ->
        val (from, tosString) = line.split(" <-> ")
        val tos = tosString.split(", ")

        if (pipes[from] == null) {
            pipes[from] = HashSet<String>()
        }
        
        pipes[from]!!.addAll(tos)

        programs.add(from)
    }

    fun exploreGroup(startingAt: String): Int {
        val connected = HashSet<String>()
        val toExplore = ArrayList<String>()

        toExplore.add(startingAt)
        programs.remove(startingAt)

        while (toExplore.isNotEmpty()) {
            val next = toExplore.removeAt(0)
            connected.add(next)
            val neighbours = pipes[next]!!

            neighbours.forEach {
                if (!connected.contains(it)) {
                    toExplore.add(it)
                    programs.remove(it)
                }
            }
        }

        return connected.size
    }

    println(exploreGroup("0"))

    var groupCount = 1
    while (programs.isNotEmpty()) {
        groupCount += 1

        val newGroupMember = programs.first()
        programs.remove(newGroupMember)
        exploreGroup(newGroupMember)
    }

    println(groupCount)
}