fun main(args: Array<String>) {
    // part 1 done by hand:
    // - square root input, integer result is even => top left corner, fractional result is .9... => near bottom right
    // - calculate next biggest square (bottom right)
    // - calculate difference between input and next biggest square (horizontal distance to 1)
    // - calculate half of square root of input (vertical distance to 1)
    // - sum horizontal and vertical distances

    val edgeSize = 1001
    val grid = ArrayList<ArrayList<Int?>>(edgeSize)
    for (i in 0 until edgeSize) {
        val row = ArrayList<Int?>(edgeSize)
        for (j in 0 until edgeSize) {
            row.add(null)
        }
        grid.add(row)
    }

    fun printGrid(halfEdgeSize: Int) {
        for (dy in -halfEdgeSize..halfEdgeSize) {
            for (dx in -halfEdgeSize..halfEdgeSize) {
                print(grid[edgeSize / 2 + dy][edgeSize / 2 + dx])
                print("\t")
            }
            println()
        }
    }

    fun sumAround(x: Int, y: Int): Int {
        return  (grid[y-1][x-1] ?: 0) + (grid[y-1][x] ?: 0) + (grid[y-1][x+1] ?: 0) +
                (grid[y  ][x-1] ?: 0)                       + (grid[y  ][x+1] ?: 0) +
                (grid[y+1][x-1] ?: 0) + (grid[y+1][x] ?: 0) + (grid[y+1][x+1] ?: 0)
    }

    var currentValue = 1
    val targetValue = 277678
    var currentEdgeSize = 0
    var x = edgeSize / 2
    var y = edgeSize / 2
    grid[y][x] = 1

    outer@ while (true) {
        currentEdgeSize += 1
        println("bottom left, new edge size $currentEdgeSize, current value $currentValue")
        printGrid(currentEdgeSize)

        // from bottom left, go right
        for (dummy in 0 until currentEdgeSize) {
            x += 1
            currentValue = sumAround(x, y)
            if (currentValue > targetValue) {
                println(currentValue)
                break@outer
            }
            grid[y][x] = currentValue
        }

        // from bottom right, go up
        for (dummy in 0 until currentEdgeSize) {
            y -= 1
            currentValue = sumAround(x, y)
            if (currentValue > targetValue) {
                println(currentValue)
                break@outer
            }
            grid[y][x] = currentValue
        }

        currentEdgeSize += 1
        println("top right, new edge size $currentEdgeSize, current value $currentValue")
        printGrid(currentEdgeSize)

        // from top right, go left
        for (dummy in 0 until currentEdgeSize) {
            x -= 1
            currentValue = sumAround(x, y)
            if (currentValue > targetValue) {
                println(currentValue)
                break@outer
            }
            grid[y][x] = currentValue
        }

        // from top left, go down
        for (dummy in 0 until currentEdgeSize) {
            y += 1
            currentValue = sumAround(x, y)
            if (currentValue > targetValue) {
                println(currentValue)
                break@outer
            }
            grid[y][x] = currentValue
        }
    }
}