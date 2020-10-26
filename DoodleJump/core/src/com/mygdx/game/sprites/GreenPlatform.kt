package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import java.util.*

class GreenPlatform {
    var greenPlatformTexture: Texture
    var platformPosition: Vector2
    private lateinit var rand: Random
    var bounds: Rectangle

    constructor(y: Float){
        greenPlatformTexture = Texture("grassplatform.png")
        rand = Random()
        var randNumber:Float = rand.nextFloat() * (Gdx.graphics.width-500f) + 200f

        platformPosition = Vector2(randNumber , y)

        bounds = Rectangle(platformPosition.x, platformPosition.y,200f, 60f )
    }

    fun reposition(y:Float):Unit{
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
}