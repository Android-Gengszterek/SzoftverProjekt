package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2

class PauseButton {

    private var fontGenerator: FreeTypeFontGenerator
    private var fontParameter: FreeTypeFontGenerator.FreeTypeFontParameter
    var bitmapFont: BitmapFont
    var pausePosition: Vector2
    var cam: OrthographicCamera

    // creating pause button font parameters
    constructor(cam: OrthographicCamera, size: Int = 100, color: Color = Color.RED, borderWidth: Float = 5f){
        this.cam = cam

        // initializing the font style and position
        fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("PixelMechaBold.ttf"))
        fontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParameter.size = size
        fontParameter.borderWidth = borderWidth
        fontParameter .borderColor = Color.RED
        fontParameter.color = color
        fontParameter.spaceX = 20
        bitmapFont = fontGenerator.generateFont(fontParameter)
        pausePosition = Vector2(Gdx.graphics.width - 100f , cam.position.y + (Gdx.graphics.height/2)-50f )
    }

    // the score's position will always stay at the top of the screen
    fun updateButtonPosition():Unit{
        pausePosition.y = cam.position.y + (Gdx.graphics.height/2)-50f
    }

}