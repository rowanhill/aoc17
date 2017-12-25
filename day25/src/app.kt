import StateLabel.*

/*
Begin in state A.
Perform a diagnostic checksum after 12656374 steps.

In state A:
If the current value is 0:
- Write the value 1.
- Move one slot to the right.
- Continue with state B.
If the current value is 1:
- Write the value 0.
- Move one slot to the left.
- Continue with state C.

In state B:
If the current value is 0:
- Write the value 1.
- Move one slot to the left.
- Continue with state A.
If the current value is 1:
- Write the value 1.
- Move one slot to the left.
- Continue with state D.

In state C:
If the current value is 0:
- Write the value 1.
- Move one slot to the right.
- Continue with state D.
If the current value is 1:
- Write the value 0.
- Move one slot to the right.
- Continue with state C.

In state D:
If the current value is 0:
- Write the value 0.
- Move one slot to the left.
- Continue with state B.
If the current value is 1:
- Write the value 0.
- Move one slot to the right.
- Continue with state E.

In state E:
If the current value is 0:
- Write the value 1.
- Move one slot to the right.
- Continue with state C.
If the current value is 1:
- Write the value 1.
- Move one slot to the left.
- Continue with state F.

In state F:
If the current value is 0:
- Write the value 1.
- Move one slot to the left.
- Continue with state E.
If the current value is 1:
- Write the value 1.
- Move one slot to the right.
- Continue with state A.
 */

enum class StateLabel { A, B, C, D, E, F }
data class Action(val valToWrite: Int, val dx: Int, val nextState: StateLabel)
data class State(val label: StateLabel, val ifZero: Action, val ifOne: Action)

val states = mapOf(
        A to State(A, Action(1, 1, B), Action(0, -1, C)),
        B to State(B, Action(1, -1, A), Action(1, -1, D)),
        C to State(C, Action(1, 1, D), Action(0, 1, C)),
        D to State(D, Action(0, -1, B), Action(0, 1, E)),
        E to State(E, Action(1, 1, C), Action(1, -1, F)),
        F to State(F, Action(1, -1, E), Action(1, 1, A))
)
val diagnosticAfter = 12656374

class TuringMachine {
    private var state = states[A]!!
    private var numStepsTaken = 0
    private var cursor = 0
    private val cellsAtOne = HashSet<Int>()

    fun step() {
        val action = if (currentVal() == 0) state.ifZero else state.ifOne
        setCurrentVal(action.valToWrite)
        cursor += action.dx
        state = states[action.nextState]!!
        numStepsTaken++
    }

    fun numSetCells(): Int {
        return cellsAtOne.count()
    }

    private fun currentVal(): Int {
        return if (cellsAtOne.contains(cursor)) 1 else 0
    }

    private fun setCurrentVal(valToSet: Int) {
        when (valToSet) {
            0 -> cellsAtOne.remove(cursor)
            1 -> cellsAtOne.add(cursor)
            else -> throw Exception("Unexpected val to set: $valToSet")
        }
    }
}

fun part1() {
    val turingMachine = TuringMachine()
    repeat(diagnosticAfter) {
        turingMachine.step()
    }
    println(turingMachine.numSetCells())
}

fun main(args: Array<String>) {
    part1()
}