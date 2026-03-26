package com.leopr.framework

class SearchRunner<S: State, A: Action>(
    private val problem: Problem<S, A>,
    private val algorithms: List<AlgorithmType>,
    private val isExhaustive: Boolean = false,
    private val solutionPrinter: ((S) -> Unit)? = null
) {
    fun executeAll() {
        for (algo in algorithms) {
            println("=== $algo ===")
            val engine = SearchEngine(problem, algo)
            
            val startTime = System.nanoTime()
            val result = engine.solve(isExhaustive)
            val endTime = System.nanoTime()

            val durationMs = (endTime - startTime) / 1_000_000.0
            val formattedTime = String.format(java.util.Locale.US, "%.3f", durationMs)

            if (result != null) {
                println("Found solution in $formattedTime ms")
                println("Path cost according to engine: ${result.pathCost}")
                println("Depth: ${result.depth}")
                println("Visited Nodes: ${engine.visitedNodes}")

                solutionPrinter?.invoke(result.state)
            } else {
                println("Failed to find a solution in $formattedTime ms. Visited Nodes: ${engine.visitedNodes}")
            }
            println("-".repeat(50))
        }
    }
}
