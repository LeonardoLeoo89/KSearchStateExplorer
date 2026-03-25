package com.leopr.framework

enum class AlgorithmType { BFS, DFS, MIN_COST, GREEDY, A_STAR, ITERATIVE_DEEPENING }

class SearchEngine <S: State, A: Action>(val problem: Problem<S, A>, val type: AlgorithmType) {
    fun calculateF(s: S, g: Double): Double {
        return when (type) {
            AlgorithmType.MIN_COST -> g
            AlgorithmType.GREEDY -> problem.h(s)
            AlgorithmType.A_STAR -> g + problem.h(s)
            else -> 0.0
        }
    }

    private fun expand(node: SearchNode<S, A>): List<SearchNode<S, A>> {
        return problem.getActions(node.state).map { action ->
            val nextState = problem.getResult(node.state, action)
            val g = node.pathCost + problem.stepCost(node.state, action)
            val f = calculateF(nextState, g)
            SearchNode(nextState, action, node, g, f, node.depth + 1)
        }
    }

    fun solve(): SearchNode<S, A>? {
        if (type == AlgorithmType.ITERATIVE_DEEPENING) {
            val initialNode = SearchNode<S, A>(problem.initialState)
            
            for (limit in 0..Int.MAX_VALUE) {
                var cutoff = false
                val pathStates = mutableSetOf<S>()

                fun recursiveDLS(node: SearchNode<S, A>): SearchNode<S, A>? {
                    if (problem.isGoal(node.state)) return node
                    if (node.depth >= limit) {
                        cutoff = true
                        return null
                    }
                    
                    pathStates.add(node.state)
                    
                    for (child in expand(node)) {
                        if (pathStates.contains(child.state)) continue
                        val result = recursiveDLS(child)
                        if (result != null) return result
                    }
                    
                    pathStates.remove(node.state)
                    return null
                }

                val result = recursiveDLS(initialNode)
                if (result != null) return result
                if (!cutoff) return null // The search space was fully explored without hitting the depth limit
            }
            return null
        }

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
            for (child in expand(node)) {
                if (explored.contains(child.state)) continue
                frontier.push(child)
            }
        }
        return null
    }
}