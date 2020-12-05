package com.mygdx.game.states

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3

abstract class State{
    protected var cam: OrthographicCamera
    protected var mouse: Vector3
    protected var gameStateManager: GameStateManager


    protected constructor(gameStateManager: GameStateManager){
        this.gameStateManager = gameStateManager
        this.cam = OrthographicCamera()
        mouse = Vector3()
    }

    protected abstract fun handleInput():Unit
    public abstract fun update(dt:Float):Unit
    abstract fun render(spriteBatch: SpriteBatch): Unit
    abstract fun dispose(): Unit

}