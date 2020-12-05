package com.mygdx.game.sprites

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Spring {
    var springTexture: Texture
    var springPosition: Vector2
    var bounds: Rectangle
    var width = 50f
    var height = 50f
    var basePlatform: Platform


    constructor(platform: Platform){
        basePlatform = platform
        springTexture = Texture("spring.png")
        springPosition = Vector2()
        springPosition.x = platform.platformPosition.x + (platform.width/2) - (width/2)
        springPosition.y = platform.platformPosition.y + platform.height
        bounds = Rectangle(springPosition.x, springPosition.y, width,height )
    }

    // returns true if the texture overlaps with the parameters bounds
    fun collide(bound: Rectangle):Boolean{
        return bounds.overlaps(bound)

    }

    // spring will be replaced in its platform
    fun update(){
        springPosition.x = basePlatform.platformPosition.x + (basePlatform.width/2) - (width/2)
        springPosition.y = basePlatform.platformPosition.y + basePlatform.height
        bounds = Rectangle(springPosition.x, springPosition.y, width,height )
    }
}