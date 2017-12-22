import java.io.File

enum class State {
    Clean,
    Weakened,
    Infected,
    Flagged
}

fun main(args: Array<String>) {
    val directions = listOf(
            Pair(-1, 0), // up    = 0
            Pair(0, 1),  // right = 1
            Pair(1, 0),  // down  = 2
            Pair(0, -1)  // left  = 3
    )

    val weakened = HashSet<Pair<Int, Int>>()
    val flagged = HashSet<Pair<Int, Int>>()
    val mapSizeHalf = 12
    val infected = HashSet<Pair<Int, Int>>()
    File("src/input.txt").readLines().forEachIndexed { lineIndex, line ->
        line.toCharArray().forEachIndexed { colIndex, c ->
            if (c == '#') {
                infected.add(Pair(lineIndex - mapSizeHalf, colIndex - mapSizeHalf))
            }
        }
    }

    var curPosition = Pair(0, 0)
    var curDirectionIndex = 0

    fun turnLeft() {
        curDirectionIndex = (curDirectionIndex - 1 + directions.size) % directions.size
    }
    fun turnRight() {
        curDirectionIndex = (curDirectionIndex + 1) % directions.size
    }
    fun move() {
        curPosition = Pair(
                curPosition.first + directions[curDirectionIndex].first,
                curPosition.second + directions[curDirectionIndex].second
                )
    }

    var numInfected = 0
    fun isInfected(): Boolean {
        return infected.contains(curPosition)
    }
    fun isWeakened(): Boolean {
        return weakened.contains(curPosition)
    }
    fun isFlagged(): Boolean {
        return flagged.contains(curPosition)
    }
    fun getState(): State {
        return when {
            isInfected() -> State.Infected
            isWeakened() -> State.Weakened
            isFlagged() -> State.Flagged
            else -> State.Clean
        }
    }
    fun weaken() {
        weakened.add(curPosition)
    }
    fun flag() {
        infected.remove(curPosition)
        flagged.add(curPosition)
    }
    fun infect() {
        numInfected++
        weakened.remove(curPosition)
        infected.add(curPosition)
    }
    fun clean() {
        flagged.remove(curPosition)
        infected.remove(curPosition)
    }

    repeat(10000000) {
        val curState = getState()
        
        when (curState) {
            State.Clean -> turnLeft()
            State.Weakened -> { /* no-op */ }
            State.Infected -> turnRight()
            State.Flagged -> { turnLeft(); turnLeft() }
        }

        when (curState) {
            State.Clean -> weaken()
            State.Weakened -> infect()
            State.Infected -> flag()
            State.Flagged -> clean()
        }

        move()
    }

    println(numInfected)
}