package com.leopr.framework

enum class AlgorithmType { BFS, DFS, MIN_COST, GREEDY, A_STAR }

class SearchEngine <S: State, A: Action>(val problem: Problem<S, A>, val type: AlgorithmType) {
    fun calculateF(s: S, g: Double): Double {
        return when (type) {
            AlgorithmType.MIN_COST -> g
            AlgorithmType.GREEDY -> problem.h(s)
            AlgorithmType.A_STAR -> g + problem.h(s)
            else -> 0.0
        }
    }

    fun solve(): SearchNode<S, A>? {
        val frontier = when (type) {
            AlgorithmType.BFS -> FIFOFrontier<S, A>()
            AlgorithmType.DFS -> LIFOFrontier<S, A>()
            else -> PriorityFrontier<S,A>()
        }

        val explored = mutableSetOf<S>()
        val initialF = calculateF(problem.initialState, 0.0)
        frontier.push(SearchNode(problem.initialState, evaluation = initialF))

        while (!frontier.isEmpty()) {
            val node = frontier.pop() ?: return null
            if (problem.isGoal(node.state)) {
                return node
            }
            explored.add(node.state)
            for (action in problem.getActions(node.state)) {
                val nextState = problem.getResult(node.state, action)
                if (explored.contains(nextState)) continue
                val g = node.pathCost + problem.stepCost(node.state, action)
                val f = calculateF(node.state, g)

                val child = SearchNode(node.state, action, node, g, f, node.depth + 1)
                frontier.push(child)
            }
        }
        return null
    }
}