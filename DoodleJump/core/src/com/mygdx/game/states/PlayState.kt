package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.MyGdxGame
import com.mygdx.game.sprites.Floor
import com.mygdx.game.sprites.GreenPlatform
import com.mygdx.game.sprites.Player
import java.util.*
import kotlin.collections.ArrayList


class PlayState(gameStateManager: GameStateManager): State(gameStateManager){
    companion object{
        private var PLATFORM_SPACING:Float = 500f
        private var PLATFORM_COUNT:Int = 4
        private var PLATFORM_HEIGHT:Float = 60f
    }
    private lateinit var backgroundImage: Texture
    private lateinit var backgroundImage2: Texture
    private lateinit var floor: Floor
    private lateinit var player: Player
    private lateinit var rand: Random
    private var platforms: java.util.ArrayList<GreenPlatform>
    lateinit var backgroundImagePosition: Vector2
    lateinit var backgroundImage2Position: Vector2

    init {
        backgroundImage = Texture("background.png")
        backgroundImagePosition = Vector2(cam.position.x-cam.viewportWidth/2,0f)
        backgroundImage2 = Texture("background.png")
        backgroundImage2Position = Vector2(cam.position.x-cam.viewportWidth/2,Gdx.graphics.height.toFloat())

        floor = Floor()
        player = Player(500,100)
        platforms = ArrayList<GreenPlatform>()

        for(i in 0..PLATFORM_COUNT){
            rand = Random()
            if(i * PLATFORM_SPACING + PLATFORM_HEIGHT > floor.floorPosition.y + floor.floorTexture.height) {
                platforms.add(GreenPlatform(i * (PLATFORM_SPACING + PLATFORM_HEIGHT)))
            }
           /* if(i == rand.nextInt(PLATFORM_COUNT)){
                platforms.add(GreenPlatform(i * PLATFORM_SPACING + 60f))
            }*/
        }
        cam.setToOrtho(false,Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

    }

    override fun handleInput() {
        if(Gdx.input.justTouched()) {
            if(player.position.y < 400f){
                player.jump()
            }
            if (Gdx.input.x < Gdx.graphics.width / 2) {
                Gdx.app.log("Debug", "Touched!");
                player.goLeft()
                //player.jump()
            }
            if (Gdx.input.x > Gdx.graphics.width / 2) {
                Gdx.app.log("Debug", "Touched!");
                player.goRight()
                //player.jump()
            }
        }
        if(!Gdx.input.isTouched()){
            player.stop()
        }


    }

    override fun update(dt: Float) {
        handleInput()
        updateBackground()
        player.update(dt)
        if(player.position.y > Gdx.graphics.height/2) {
            cam.position.y = player.position.y
        }

        if(floor.collide(player.bounds)){
            //player.jump()
            player.stopFall()
        }


        for(platform in platforms){
            if((player.isFalling) && platform.collide(player.bounds)){
                player.jump()
            }
            if(player.position.y-(cam.viewportHeight/2)  > platform.platformPosition.y + PLATFORM_HEIGHT){
                platform.reposition(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING + PLATFORM_HEIGHT)))
            }
        }
        cam.update()

    }



    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = cam.combined
        spriteBatch.begin()
        spriteBatch.draw(backgroundImage,backgroundImagePosition.x,backgroundImagePosition.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(backgroundImage2,backgroundImage2Position.x,backgroundImage2Position.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(floor.floorTexture,0f-(Gdx.graphics.width/2),floor.floorPosition.y,Gdx.graphics.width.toFloat()*2,500f)
        spriteBatch.draw(player.playerTexture, player.position.x, player.position.y,300f,300f)

        //spriteBatch.draw(platforms[0].greenPlatformTexture, platforms[0].platformPosition.x,platforms[0].platformPosition.y,200f,60f)
        for(platform in platforms){
            spriteBatch.draw(platform.greenPlatformTexture, platform.platformPosition.x,platform.platformPosition.y,200f,60f)
        }
        spriteBatch.end()
    }

    override fun dispose() {
        backgroundImage.dispose()
    }

    fun updateBackground():Unit{
        if(player.position.y-(cam.viewportHeight)   > backgroundImagePosition.y + (Gdx.graphics.height.toFloat()/2)){
            backgroundImagePosition.add(0f,Gdx.graphics.height.toFloat()*2)
        }
        if(player.position.y-(cam.viewportHeight)    > backgroundImage2Position.y+ (Gdx.graphics.height.toFloat()/2)){
            backgroundImage2Position.add(0f,Gdx.graphics.height.toFloat()*2)
        }
    }



}