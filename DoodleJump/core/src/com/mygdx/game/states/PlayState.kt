package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.MyGdxGame
import com.mygdx.game.sprites.Floor
import com.mygdx.game.sprites.Player

class PlayState(gameStateManager: GameStateManager): State(gameStateManager){
    private lateinit var backgroundImage: Texture
    private lateinit var floor: Floor
    private lateinit var player: Player

    init {
        backgroundImage = Texture("background.png")
        floor = Floor()
        player = Player(500,1000)
        cam.setToOrtho(false,Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    }

    override fun handleInput() {
        if(Gdx.input.justTouched()) {
            if (Gdx.input.x < Gdx.graphics.width / 2) {
                Gdx.app.log("Debug", "Touched!");
                player.goLeft()
            }
            if (Gdx.input.x > Gdx.graphics.width / 2) {
                Gdx.app.log("Debug", "Touched!");
                player.goRight()
            }
        }
        if(!Gdx.input.isTouched()){
            player.stop()
        }


    }

    override fun update(dt: Float) {
        handleInput()
        player.update(dt)

        if(floor.collide(player.bounds)){
            player.jump()
        }
    }



    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = cam.combined
        spriteBatch.begin()
        spriteBatch.draw(backgroundImage,0f,0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(floor.floorTexture,floor.floorPosition.x,floor.floorPosition.y,Gdx.graphics.width.toFloat()*2,500f)
        spriteBatch.draw(player.playerTexture, player.position.x, player.position.y,300f,300f)
        spriteBatch.end()
    }

    override fun dispose() {
        backgroundImage.dispose()
    }



}