package com.leopr.puzzle

import com.leopr.framework.Action
import com.leopr.framework.State

class State: State {
    board: List<Int>
}

enum class Action : Action { UP, DOWN, LEFT, RIGHT }
