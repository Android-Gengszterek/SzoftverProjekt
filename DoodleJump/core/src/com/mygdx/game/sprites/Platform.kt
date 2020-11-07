package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*

class Platform {
    var greenPlatformTexture: Texture
    var platformPosition: Vector2
    private lateinit var rand: Random
    var bounds: Rectangle
    var velocity: Vector2
    var fallSpeed:Float
    var isWood: Boolean
    var moveSpeed:Float
    var isMooving: Boolean
    var uniqueSpacingMultiplier: Float = 500f
    var width: Float = 150f
    var height: Float = 40f


    constructor(y: Float,texture_picture: String, isWood: Boolean){
        velocity = Vector2(0f,0f)
        greenPlatformTexture = Texture(texture_picture)
        rand = Random()
        var randNumber:Float = rand.nextFloat() * (Gdx.graphics.width-500f) + width

        platformPosition = Vector2(randNumber , y)

        bounds = Rectangle(platformPosition.x, platformPosition.y,width, height/2 )
        this.isWood = isWood
        isMooving = false
        fallSpeed = 0f
        moveSpeed = 200f

    }

    fun reposition(y:Float):Unit{
        platformPosition.set(rand.nextFloat() * (Gdx.graphics.width-500f) + width,y)
        bounds.set(platformPosition.x, platformPosition.y,width,height)
    }

    fun repositionWoodenAlongX(y:Float):Unit{
        platformPosition.set(rand.nextFloat() * (Gdx.graphics.width-500f) + width,y)
        bounds.set(platformPosition.x, platformPosition.y,width,height)
    }


    fun collide(bound: Rectangle):Boolean{
        var position: Vector2 = Vector2()
        bound.getPosition(position)
        if((position.y > platformPosition.y) && (position.y < platformPosition.y + 20) && (position.x > (platformPosition.x-270f)) && (position.x < platformPosition.x+60f)){
            return true
        }
        return false
    }


    fun platformFall(){
        if(isWood) {
            fallSpeed = -1600f
        }
    }

    fun stopFall(){
        fallSpeed = 0f
    }

    fun turnLeft(){
        moveSpeed = -200f
    }

    fun turnRight(){
        moveSpeed = 200f
    }

    fun startMove(){
        isMooving = true
    }

    fun update(dt:Float){
        if(isWood) {
            velocity.add(0f, fallSpeed)
            velocity.scl(dt)
            platformPosition.add(0f, velocity.y)
            velocity.scl(1 / dt)
            bounds.setPosition(platformPosition.x, platformPosition.y)
            velocity.add(0f,-fallSpeed)
        } else if(isMooving){
            velocity.set(moveSpeed, 0f)
            velocity.scl(dt)
            platformPosition.add(velocity.x, 0f)
            velocity.scl(1 / dt)
            bounds.setPosition(platformPosition.x, platformPosition.y)
        }
    }
}