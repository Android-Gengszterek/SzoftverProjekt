package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class Player{
    companion object{
        private var GRAVITY:Float = -15f
    }
    var position: Vector3
    var velocity: Vector3
    var playerTexture: Texture
    var bounds: Rectangle
    var isFalling: Boolean
    var jumpSound: Sound
    var speedUpValue: Float
    var gravityChange:Float
    var turnSpeed: Float
    var width: Float = 200f
    var height: Float = 200f
    var isImmune: Boolean = false

    constructor(x:Int, y:Int){
        position = Vector3(x.toFloat(),y.toFloat(),0f)
        velocity = Vector3(0f,0f,0f)
        playerTexture = Texture("player.png")
        bounds = Rectangle(position.x+120f, position.y, width-70f, height)
        isFalling = true
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump-arcade.mp3"))
        speedUpValue = 0f
        gravityChange = 0f
        turnSpeed = 0f
    }

    fun update(dt:Float):Unit {
        // the character will fall with a scaling GRAVITY
        velocity.add(0f,GRAVITY + gravityChange,0f)
        velocity.scl(dt)
        // the new velocities will be added to the player
        position.add(velocity.x,velocity.y,0f)

        //if the character goes out in the left of the screen it will be replaced in the right side of the screen
        if(position.x+100f < 0f){
            position.x = Gdx.graphics.width.toFloat()
        }
        // if the character goes out in the right side of the screen it will be replaced in the left side of the screen
        if(position.x > Gdx.graphics.width.toFloat()){
            position.x = 0f
        }
        // if the character's velocity is less then 0 it is falling
        if(velocity.y < 0){
            isFalling = true
            if(isImmune) {
                isImmune = false
            }
        }

        velocity.scl(1/dt)
        // the bound of the player is following the character
        bounds.setPosition(position.x,position.y)
    }

    fun goLeft():Unit{
        velocity.x = -400f - turnSpeed
        if(!isImmune) {
            playerTexture = Texture("player.png")
        }
    }

    fun goRight():Unit{
        velocity.x = 400f + turnSpeed
        if(!isImmune) {
            playerTexture = Texture("player2.png")
        }
    }

    fun jump():Unit{
        jumpSound.play()
        velocity.y = 1200f + speedUpValue
        isFalling = false
    }

    fun highJump():Unit{
        velocity.y = 2500f + speedUpValue
        isFalling = false
    }

    fun fly(){
        velocity.y = 7500f + speedUpValue
        playerTexture = Texture("playerjetpack.png")
        isFalling = false
        isImmune = true
    }

    fun stop():Unit{
        velocity.x = 0f
    }

    fun stopFall():Unit{
        velocity.y = -GRAVITY
    }

    fun speedUp():Unit{
        if(speedUpValue < 500f) {
            speedUpValue += 2.5f
            gravityChange -= 0.1f
            if(turnSpeed < 450f){
                turnSpeed += 5f
            }
        }

    }




}