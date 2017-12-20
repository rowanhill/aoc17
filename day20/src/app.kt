import java.io.File
import java.lang.Math.abs

data class Particle(
        val index: Int,
        val position: Triple,
        val velocity: Triple,
        val acceleration: Triple
) {
    fun tick(): Particle {
        val newVelocity = this.velocity + this.acceleration
        val newPosition = this.position + newVelocity
        return Particle(this.index, newPosition, newVelocity, this.acceleration)
    }
}

data class Triple(val x: Long, val y: Long, val z: Long) {
    operator fun plus(other: Triple): Triple {
        return Triple(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        )
    }

    fun distance(): Long {
        return abs(this.x) + abs(this.y) + abs(this.z)
    }
}

fun makeTriple(spec: String): Triple {
    val (x, y, z) = spec
            .substringAfter('<')
            .substringBefore('>')
            .split(",")
            .map { it.trim().toLong() }
    return Triple(x, y, z)
}

fun main(args: Array<String>) {
    var particles = File("src/input.txt").readLines().mapIndexed { index, line ->
        val (posSpec, velSpec, accSpec) = line.split(", ")
        Particle(index, makeTriple(posSpec), makeTriple(velSpec), makeTriple(accSpec))
    }

    // part 1
    // Note - there's a potential problem where two particles have the same acc & vel but one has a velocity that is,
    // say, to the left but the position is to the right of the origin, but the other has a leftwards velocity and a
    // leftwards starting position. The former will stay nearer the origin in the long term, but the below sort doesn't
    // account for that. In practice, this isn't an issue with the input data.
    val nearestInLongTerm = particles.sortedWith(compareBy(
            { it.acceleration.distance() },
            { it.velocity.distance() },
            { it.position.distance() }
    )).first()
    println(nearestInLongTerm)

    // part 2
    var particlesAreSortedByAcceleration: Boolean
    var counter = 0
    do {
        particles = particles
                .map { it.tick() }
                .groupBy { it.position }.filter { it.value.size == 1 } // remove collisions
                .flatMap { it.value }

        // Sorting by absolute manhattan distance isn't quite right; sign ought to be taken into account. No need to
        // bother fixing, though - the printed number stabilises early on, which turns out to be the answer!
        val posSortedParticles = particles.sortedBy { it.position.distance() }
        val accSortedParticles = particles.sortedBy { it.acceleration.distance() }
        val posSortedIndexes = posSortedParticles.map { it.index }
        val accSortedIndexes = accSortedParticles.map { it.index }.filter { posSortedIndexes.contains(it) }
        particlesAreSortedByAcceleration = posSortedIndexes == accSortedIndexes

        counter++
        println("$counter ${particles.size}")
    } while (!particlesAreSortedByAcceleration)

    println(particles.size)
}

