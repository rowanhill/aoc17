import java.io.File

/*

00 01 02    20 10 00
10 11 12    21 11 01
20 21 22    22 12 02

 */
fun rotate90(grid: List<List<Char>>): List<List<Char>> {
    return when {
        grid.size == 3 -> listOf(
                listOf(grid[2][0], grid[1][0], grid[0][0]),
                listOf(grid[2][1], grid[1][1], grid[0][1]),
                listOf(grid[2][2], grid[1][2], grid[0][2])
        )
        grid.size == 2 -> listOf(
                listOf(grid[1][0], grid[0][0]),
                listOf(grid[1][1], grid[0][1])
        )
        else -> throw Exception("Unexpected sub-grid size when rotating: ${grid.size}")
    }
}

fun flipX(grid: List<List<Char>>): List<List<Char>> {
    return grid.map { it.asReversed() }
}

fun flipY(grid: List<List<Char>>): List<List<Char>> {
    return grid.asReversed()
}

fun asRule(grid: List<List<Char>>): String {
    return grid.joinToString("/") { it.joinToString("") }
}

fun asGrid(spec: String): List<List<Char>> {
    return spec.split("/").map { it.toCharArray().asList() }
}

fun permutations(grid: List<List<Char>>): List<String> {
    val result = ArrayList<String>()

    result.add(asRule(grid))
    result.add(asRule(flipX(grid)))
    result.add(asRule(flipY(grid)))

    var permutation = grid
    (0 until 4).forEach {
        permutation = rotate90(permutation)
        result.add(asRule(permutation))
        result.add(asRule(flipX(permutation)))
        result.add(asRule(flipY(permutation)))
    }

    return result
}

fun main(args: Array<String>) {
    val rules = File("src/input.txt").readLines()
            .map { it.split(" => ") }
            .map { it[0] to it[1] }
            .flatMap { permutations(asGrid(it.first)).distinct().map { p -> p to it.second } } //permute each rule input
            .toMap()

    /*
    Grid starts off as:
        .#.
        ..#
        ###
     */
    var grid = listOf(
            listOf('.', '#', '.'),
            listOf('.', '.', '#'),
            listOf('#', '#', '#')
    )

    fun expandGrid(grid: List<List<Char>>): List<List<Char>> {
        val subGridSize = if (grid.size % 2 == 0) {
            2
        } else if (grid.size % 3 == 0) {
            3
        } else {
            throw Exception("Unexpected grid size: ${grid.size}")
        }

        return (0 until grid.size / subGridSize).flatMap { y ->
            val newRows = (0..subGridSize).map { ArrayList<Char>() }.toMutableList()

            (0 until grid.size / subGridSize).forEach { x ->
                val subGrid =
                        if (subGridSize == 2) {
                            listOf(
                                    listOf(grid[y * 2][x * 2], grid[y * 2][x * 2 + 1]),
                                    listOf(grid[y * 2 + 1][x * 2], grid[y * 2 + 1][x * 2 + 1])
                            )
                        } else {
                            listOf(
                                    listOf(grid[y * 3][x * 3], grid[y * 3][x * 3 + 1], grid[y * 3][x * 3 + 2]),
                                    listOf(grid[y * 3 + 1][x * 3], grid[y * 3 + 1][x * 3 + 1], grid[y * 3 + 1][x * 3 + 2]),
                                    listOf(grid[y * 3 + 2][x * 3], grid[y * 3 + 2][x * 3 + 1], grid[y * 3 + 2][x * 3 + 2])
                            )
                        }

                val newSubGridSpec = rules[asRule(subGrid)]!!
                val newSubGrid = asGrid(newSubGridSpec)

                newSubGrid.forEachIndexed { i, subRow ->
                    newRows[i].addAll(subRow)
                }
            }

            newRows
        }.toList()
    }

    repeat(5) { grid = expandGrid(grid) }
    println(grid.map { it.filter { it == '#' }.size }.sum())


    repeat(13) { grid = expandGrid(grid) }
    println(grid.map { it.filter { it == '#' }.size }.sum())
}