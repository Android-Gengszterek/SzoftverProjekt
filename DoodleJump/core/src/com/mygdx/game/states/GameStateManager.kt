package com.mygdx.game.states

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.util.*

class GameStateManager{
    private lateinit var states: Stack<State>

    constructor(){
        states = Stack<State>();
    }

    fun push(state: State): Unit{
        states.push(state)
    }

    fun pop():Unit{
        states.pop()
    }

    fun set(state:State){
        states.pop()
        states.push(state)
    }

    fun update(dt:Float):Unit{
        states.peek().update(dt)
    }

    fun render(spriteBatch: SpriteBatch):Unit{
        states.peek().render(spriteBatch)
    }
}