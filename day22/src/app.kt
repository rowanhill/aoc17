import java.io.File

enum class State {
    Clean,
    Weakened,
    Infected,
    Flagged
}

class VirusCarrier {
    var curPosition = Pair(0, 0)
    private var curDirectionIndex = 0

    private val directions = listOf(
            Pair(-1, 0), // up    = 0
            Pair(0, 1),  // right = 1
            Pair(1, 0),  // down  = 2
            Pair(0, -1)  // left  = 3
    )

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
}

fun part1() {
    val mapSizeHalf = 12
    val infected = HashSet<Pair<Int, Int>>()
    File("src/input.txt").readLines().forEachIndexed { lineIndex, line ->
        line.toCharArray().forEachIndexed { colIndex, c ->
            if (c == '#') {
                infected.add(Pair(lineIndex - mapSizeHalf, colIndex - mapSizeHalf))
            }
        }
    }

    val virusCarrier = VirusCarrier()

    var numInfected = 0
    fun isInfected(): Boolean {
        return infected.contains(virusCarrier.curPosition)
    }
    fun infect() {
        numInfected++
        infected.add(virusCarrier.curPosition)
    }
    fun clean() {
        infected.remove(virusCarrier.curPosition)
    }

    repeat(10000) {
        if (isInfected()) {
            virusCarrier.turnRight()
        } else {
            virusCarrier.turnLeft()
        }

        if (!isInfected()) {
            infect()
        } else {
            clean()
        }

        virusCarrier.move()
    }

    println(numInfected)
}


fun part2() {
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

    val virusCarrier = VirusCarrier()

    var numInfected = 0
    fun isInfected(): Boolean {
        return infected.contains(virusCarrier.curPosition)
    }
    fun isWeakened(): Boolean {
        return weakened.contains(virusCarrier.curPosition)
    }
    fun isFlagged(): Boolean {
        return flagged.contains(virusCarrier.curPosition)
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
        weakened.add(virusCarrier.curPosition)
    }
    fun flag() {
        infected.remove(virusCarrier.curPosition)
        flagged.add(virusCarrier.curPosition)
    }
    fun infect() {
        numInfected++
        weakened.remove(virusCarrier.curPosition)
        infected.add(virusCarrier.curPosition)
    }
    fun clean() {
        flagged.remove(virusCarrier.curPosition)
        infected.remove(virusCarrier.curPosition)
    }

    repeat(10000000) {
        val curState = getState()

        when (curState) {
            State.Clean -> virusCarrier.turnLeft()
            State.Weakened -> { /* no-op */ }
            State.Infected -> virusCarrier.turnRight()
            State.Flagged -> { virusCarrier.turnLeft(); virusCarrier.turnLeft() }
        }

        when (curState) {
            State.Clean -> weaken()
            State.Weakened -> infect()
            State.Infected -> flag()
            State.Flagged -> clean()
        }

        virusCarrier.move()
    }

    println(numInfected)
}

fun main(args: Array<String>) {
    part1()
    part2()
}