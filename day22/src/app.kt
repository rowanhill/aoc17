import java.io.File


fun main(args: Array<String>) {
    val directions = listOf(
            Pair(-1, 0), // up    = 0
            Pair(0, 1),  // right = 1
            Pair(1, 0),  // down  = 2
            Pair(0, -1)  // left  = 3
    )

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
    fun infect() {
        numInfected++
        infected.add(curPosition)
    }
    fun clean() {
        infected.remove(curPosition)
    }

    repeat(10000) {
        if (isInfected()) {
            turnRight()
        } else {
            turnLeft()
        }

        if (!isInfected()) {
            infect()
        } else {
            clean()
        }

        move()
    }

    println(numInfected)
}