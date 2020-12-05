package com.mygdx.game.states

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.MyGdxGame
import com.mygdx.game.sprites.Score
import javax.xml.soap.Text

class MenuState(gameStateManager: GameStateManager, val scoreValue: Int) : State(gameStateManager) {

    private var replayButton: Texture
    private var exitButton: Texture
    private var backgroundImage: Texture
    private var backgroundImagePosition: Vector2
    private var newScore: Score
    private var centerX = Gdx.graphics.width/2
    private var centerY = Gdx.graphics.height/2
    private var myPreferences: Preferences

    init {

        replayButton = Texture("replay.png")
        exitButton = Texture("back.png")
        backgroundImage = Texture("background.png")
        backgroundImagePosition = Vector2(cam.position.x, cam.position.y)
        cam.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        newScore = Score(cam,200, Color.GREEN, 10f)

        myPreferences = Gdx.app.getPreferences("MyScores")
        val myScores = myPreferences.get()
        myPreferences.putInteger("${myScores.size}", scoreValue)
        myPreferences.flush()
        System.out.println("Score: $scoreValue")

    }

    override fun handleInput() {
        if (Gdx.input.isTouched){

            if (Gdx.input.x > centerX - 150 &&  Gdx.input.x < centerX + 150 && Gdx.input.y > centerY - 150 && Gdx.input.y < centerY + 150 ){
                gameStateManager.set(PlayState(gameStateManager))
            }

            if (Gdx.input.x > backgroundImagePosition.x + 100 &&
                    Gdx.input.x < backgroundImagePosition.x + 400 &&
                    Gdx.input.y > backgroundImagePosition.y + Gdx.graphics.height.toFloat() - 400) {
                Gdx.app.exit()
            }

        }

    }

    override fun update(dt: Float) {
        handleInput()
    }

    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = cam.combined
        spriteBatch.begin()
        spriteBatch.draw(backgroundImage, backgroundImagePosition.x, backgroundImagePosition.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(replayButton, backgroundImagePosition.x + (Gdx.graphics.width.toFloat() / 2 - 150), backgroundImagePosition.y + (Gdx.graphics.height.toFloat() / 2 - 150),
                300f, 300f)
        spriteBatch.draw(exitButton,backgroundImagePosition.x + 100f, backgroundImagePosition.y + 100f, 300f, 300f)
        newScore.bitmapFont.draw(spriteBatch, scoreValue.toString(), backgroundImagePosition.x + (Gdx.graphics.width.toFloat() / 2 - 100), backgroundImagePosition.y + Gdx.graphics.height.toFloat() - (centerY/2))


        spriteBatch.end()
    }

    override fun dispose() {
        backgroundImage.dispose()
        replayButton.dispose()
    }
}

