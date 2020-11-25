package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import kotlin.math.floor

class Floor{
    var floorTexture: Texture
    var floorPosition: Vector2
    var bounds: Rectangle

    // initializing floor
    constructor(){
        floorTexture = Texture("floor.png")
        floorPosition = Vector2(-floorTexture.width.toFloat(),-floorTexture.height/2f)
        bounds = Rectangle(floorPosition.x, floorPosition.y,Gdx.graphics.width.toFloat()*2, floorTexture.height.toFloat() )

    }

    // returns true if the texture overlaps with the parameters bounds
    fun collide(bound: Rectangle):Boolean{
        return bound.overlaps(bounds)
    }

}