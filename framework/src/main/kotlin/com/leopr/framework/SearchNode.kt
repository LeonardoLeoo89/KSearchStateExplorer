package com.leopr.framework


data class SearchNode<S: State, A: Action>(
    val state: S,
    val action: A? = null,
    val parent: SearchNode<S, A>? = null,
    val pathCost: Double = 0.0,
    val evaluation: Double = 0.0, // f(n) = g(n) + h(n)
    val depth: Int = 0
) : Comparable<SearchNode<S, A>> {
    override fun compareTo(other: SearchNode<S, A>): Int {
        return evaluation.compareTo(other.evaluation)
    }
    fun getPath(): List<SearchNode<S, A>> {
        val path = mutableListOf<SearchNode<S, A>>()
        var current : SearchNode<S, A> = this
        while (true) {
            val p = current.parent ?: break
            if (current.action != null) {
                path.add(0, current)
            }
            current = p
        }
        return path
    }
}


