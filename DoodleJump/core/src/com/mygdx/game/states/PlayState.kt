package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.sprites.Floor
import com.mygdx.game.sprites.Platform
import com.mygdx.game.sprites.Player
import com.mygdx.game.sprites.Score
import java.util.*
import kotlin.collections.ArrayList


class PlayState(gameStateManager: GameStateManager): State(gameStateManager){
    companion object{
        private var PLATFORM_SPACING:Float = 500f
        private var PLATFORM_COUNT:Int = 4
        private var PLATFORM_HEIGHT:Float = 60f
    }
    private var backgroundImage: Texture
    private var backgroundImage2: Texture
    private var floor: Floor
    private var player: Player
    private lateinit var rand: Random
    private var platforms: java.util.ArrayList<Platform>
    var backgroundImagePosition: Vector2
    var backgroundImage2Position: Vector2
    var score:Score

    //initializing attributes
    init {
        backgroundImage = Texture("background.png")
        backgroundImagePosition = Vector2(cam.position.x - cam.viewportWidth / 2, 0f)
        backgroundImage2 = Texture("background.png")
        backgroundImage2Position = Vector2(cam.position.x - cam.viewportWidth / 2, Gdx.graphics.height.toFloat())

        floor = Floor()
        player = Player(Gdx.graphics.width / 2 - 200, 150)
        score = Score(cam)
        platforms = ArrayList<Platform>()

        rand = Random()
        var randNumber = rand.nextFloat() * PLATFORM_COUNT
        // placing platforms randomly
        var platformType:String
        for(i in 0..PLATFORM_COUNT){
            if (i * PLATFORM_SPACING + PLATFORM_HEIGHT > floor.floorPosition.y + floor.floorTexture.height) {
                platforms.add(Platform(i * (PLATFORM_SPACING + PLATFORM_HEIGHT), "grassplatform.png",false))
            }
        }
        if (randNumber * PLATFORM_SPACING + PLATFORM_HEIGHT > floor.floorPosition.y + floor.floorTexture.height) {
            platforms.add(Platform(randNumber * (PLATFORM_SPACING + PLATFORM_HEIGHT), "woodplatform.png",true))
        }


        // setting the camera position
        cam.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())




    }

    override fun handleInput() {
        // if the player touches the screen
        if(Gdx.input.justTouched()) {
            // if the players position is low, with a tap can start the jumping
            if(player.position.y < 400f){
                player.jump()
            }
            //if the player touches the left side of the screen the character goes left
            if (Gdx.input.x < Gdx.graphics.width / 2) {
                player.goLeft()
            }
            // if the player touches the right side of the screen the character goes right
            if (Gdx.input.x > Gdx.graphics.width / 2) {
                player.goRight()
            }
        }
        //if the player does not touch the screen then the character will stop moving in the X axis
        if(!Gdx.input.isTouched()){
            player.stop()
        }


    }

    override fun update(dt: Float) {

        // in every update we handle the input and update the background and the players position if it is necessary
        updateScore()
        handleInput()
        updateBackground()
        player.update(dt)
        cam.update()
        score.updateScorePosition()

        // if the player falls below the camera's y position then the game ends and a new state will start
        if((cam.position.y > Gdx.graphics.height) && (player.position.y < (cam.position.y - Gdx.graphics.height/2))){
            gameStateManager.pop()
            gameStateManager.push(PlayState(gameStateManager))
        }

        // if the players y position is greater than the height of the camera, then the camera will follow the player
        if(player.position.y > Gdx.graphics.height/2 && (player.position.y > cam.position.y)) {
            cam.position.y = player.position.y
        }

        // if the player will get contact with the ground at the beginning of the game the player will stop on the ground
        if(floor.collide(player.bounds)){
            player.stopFall()
        }

        for(platform in platforms){
            // if the platform isnt wood platform
            if(!platform.isWood) {
                // if the player is falling and touches any of the platforms then the character will jump
                if ((player.isFalling) && platform.collide(player.bounds)) {
                    player.jump()
                }
                // if the platforms are below the camera's viewport then the platforms will be replaced above
                if (player.position.y - (cam.viewportHeight / 2) > platform.platformPosition.y + PLATFORM_HEIGHT) {
                    platform.reposition(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING + PLATFORM_HEIGHT)))
                }
            } else{
                // if the player touches the wood platform he will fall with the wood platform
                if ((player.isFalling) && platform.collide(player.bounds)) {
                    platform.platformFall()
                }
                // every 50 score the wooden platform will be replaced checking itt will not collide with any other platforms
                if (( score.score % 50 == 0) && (player.position.y - (cam.viewportHeight / 2) > platform.platformPosition.y + PLATFORM_HEIGHT)) {
                    platform.reposition(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING + PLATFORM_HEIGHT)) + (backgroundImage.height/2))
                    do{
                        var collideWithOthers = false
                        platform.repositionWoodenAlongX(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING + PLATFORM_HEIGHT))+ (backgroundImage.height/2))

                        for(greenPlatform in platforms){
                            if(!greenPlatform.isWood && platform.collide(greenPlatform.bounds)){
                                collideWithOthers = true
                            }
                        }
                    }while(collideWithOthers)


                }
            }
        }
    }


    // in the render we draw every element on the screen
    override fun render(spriteBatch: SpriteBatch) {

        spriteBatch.projectionMatrix = cam.combined
        spriteBatch.begin()

        spriteBatch.draw(backgroundImage, backgroundImagePosition.x, backgroundImagePosition.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(backgroundImage2, backgroundImage2Position.x, backgroundImage2Position.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(floor.floorTexture, 0f - (Gdx.graphics.width / 2), floor.floorPosition.y, Gdx.graphics.width.toFloat() * 2, 500f)


        for(platform in platforms){
            if(!platform.isWood) {
                spriteBatch.draw(platform.greenPlatformTexture, platform.platformPosition.x, platform.platformPosition.y, 200f, 60f)
            } else{
                spriteBatch.draw(platform.greenPlatformTexture, platform.platformPosition.x, platform.platformPosition.y, 200f, 200f)
            }
        }
        spriteBatch.draw(player.playerTexture, player.position.x, player.position.y, 300f, 300f)

        score.bitmapFont.draw(spriteBatch, score.score.toString(), score.scorePosition.x, score.scorePosition.y)

        spriteBatch.end()
    }

    // if the state will be removed then we clear the textures
    override fun dispose() {
        backgroundImage.dispose()
        backgroundImage2.dispose()
        floor.floorTexture.dispose()
        player.playerTexture.dispose()
        for(platform in platforms){
            platform.greenPlatformTexture.dispose()
        }
    }

    // if the camera's position is greater than the height of the background image, the background image will be placed at the top
    fun updateBackground():Unit{
        if(player.position.y-(cam.viewportHeight)   > backgroundImagePosition.y + (Gdx.graphics.height.toFloat()/2)){
            backgroundImagePosition.add(0f, Gdx.graphics.height.toFloat() * 2)
        }
        if(player.position.y-(cam.viewportHeight)    > backgroundImage2Position.y+ (Gdx.graphics.height.toFloat()/2)){
            backgroundImage2Position.add(0f, Gdx.graphics.height.toFloat() * 2)
        }
    }

    // every 100 pixel the player gets 1 point
    fun updateScore(){
        if(player.position.y.toInt() / 100 > score.score) {
            score.score = player.position.y.toInt() / 100
        }
    }


}