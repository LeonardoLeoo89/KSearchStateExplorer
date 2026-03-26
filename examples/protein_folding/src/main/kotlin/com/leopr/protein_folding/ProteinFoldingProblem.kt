package com.leopr.protein_folding

import com.leopr.framework.Action
import com.leopr.framework.Problem
import com.leopr.framework.State

data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos) = Pos(x + other.x, y + other.y)
}

data class ProteinState(val positions: List<Pos>, val contacts: Int = 0) : State

enum class Move(val d: Pos) : Action {
    UP(Pos(0, 1)),
    DOWN(Pos(0, -1)),
    LEFT(Pos(-1, 0)),
    RIGHT(Pos(1, 0))
}

class ProteinFoldingProblem(val protein: String) : Problem<ProteinState, Move>(
    ProteinState(listOf(Pos(0, 0)), 0)
) {
    private val unplaced = IntArray(protein.length + 1)
    private val evenMaxTotal: Int
    private val oddMaxTotal: Int

    init {
        var even = 0
        var odd = 0
        for (i in protein.length - 1 downTo 0) {
            // capacità di quanti contatti può fare l'i-esimo aminoacido
            var cap = 0
            if (protein[i] == 'H') {
                // 3 contatti se si trova all'estremo, due altrimenti
                cap = if (i == 0 || i == protein.length - 1) 3 else 2
                if (i % 2 == 0) even += cap else odd += cap
            }
            unplaced[i] = unplaced[i + 1] + cap
        }
        evenMaxTotal = even
        oddMaxTotal = odd
    }

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
        
        var newContacts = 0
        val index = state.positions.size
        if (protein[index] == 'H') {
            for (i in 0 until index - 1) { // exclude directly preceding
                if (protein[i] == 'H') {
                    val p = state.positions[i]
                    val manhattan = (newPos.x - p.x).let { if (it < 0) -it else it } +
                                    (newPos.y - p.y).let { if (it < 0) -it else it }
                    if (manhattan == 1) {
                        newContacts++
                    }
                }
            }
        }
        
        return ProteinState(newPositions, state.contacts + newContacts)
    }

    override fun stepCost(state: ProteinState, action: Move): Double {
        val index = state.positions.size
        val aminoAcid = protein[index]
        if (aminoAcid == 'P') return 3.0
        
        val newPos = state.positions.last() + action.d
        var newContacts = 0
        for (i in 0 until index - 1) { // exclude directly preceding
            if (protein[i] == 'H') {
                val p = state.positions[i]
                val manhattan = (newPos.x - p.x).let { if (it < 0) -it else it } +
                                (newPos.y - p.y).let { if (it < 0) -it else it }
                if (manhattan == 1) {
                    newContacts++
                }
            }
        }
        return 3.0 - newContacts.toDouble()
    }

    override fun h(state: ProteinState): Double {
        val currentSize = state.positions.size
        if (currentSize == protein.length) return 0.0

        val maxNewEven = evenMaxTotal - state.contacts
        val maxNewOdd = oddMaxTotal - state.contacts
        val unplacedSum = unplaced[currentSize]
        
        val maxNewContacts = minOf(maxNewEven, maxNewOdd, unplacedSum)

        val remainingSteps = protein.length - currentSize
        return maxOf(0.0, remainingSteps * 3.0 - maxNewContacts)
    }
}
