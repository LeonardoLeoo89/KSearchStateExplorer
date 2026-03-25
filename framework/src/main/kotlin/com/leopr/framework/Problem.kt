package com.leopr.framework

abstract class Problem<S: State, A: Action>(
    val initialState: S
) {
    abstract fun isGoal(state: S): Boolean
    abstract fun getActions(state: S): List<A>
    abstract fun getResult(state: S, action: A): S
    open fun stepCost(state: S, action: A): Double = 1.0;
    open fun h(state: S): Double = 0.0;
}
