import Direction.*
import java.io.File

enum class Direction(val dx: Int, val dy: Int) {
    DOWN(0, 1),
    LEFT (-1, 0),
    RIGHT(1, 0),
    UP(0, -1)
}

fun main(args: Array<String>) {
    val map = File("src/input.txt").readLines()

    var y = 0
    var x = map[0].indexOf('|')
    var direction = DOWN
    val letters = ArrayList<Char>()
    var numSteps = 1

    loop@ while (map[y][x] != ' ') {
        x += direction.dx
        y += direction.dy
        numSteps++

        val newchar = map[y][x]

        when (newchar) {
            '|' -> {
                // carry on
            }
            '-' -> {
                // carry on
            }
            ' ' -> {
                break@loop
            }
            '+' -> {
                if (direction == DOWN || direction == UP) {
                    // check left and right
                    if (x - 1 >= 0 && map[y][x - 1] != ' ') {
                        x -= 1
                        direction = LEFT
                    } else if (x + 1 <= map[y].lastIndex && map[y][x + 1] != ' ') {
                        x += 1
                        direction = RIGHT
                    } else {
                        throw Exception("Couldn't figure out which way to turn at corner ($x, $y)")
                    }
                } else {
                    // check up and down
                    if (y + 1 <= map.lastIndex && map[y + 1][x] != ' ') {
                        y += 1
                        direction = DOWN
                    } else if (y - 1 >= 0 && map[y - 1][x] != ' ') {
                        y -= 1
                        direction = UP
                    } else {
                        throw Exception("Couldn't figure out which way to turn at corner ($x, $y)")
                    }
                }
                numSteps++
            }
            else -> {
                letters.add(newchar)
            }
        }
    }

    println(letters.joinToString(""))
    println(numSteps - 1)
}