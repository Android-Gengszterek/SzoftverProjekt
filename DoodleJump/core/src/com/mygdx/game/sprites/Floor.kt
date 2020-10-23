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

    constructor(){
        floorTexture = Texture("floor.png")

        floorPosition = Vector2(-floorTexture.width.toFloat(),-floorTexture.height/2f)
       // bounds = Rectangle(Gdx.graphics.height-(floorTexture.height/2f), 0f,Gdx.graphics.width.toFloat(), floorTexture.height.toFloat() )
        bounds = Rectangle(floorPosition.x, floorPosition.y,Gdx.graphics.width.toFloat()*2, floorTexture.height.toFloat() )

    }

    fun collide(bound: Rectangle):Boolean{
        return bound.overlaps(bounds)
    }

}