package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image

class Floor{
    var floorTexture: Texture
    var floorPosition: Vector2

    constructor(){
        floorTexture = Texture("floor.png")

        floorPosition = Vector2(-floorTexture.width.toFloat(),-floorTexture.height/2f)
    }


}