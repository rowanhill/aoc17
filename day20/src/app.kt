import java.io.File
import java.lang.Math.abs
import kotlin.math.sqrt

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
    companion object {
        fun parse(spec: String): Triple {
            val (x, y, z) = spec
                    .substringAfter('<')
                    .substringBefore('>')
                    .split(",")
                    .map { it.trim().toLong() }
            return Triple(x, y, z)
        }
    }

    operator fun plus(other: Triple): Triple {
        return Triple(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        )
    }

    fun manhattanDistance(): Long {
        return abs(this.x) + abs(this.y) + abs(this.z)
    }

    fun linearDistance(): Double {
        return sqrt(this.x*this.x + this.y*this.y + this.z*this.z.toDouble())
    }

    /*
    Returns a distinct integer for the "direction class" of the vector - in loose terms, the quadrant of space the
    vector is in
     */
    fun directionClass(): Int {
        fun signToInt(x: Long): Int {
            if (x < 0) {
                return 1
            } else if (x == 0L) {
                return 2
            } else {
                return 3
            }
        }

        return signToInt(this.x)*3 + signToInt(this.y)*5 + signToInt(this.z)*7
    }
}

fun main(args: Array<String>) {
    var particles = File("src/input.txt").readLines().mapIndexed { index, line ->
        val (posSpec, velSpec, accSpec) = line.split(", ")
        Particle(index, Triple.parse(posSpec), Triple.parse(velSpec), Triple.parse(accSpec))
    }

    // part 1
    // Note - there's a potential problem where two particles have the same acc & vel but one has a velocity that is,
    // say, to the left but the position is to the right of the origin, but the other has a leftwards velocity and a
    // leftwards starting position. The former will stay nearer the origin in the long term, but the below sort doesn't
    // account for that. In practice, this isn't an issue with the input data.
    val nearestInLongTerm = particles.sortedWith(compareBy(
            { it.acceleration.manhattanDistance() },
            { it.velocity.manhattanDistance() },
            { it.position.manhattanDistance() }
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

        // Order particles by the acceleration's direction class - the quadrant they'll end up in eventually - followed
        // by the position's direction class - where it is now - and then the linear distance from the origin
        val posSortedParticles = particles.sortedWith(compareBy(
                { it.acceleration.directionClass() },
                { it.position.directionClass() },
                { it.position.linearDistance() }
        ))
        // Order the particles by the acceleration's direction class - the quadrant they'll end up in eventually -
        // followed by linear size of acceleration
        val accSortedParticles = particles.sortedWith(compareBy(
                { it.acceleration.directionClass() },
                { it.acceleration.linearDistance() }
        ))
        val posSortedIndexes = posSortedParticles.map { it.index }
        val accSortedIndexes = accSortedParticles.map { it.index }
        // If all the particles are in the quadrants they'll eventually end up in, and those with the bigger
        // accelerations have overtaken those with smaller accelerations, then there can be no future collisions
        particlesAreSortedByAcceleration = posSortedIndexes == accSortedIndexes

        counter++
        if (counter % 100 == 0) println("$counter ${particles.size}")

        // Note: The above sorting doesn't seem to succeed in any reasonable time - even after ~10,000 ticks, there are
        // a couple of particles not quite ordered (and there'll probably be overflow issues not long after that).
        // Instead, we just give up after a while - in practice, there have been no collisions for a long time by this
        // point, so we've likely found the right answer.
        if (counter > 2500) {
            println("Giving up trying to particles in the right order")
            break
        }
    } while (!particlesAreSortedByAcceleration)

    println(particles.size)
}

