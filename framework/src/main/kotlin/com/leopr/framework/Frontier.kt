package com.leopr.framework

import java.util.PriorityQueue


interface Frontier<S: State, A: Action> {
    fun push(node: SearchNode<S, A>)
    fun pop(): SearchNode<S, A>?
    fun isEmpty(): Boolean
    fun containsState(state: S): Boolean
}

class FIFOFrontier<S: State, A: Action> : Frontier<S, A> {
    private val queue = ArrayDeque<SearchNode<S, A>>()
    private val states = HashSet<S>() // this for performance
    override fun push(node: SearchNode<S, A>) {
        queue.add(node)
        states.add(node.state)
    }

    override fun pop(): SearchNode<S, A>? {
        return queue.removeFirstOrNull().also { states.remove(it?.state) }
    }

    override fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    override fun containsState(state: S): Boolean {
        return states.contains(state)
    }
}

class LIFOFrontier<S: State, A: Action> : Frontier<S, A> {
    private val queue = ArrayDeque<SearchNode<S, A>>()
    private val states = HashSet<S>() // this for performance
    override fun push(node: SearchNode<S, A>) {
        queue.add(node)
    }
    override fun pop(): SearchNode<S, A>? {
        return queue.removeLastOrNull().also { states.remove(it?.state) }
    }
    override fun isEmpty(): Boolean {
        return queue.isEmpty()
    }
    override fun containsState(state: S): Boolean {
        return states.contains(state)
    }
}

class PriorityFrontier<S: State, A: Action> : Frontier<S, A> {
    private val pQueue = PriorityQueue<SearchNode<S, A>>();
    private val states = mutableMapOf<S, Double>()

    override fun push(node: SearchNode<S, A>) {
        val existingF = states[node.state]
        if (existingF == null || existingF > node.evaluation) {
            states[node.state] = node.evaluation
            pQueue.add(node)
        }

    }

    override fun pop(): SearchNode<S, A>? {
        return pQueue.poll().also { states.remove(it.state) }
    }

    override fun isEmpty(): Boolean {
        return pQueue.isEmpty()
    }

    override fun containsState(state: S): Boolean {
        return states.containsKey(state)
    }


}
