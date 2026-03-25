package com.leopr.protein_folding

import com.leopr.framework.Action
import com.leopr.framework.Problem
import com.leopr.framework.State

data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos) = Pos(x + other.x, y + other.y)
}

data class ProteinState(val positions: List<Pos>) : State

enum class Move(val d: Pos) : Action {
    UP(Pos(0, 1)),
    DOWN(Pos(0, -1)),
    LEFT(Pos(-1, 0)),
    RIGHT(Pos(1, 0))
}

class ProteinFoldingProblem(val protein: String) : Problem<ProteinState, Move>(
    ProteinState(listOf(Pos(0, 0)))
) {
    override fun isGoal(state: ProteinState): Boolean {
        return state.positions.size == protein.length
    }

    override fun getActions(state: ProteinState): List<Move> {
        val actions = mutableListOf<Move>()
        val lastPos = state.positions.last()
        val occupied = state.positions.toSet()

        if (state.positions.size == 1) {
            return listOf(Move.RIGHT)
        }

        for (move in Move.entries) {
            val newPos = lastPos + move.d
            if (!occupied.contains(newPos)) {
                actions.add(move)
            }
        }
        return actions
    }

    override fun getResult(state: ProteinState, action: Move): ProteinState {
        val newPos = state.positions.last() + action.d
        val newPositions = state.positions.toMutableList()
        newPositions.add(newPos)
        return ProteinState(newPositions)
    }

   override fun stepCost(state: ProteinState, action: Move): Double {
       val newPos = state.positions.last() + action.d
       val indexInProtein = state.positions.size
       val aminoAcid = protein[indexInProtein]

       if (aminoAcid == 'P') {
           return 3.0
       }

       var contacts = 0
       val prevPos = state.positions.last()

       for (i in 0 until state.positions.size - 1) {
           if (protein[i] == 'H') {
               val p = state.positions[i]
               val manhattan = (newPos.x - p.x).let { if (it < 0) -it else it } +
                              (newPos.y - p.y).let { if (it < 0) -it else it }
               if (manhattan == 1 && p != prevPos) {
                   contacts++
               }
           }
       }
       return 3.0 - contacts.toDouble()
   }

    override fun h(state: ProteinState): Double {
        var remainingP = 0
        for (i in state.positions.size until protein.length) {
            if (protein[i] == 'P') {
                remainingP++
            }
        }
        return remainingP * 3.0
    }
}
