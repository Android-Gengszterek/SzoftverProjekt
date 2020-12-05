package com.mygdx.game.sprites

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2

class Score {

    private var fontGenerator: FreeTypeFontGenerator
    private var fontParameter: FreeTypeFontGenerator.FreeTypeFontParameter
    var bitmapFont: BitmapFont
    var score: Int
    var scorePosition: Vector2
    var cam: OrthographicCamera

    constructor(cam: OrthographicCamera, size: Int = 100, color: Color = Color.GRAY, borderWidth: Float = 5f){
        this.cam = cam

        // initializing the font style and position
        fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("PixelMechaBold.ttf"))
        fontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParameter.size = size
        fontParameter.borderWidth = borderWidth
        fontParameter .borderColor = Color.BLACK
        fontParameter.color = color
        bitmapFont = fontGenerator.generateFont(fontParameter)
        score = 0
        scorePosition = Vector2(50f , cam.position.y + (Gdx.graphics.height/2)-50f )
    }

    // the score's position will always stay at the top of the screen
    fun updateScorePosition():Unit{
        scorePosition.y = cam.position.y + (Gdx.graphics.height/2)-50f
    }

}