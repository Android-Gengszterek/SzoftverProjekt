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
    var isWood: Boolean

    companion object{
        private val GRAVITY:Float = -15f
    }

    constructor(y: Float,texture_picture: String, isWood: Boolean){
        velocity = Vector2(0f,0f)
        greenPlatformTexture = Texture(texture_picture)
        rand = Random()
        var randNumber:Float = rand.nextFloat() * (Gdx.graphics.width-500f) + 200f

        platformPosition = Vector2(randNumber , y)

        bounds = Rectangle(platformPosition.x, platformPosition.y,200f, 60f )
        this.isWood = isWood
    }

    fun reposition(y:Float):Unit{
        platformPosition.set(rand.nextFloat() * (Gdx.graphics.width-500f) + 200f,y)
        bounds.set(platformPosition.x, platformPosition.y,200f,60f)
    }

    fun repositionWoodenAlongX(y:Float):Unit{
        platformPosition.set(rand.nextFloat() * (Gdx.graphics.width-500f) + 200f,y)
        bounds.set(platformPosition.x, platformPosition.y,200f,60f)
    }


    fun collide(bound: Rectangle):Boolean{
        var position: Vector2 = Vector2()
        bound.getPosition(position)
        if((position.y > platformPosition.y) && (position.y < platformPosition.y + 20) && (position.x > (platformPosition.x-250f)) && (position.x < platformPosition.x+60f)){
            return true
        }
        return false
    }


    fun platformFall(){
        platformPosition.add(0f,-45f)
        bounds.setPosition(platformPosition.x,platformPosition.y)
    }
}