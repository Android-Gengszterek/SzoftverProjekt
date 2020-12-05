package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.GL20
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
    private lateinit var myPreferences: Preferences

    override fun create() {
        spriteBatch = SpriteBatch()
        gameStateManager = GameStateManager()
        gameStateManager.push(PlayState(gameStateManager))
        myPreferences = Gdx.app.getPreferences("MyScores")

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
        val scoreKeys = myPreferences.get()
        var scoreString = ""
        scoreKeys.forEach {
            val score = myPreferences.getInteger(it.key)
            scoreString = "$scoreString $score"
        }
        Gdx.app.log("Game", scoreString)
        Gdx.files.local("scores.txt").apply {
            writeString( scoreString, false)
        }
        myPreferences.clear()
        myPreferences.flush()
    }
}