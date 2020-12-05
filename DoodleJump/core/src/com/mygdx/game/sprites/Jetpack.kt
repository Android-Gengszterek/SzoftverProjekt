package com.mygdx.game.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Jetpack {
    var jetpackTexture: Texture
    var jetpackPosition: Vector2
    var bounds: Rectangle
    var width = 150f
    var height = 150f
    var basePlatform: Platform

    // init jetpack
    constructor(platform: Platform){
        basePlatform = platform
        jetpackTexture = Texture("jetpack.png")
        jetpackPosition = Vector2()
        jetpackPosition.x = platform.platformPosition.x + (platform.width/2) - (width/2)
        jetpackPosition.y = platform.platformPosition.y + platform.height
        bounds = Rectangle(jetpackPosition.x, jetpackPosition.y, width,height )
    }

    // returns true if the texture overlaps with the parameters bounds
    fun collide(bound: Rectangle):Boolean{
        return bounds.overlaps(bound)

    }

    fun update(){
        jetpackPosition.x = basePlatform.platformPosition.x + (basePlatform.width/2) - (width/2)
        jetpackPosition.y = basePlatform.platformPosition.y + basePlatform.height
        bounds = Rectangle(jetpackPosition.x, jetpackPosition.y, width,height )
    }
}