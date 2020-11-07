package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.states.GameStateManager
import com.mygdx.game.states.PlayState

class MyGdxGame : ApplicationAdapter() {

    companion object{
        var WIDTH = 480
        var HEIGHT = 800
        var TITLE = "DOODLE JUMP"
    }

    private lateinit var gameStateManager: GameStateManager
    private lateinit var spriteBatch: SpriteBatch

    override fun create() {
        spriteBatch = SpriteBatch()
        gameStateManager = GameStateManager()
        gameStateManager.push(PlayState(gameStateManager))

        if(Gdx.input.isTouched){
            Gdx.app.log("Debug", "Touched!")
        }

        //Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameStateManager.update(Gdx.graphics.deltaTime)
        gameStateManager.render(spriteBatch)
    }

    override fun dispose() {
       // System.out.println("Score MyGdxGame: ${myPreferences.getInteger("newscore")}")
    }
}