package com.mygdx.game.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class Player{
    companion object{
        private val GRAVITY:Float = -15f
    }
    var position: Vector3
    var velocity: Vector3
    var playerTexture: Texture
    var bounds: Rectangle

    constructor(x:Int, y:Int){
        position = Vector3(x.toFloat(),y.toFloat(),0f)
        velocity = Vector3(0f,0f,0f)
        playerTexture = Texture("player.png")
        bounds = Rectangle(position.x, position.y, playerTexture.width.toFloat(), playerTexture.height.toFloat())

    }

    fun update(dt:Float):Unit {
        velocity.add(0f,GRAVITY,0f)
        velocity.scl(dt)
        position.add(velocity.x,velocity.y,0f)

        velocity.scl(1/dt)
        bounds.setPosition(position.x,position.y)
    }

    fun goLeft():Unit{
        //velocity.add(-10f, 0f,0f)
        velocity.x = -400f
    }

    fun goRight():Unit{
        velocity.x = 400f
    }

    fun jump():Unit{
        velocity.y = 1200f
    }

    fun stop():Unit{
        velocity.x = 0f
    }



}