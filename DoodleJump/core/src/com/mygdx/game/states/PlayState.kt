package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.sprites.*
import java.util.*
import kotlin.collections.ArrayList


class PlayState(gameStateManager: GameStateManager): State(gameStateManager){
    companion object{
        private var PLATFORM_SPACING:Float = 500f
        private var PLATFORM_COUNT:Int = 10
        private var PLATFORM_HEIGHT:Float = 60f
        private var bulletIndex: Int = 0
        private var BULLET_COUNT = 5
        private var GAME_PLAY = 1
        private var GAME_PAUSE = 0
    }
    private var backgroundImage: Texture
    private var backgroundImage2: Texture
    private var bullets: ArrayList<Bullet>
    private var floor: Floor
    private var player: Player
    private var rand: Random
    private var platforms: java.util.ArrayList<Platform>
    private var platformsDoubler: java.util.ArrayList<Platform>
   // private var deactivated: ArrayList<Boolean>
    private var monster: Monster
    private var spring: Spring
    private var jetpack: Jetpack
    var backgroundImagePosition: Vector2
    private var backgroundImage2Position: Vector2
    private var score: Score
    private var pause: PauseButton
    private var music: Music
    var allPlatforms:List<Platform>
    var isShooting: Boolean = false
    var gameState = GAME_PLAY
    var pauseState: PauseState
    var released = true

    //initializing attributes
    init {
        backgroundImage = Texture("background.png")
        backgroundImagePosition = Vector2(cam.position.x - cam.viewportWidth / 2, 0f)
        backgroundImage2 = Texture("background.png")
        backgroundImage2Position = Vector2(cam.position.x - cam.viewportWidth / 2, Gdx.graphics.height.toFloat())
        music = Gdx.audio.newMusic(Gdx.files.internal("African Safari Loop.wav"))
        music.volume = 0.3f
        music.isLooping = true
        music.play()
        floor = Floor()
        player = Player(Gdx.graphics.width / 2 - 200, 150)
        score = Score(cam)
        pause = PauseButton(cam)
        platforms = ArrayList<Platform>()
        platformsDoubler = ArrayList<Platform>()
        bullets = ArrayList()
        monster = Monster()
        pauseState = PauseState(cam)

        for (i in 0..BULLET_COUNT){
            bullets.add(Bullet())
        }

        rand = Random()
        val randNumber = rand.nextFloat() * PLATFORM_COUNT
        // placing platforms randomly
        for(i in 0..PLATFORM_COUNT){
            if (i * PLATFORM_SPACING + PLATFORM_HEIGHT > floor.floorPosition.y + floor.floorTexture.height) {
                val uniqueSpacingMultiplier = rand.nextFloat()
                //Adding new platform
                platforms.add(Platform(i * (PLATFORM_SPACING + PLATFORM_HEIGHT), "grassplatform.png",false))
                // Add a new platform above the platfrom
                platformsDoubler.add(Platform(i * (PLATFORM_SPACING * uniqueSpacingMultiplier + PLATFORM_HEIGHT), "grassplatform.png",false))
                //If the other platfrom collides with the previous platform it will be replaced
                while(platforms[platforms.size-1].collide(platformsDoubler[platformsDoubler.size-1].bounds) ){
                    platformsDoubler[platformsDoubler.size-1].repositionWoodenAlongX(platformsDoubler[platformsDoubler.size-1].platformPosition.y)
                }
                platformsDoubler[i-1].uniqueSpacingMultiplier = uniqueSpacingMultiplier
            }
        }
        // setting the wooden platform
        if (randNumber * PLATFORM_SPACING + PLATFORM_HEIGHT > floor.floorPosition.y + floor.floorTexture.height) {
            platforms.add(Platform(randNumber * (PLATFORM_SPACING + PLATFORM_HEIGHT), "woodplatform.png",true))
        }
        // putting the spring on the 2th platform and the jetpack on the 9th
        spring = Spring(platforms[2])
        jetpack = Jetpack(platforms[9])

        // setting the camera position
        cam.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        // adding all the platforms into one to handle collisions easier
        allPlatforms = platforms + platformsDoubler
    }

    override fun handleInput() {

        // if we are in the play state
        if(gameState == GAME_PLAY) {
            // if the player activated the control by gyroscope
            if(pauseState.isGyroscopeActive ){
                // the character goes on the direction of the tilting
                if (Gdx.input.accelerometerX > 1) {
                    player.goStrongLeft()
                }else if (Gdx.input.accelerometerX < -1) {
                    player.goStrongRight()
                }
                // if the position of the telephone is close to the horizontal(in portraid mode) then the character stops
                if(Gdx.input.accelerometerX < 0.5 && Gdx.input.accelerometerX > -0.5){
                    player.stop()
                }
            }
            // if the player touches the screen
            if (Gdx.input.justTouched()) {
                // if the players position is low, with a tap can start the jumping
                if (player.position.y < 400f) {
                    player.jump()
                }
                if(!pauseState.isGyroscopeActive) {
                    //if the player touches the left side of the screen the character goes left
                    if ((Gdx.input.x < Gdx.graphics.width / 2) && (Gdx.input.y > Gdx.graphics.height / 2)) {
                        player.goLeft()
                    }
                    // if the player touches the right side of the screen the character goes right
                    if ((Gdx.input.x > Gdx.graphics.width / 2) && (Gdx.input.y > Gdx.graphics.height / 2)) {
                        player.goRight()
                    }
                }
            }

            //if the player does not touch the screen then the character will stop moving in the X axis
            if (!Gdx.input.isTouched()) {
                if(!pauseState.isGyroscopeActive) {
                    player.stop()
                }
                // if the character was shooting we change the texture back
                if (isShooting) {
                    isShooting = false
                    player.playerTexture = Texture("player.png")
                }
            } else {
                // if the player touches te top side of the screen then the character will shoot and change texture
                if (Gdx.input.y < Gdx.graphics.height / 2 && Gdx.input.y > 200 && !isShooting && !player.isImmune) {
                    player.playerTexture = Texture("player_shooting.png")
                    isShooting = true
                    // reset the bullets location at the player
                    bullets[bulletIndex].reset(player)
                    bulletIndex = (bulletIndex + 1) % BULLET_COUNT
                }

                // if the player clicks on the pause button the pause menu will be shown
                if (Gdx.input.y < 200 && Gdx.input.x > Gdx.graphics.width - 150f) {
                    gameState = GAME_PAUSE
                }
            }
            // if the pause state is active
        } else if(gameState == GAME_PAUSE){
            // with the back button the player can resume the game
            if (Gdx.input.y < 200 && Gdx.input.x < 150f && Gdx.input.isTouched()) {
                // if the player touches the resume button the game will continue
                if(!pauseState.isInformationsShown && released){
                    gameState = GAME_PLAY
                    released = false
                    // if the player touches the back button in the informations it will go back to the pause page
                } else{
                    pauseState.hideInformations()
                    released = false
                }
            }

            // if the player touches the right bottom of the screen in the pause state it will go to the informations
            if (Gdx.input.y > Gdx.graphics.height-200 && Gdx.input.x > Gdx.graphics.width - 150f) {
                pauseState.showInformations()
            }
            // if the player clicks on the switch button he can activate is deactivate the gyroscope controls
            if (Gdx.input.isTouched() && released && (Gdx.input.x < (Gdx.graphics.width/2+300f)) && (Gdx.input.x > (Gdx.graphics.width/2-300f))
                    && Gdx.input.y > 450 && Gdx.input.y < 650) {
                pauseState.isGyroscopeActive = !pauseState.isGyroscopeActive
                released = false
            }
            if(!Gdx.input.isTouched()){
                released = true
            }
        }
    }

    override fun update(dt: Float) {
        if(gameState == GAME_PLAY) {
            // in every update we handle the input and update the background and the players position if it is necessary
            updateScore()
            handleInput()
            updateBackground()
            updateSpeed()
            player.update(dt)
            cam.update()
            score.updateScorePosition()
            pause.updateButtonPosition()

            // if the character jumps on the jetpack
            if (jetpack.collide(player.bounds)) {
                player.fly()
            }

            // at every 150 score the spring will be reset
            if (score.score % 150 == 0) {
                spring.update()
            }

            // if the character jumps on the spring it will jump high
            if ((player.isFalling) && spring.collide(player.bounds)) {
                player.highJump()
            }

            // after 500 score the monsters begin to move
            if (score.score > 500) {
                monster.update(dt)
            }

            //if the monster reaches the left side of the screen turns right
            if (monster.monsterPosition.x < 0) monster.turnRight()
            // if the monster reaches the right side of the screen turns left
            if (monster.monsterPosition.x + monster.bounds.width > Gdx.graphics.width) monster.turnLeft()

            // if the monster is below the camera view we replace it
            if (monster.monsterPosition.y < player.position.y - Gdx.graphics.height) monster.replaceMonster()

            // we reposition the bullets if needed
            for (bullet in bullets) {
                bullet.update(dt, player)
            }

            // wooden platform updating
            platforms[platforms.size - 1].update(dt,cam,player)


            for (bullet in bullets) {
                // if the bullet is off the screen itt will be replaced at the side of the screen out of view
                if (bullet.bulletPosition.y > player.position.y + Gdx.graphics.height) {
                    bullet.hideBullet()
                }

                if (bullet.collide(monster.bounds)) {
                    monster.replaceMonster()
                }
            }

            // If the player reaches the score a certain platform will move and a certain platform will be deleted
            when (score.score) {
                100 -> makeMoveAndReduceSize(platforms[0], platformsDoubler)
                500 -> makeMoveAndReduceSize(platforms[2], platformsDoubler)
                1000 -> makeMoveAndReduceSize(platforms[6], platformsDoubler)
                1500 -> makeMoveAndReduceSize(platforms[9], platformsDoubler)
                2000 -> makeMoveAndReduceSize(platforms[5], platformsDoubler)
                2500 -> makeMoveAndReduceSize(platforms[3], platformsDoubler)
                3000 -> makeMoveAndReduceSize(platforms[4], platformsDoubler)
                3500 -> makeMoveAndReduceSize(platforms[7], platformsDoubler)
                4000 -> makeMoveAndReduceSize(platforms[1], platformsDoubler)
                4500 -> makeMoveAndReduceSize(platforms[8], platformsDoubler)
            }

            //If the platform is moving it will be redirected at the borders
            for (platform in platforms) {
                platform.update(dt,cam,player);
            }

            // if the player falls below the camera's y position then the game ends and a new state will start
            if (((cam.position.y > Gdx.graphics.height) && (player.position.y < (cam.position.y - Gdx.graphics.height / 2))) || monster.collide(player)) {
                gameStateManager.pop()
                // We put menu state in the gamestatemanager
                music.stop()
                gameStateManager.push(MenuState(gameStateManager, score.score))
            }

            // if the players y position is greater than the height of the camera, then the camera will follow the player
            if (player.position.y > Gdx.graphics.height / 2 && (player.position.y > cam.position.y)) {
                cam.position.y = player.position.y
            }

            // if the player will get contact with the ground at the beginning of the game the player will stop on the ground
            if (floor.collide(player.bounds)) {
                player.stopFall()
            }

            for (platform in platforms) {
                // if the platform isnt wood platform
                if (!platform.isWood) {
                    // if the player is falling and touches any of the platforms then the character will jump
                    if ((player.isFalling) && platform.collide(player.bounds)) {
                        player.jump()
                    }
                    // if the platforms are below the camera's viewport then the platforms will be replaced above
                    if (player.position.y - (cam.viewportHeight / 2) > platform.platformPosition.y + PLATFORM_HEIGHT) {
                        platform.reposition(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING + PLATFORM_HEIGHT)))
                    }
                } else {
                    // if the player touches the wood platform he will fall with the wood platform
                    if ((player.isFalling) && platform.collide(player.bounds)) {
                        platform.platformFall()
                    }
                    // every 50 score the wooden platform will be replaced checking itt will not collide with any other platforms
                    if ((score.score % 50 == 0) && (player.position.y - (cam.viewportHeight / 2) > platform.platformPosition.y + PLATFORM_HEIGHT)) {
                        platform.reposition(player.position.y + (Gdx.graphics.height / 2))
                        repositionIfCollideWithOtherPlatforms(platform, allPlatforms, false)
                    }
                }
            }
            for (platform in platformsDoubler) {
                // if the player is falling and touches any of the platforms then the character will jump
                if ((player.isFalling) && platform.collide(player.bounds)) {
                    player.jump()
                }
                // if the platforms are below the camera's viewport then the platforms will be replaced above
                if (player.position.y - (Gdx.graphics.height / 2) > platform.platformPosition.y + PLATFORM_HEIGHT) {
                    //platform.reposition(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING * (1+platform.uniqueSpacingMultiplier) + PLATFORM_HEIGHT)))
                    repositionIfCollideWithOtherPlatforms(platform, allPlatforms, true)
                }
            }
        } else if(gameState == GAME_PAUSE){
            handleInput()
            pauseState.updatePauseState(cam.position.x, cam.position.y)
        }
    }


    // in the render we draw every element on the screen
    override fun render(spriteBatch: SpriteBatch) {
        spriteBatch.projectionMatrix = cam.combined
        spriteBatch.begin()
        // if the play state is active
        if(gameState == GAME_PLAY) {
            // we draw the backgrounds
            spriteBatch.draw(backgroundImage, backgroundImagePosition.x, backgroundImagePosition.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            spriteBatch.draw(backgroundImage2, backgroundImage2Position.x, backgroundImage2Position.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

            // draw the floor
            spriteBatch.draw(floor.floorTexture, 0f - (Gdx.graphics.width / 2), floor.floorPosition.y, Gdx.graphics.width.toFloat() * 2, 500f)

            //draw the platforms
            for (platform in platforms) {
                if (!platform.isWood) {
                    spriteBatch.draw(platform.greenPlatformTexture, platform.platformPosition.x, platform.platformPosition.y, platform.width, platform.height)
                } else {
                    spriteBatch.draw(platform.greenPlatformTexture, platform.platformPosition.x, platform.platformPosition.y, platform.width, 200f)
                }
            }

            // draw the platforms that will be deleted later
            for (platform in platformsDoubler) {
                spriteBatch.draw(platform.greenPlatformTexture, platform.platformPosition.x, platform.platformPosition.y, platform.width, platform.height)
            }

            //drawing spring
            spriteBatch.draw(spring.springTexture, spring.springPosition.x, spring.springPosition.y, spring.width, spring.height)

            // drawing jetpack
            spriteBatch.draw(jetpack.jetpackTexture, jetpack.jetpackPosition.x, jetpack.jetpackPosition.y, jetpack.width, jetpack.height)

            // drawing character
            spriteBatch.draw(player.playerTexture, player.position.x, player.position.y, player.width, player.height)

            // drawing score
            score.bitmapFont.draw(spriteBatch, score.score.toString(), score.scorePosition.x, score.scorePosition.y)

            // drawing pause button
            pause.bitmapFont.draw(spriteBatch, "II", pause.pausePosition.x, pause.pausePosition.y)

            // drawing bullets
            for (bullet in bullets) {
                spriteBatch.draw(bullet.bulletTexture, bullet.bulletPosition.x, bullet.bulletPosition.y, 50f, 50f)
            }

            // drawing monster
            spriteBatch.draw(monster.monsterTexture, monster.monsterPosition.x, monster.monsterPosition.y, monster.width, monster.height)
        }
        // if we are in the pause state we call the pause state's draw
        if(gameState == GAME_PAUSE) {
            pauseState.render(spriteBatch)
        }

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
        for(platform in platformsDoubler){
            platform.greenPlatformTexture.dispose()
        }
        monster.monsterTexture.dispose()
        spring.springTexture.dispose()
        jetpack.jetpackTexture.dispose()
        pauseState.dispose()
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

    fun updateSpeed(){
        if(score.score % 100 == 0){
            player.speedUp()
        }
    }

    //If the platform collides with any of the platforms in the list it will be repositioned
    fun repositionIfCollideWithOtherPlatforms(platform: Platform, platforms: List<Platform>, isDoubler:Boolean){
        do{
            var collideWithOthers = false
            if(!isDoubler) {
                platform.repositionWoodenAlongX(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING + PLATFORM_HEIGHT)) + (backgroundImage.height / 2))
            }else{
                platform.reposition(platform.platformPosition.y + (PLATFORM_COUNT * (PLATFORM_SPACING * (1+platform.uniqueSpacingMultiplier) + PLATFORM_HEIGHT)))
            }
            for(greenPlatform in platforms){
                if(!greenPlatform.isWood && platform.collide(greenPlatform.bounds)){
                    collideWithOthers = true
                }
            }
        }while(collideWithOthers)
    }

    //This function make a platform move and remove another platfrom from the doublers
    fun makeMoveAndReduceSize(platform: Platform, platformDoubler:ArrayList<Platform>){
        platform.startMove()
        //if(!doublersDeactivated[platformDoubler.size-1]) {
        if(!platformDoubler[platformDoubler.size-1].deactivated) {
            platformDoubler.remove(platformDoubler[platformDoubler.size - 1])
            //doublersDeactivated[platformDoubler.size-1] = true
            platformDoubler[platformDoubler.size-1].deactivated = true
        }
    }


}