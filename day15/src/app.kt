import kotlin.experimental.*

fun main(args: Array<String>) {
    val aSeed = 289L
    //val aSeed = 65L
    val bSeed = 629L
    //val bSeed = 8921L
    val mask = 0xFFFFL

    val aFactor = 16807
    val bFactor = 48271
    val divider = 2147483647

    run {
        var judgeCount = 0
        var a = aSeed
        var b = bSeed
        repeat(40_000_000) {
            a = (a * aFactor) % divider
            b = (b * bFactor) % divider

            if (a and mask == b and mask) {
                judgeCount++
            }
        }

        println(judgeCount)
    }

    // part 2
    run {
        var judgeCount = 0
        var a = aSeed
        var b = bSeed
        repeat(5_000_000) {
            do {
                a = (a * aFactor) % divider
            } while (a % 4 != 0L)
            do {
                b = (b * bFactor) % divider
            } while (b % 8 != 0L)

            if (a and mask == b and mask) {
                judgeCount++
            }
        }

        println(judgeCount)
    }
}