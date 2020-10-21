package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.MyGdxGame
import com.mygdx.game.sprites.Floor

class PlayState(gameStateManager: GameStateManager): State(gameStateManager) {
    private lateinit var backgroundImage: Texture
    private lateinit var floor: Floor

    init {
        backgroundImage = Texture("background.png")
        floor = Floor()
        cam.setToOrtho(false,Gdx.graphics.width.toFloat()/2, Gdx.graphics.height.toFloat()/2)
    }
    override fun handleInput() {
    }

    override fun update(dt: Float) {
    }

    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.begin()
        spriteBatch.draw(backgroundImage,0f,0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(floor.floorTexture,floor.floorPosition.x,floor.floorPosition.y,Gdx.graphics.width+2*(-floor.floorPosition.x),500f)
        spriteBatch.end()
    }

}