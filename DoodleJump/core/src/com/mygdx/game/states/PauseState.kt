package com.mygdx.game.states

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Vector2

class PauseState(cam: OrthographicCamera) {

    private var backgroundImage: Texture
    private var backButton: Texture
    private var backgroundImagePosition: Vector2
    private var cam:OrthographicCamera
    private var bottomLeftPoint: Vector2
    var isGyroscopeActive = false
    var offsetOffText = 0f
    var offsetOnText = Gdx.graphics.width.toFloat()
    var informationOffset = Gdx.graphics.width.toFloat()

    private var fontGenerator: FreeTypeFontGenerator
    private var fontParameter: FreeTypeFontGenerator.FreeTypeFontParameter
    var bitmapFont: BitmapFont
    var bitmapFont2: BitmapFont
    var bitmapFont3: BitmapFont
    var buttonBackground: Texture
    var informationTexture: Texture
    private var nextButton: Texture
    private var exampleImage: Texture
    var isInformationsShown = false

    init {
        this.cam = cam
        backButton = Texture("circled-left.png")
        backgroundImage = Texture("background.png")
        buttonBackground = Texture("button_background.png")
        informationTexture = Texture("useful_informations.png")
        nextButton = Texture("nextbutton.png")
        exampleImage = Texture("example.png")
        bottomLeftPoint = Vector2(cam.position.x, cam.viewportHeight)
        backgroundImagePosition = Vector2(cam.position.x, cam.position.y)
        cam.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("PixelMechaBold.ttf"))
        fontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParameter.size = 120
        fontParameter.borderWidth = 5f
        fontParameter .borderColor = Color.BLACK
        fontParameter.color = Color.GRAY
        bitmapFont = fontGenerator.generateFont(fontParameter)
        fontParameter.size = 80
        bitmapFont2 = fontGenerator.generateFont(fontParameter)
        fontParameter.color = Color.GREEN
        bitmapFont3 = fontGenerator.generateFont(fontParameter)


    }


    fun render(spriteBatch: SpriteBatch) {
        spriteBatch.draw(backgroundImage, bottomLeftPoint.x, bottomLeftPoint.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(backButton, 0f, bottomLeftPoint.y + Gdx.graphics.height - 200f, 200f, 200f)
        bitmapFont.draw(spriteBatch, "PAUSED", getPositionOffset(bitmapFont,"PAUSED"), bottomLeftPoint.y + Gdx.graphics.height - 50f)
        bitmapFont3.draw(spriteBatch, "Switch to gyroscope", 50f, bottomLeftPoint.y + Gdx.graphics.height - 350f)

        spriteBatch.draw(buttonBackground, (Gdx.graphics.width/2 - getTextWidth(bitmapFont,"OFF")), bottomLeftPoint.y + Gdx.graphics.height - 680f, 2*getTextWidth(bitmapFont,"OFF"), 200f)
        bitmapFont2.draw(spriteBatch, "Off", Gdx.graphics.width/2 - getTextWidth(bitmapFont,"OFF")/3 + offsetOffText, bottomLeftPoint.y + Gdx.graphics.height - 550f)

        bitmapFont3.draw(spriteBatch, "On", Gdx.graphics.width/2 - getTextWidth(bitmapFont,"ON")/3 + offsetOnText, bottomLeftPoint.y + Gdx.graphics.height - 550f)

        bitmapFont3.draw(spriteBatch, "Controls:", bottomLeftPoint.x + 50f, bottomLeftPoint.y + Gdx.graphics.height/2+200f)

        spriteBatch.draw(exampleImage, 50f, bottomLeftPoint.y + 150f, 500f, 800f)
        bitmapFont3.draw(spriteBatch, "Pause", bottomLeftPoint.x + 580f, bottomLeftPoint.y + Gdx.graphics.height/2+50f)
        bitmapFont3.draw(spriteBatch, "Shoot", bottomLeftPoint.x + 580f, bottomLeftPoint.y + Gdx.graphics.height/2-130f)
        bitmapFont3.draw(spriteBatch, "Move left", bottomLeftPoint.x + 580f, bottomLeftPoint.y + Gdx.graphics.height/2-330f)
        bitmapFont3.draw(spriteBatch, "Move right", bottomLeftPoint.x + 580f, bottomLeftPoint.y + Gdx.graphics.height/2-580f)

        bitmapFont2.draw(spriteBatch, "Go to informations", bottomLeftPoint.x + 150f, bottomLeftPoint.y + 100f)
        spriteBatch.draw(nextButton, bottomLeftPoint.x+Gdx.graphics.width-150f, bottomLeftPoint.y, 150f, 150f)

        spriteBatch.draw(informationTexture, bottomLeftPoint.x + informationOffset, bottomLeftPoint.y, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        spriteBatch.draw(backButton, 0f, bottomLeftPoint.y + Gdx.graphics.height - 200f, 200f, 200f)
    }

    fun showInformations(){
        informationOffset = 0f
        isInformationsShown = true
    }
    fun hideInformations(){
        informationOffset = Gdx.graphics.width.toFloat()
        isInformationsShown = false
    }

    fun dispose() {
        backgroundImage.dispose()
        backButton.dispose()
        buttonBackground.dispose()
        nextButton.dispose()
        informationTexture.dispose()
    }

    fun updatePauseState(camPositionX: Float, camPositionY: Float){
        bottomLeftPoint.x = camPositionX - Gdx.graphics.width/2
        bottomLeftPoint.y = camPositionY - Gdx.graphics.height/2
        if(isGyroscopeActive){
            offsetOnText = 0f
            offsetOffText = Gdx.graphics.width.toFloat()
        }else{
            offsetOnText = Gdx.graphics.width.toFloat()
            offsetOffText = 0f
        }

    }

    private fun getPositionOffset(bitmapFont: BitmapFont, value: String): Float {
        val glyphLayout = GlyphLayout()
        glyphLayout.setText(bitmapFont, value)
        return bottomLeftPoint.x + (Gdx.graphics.width / 2) - (glyphLayout.width/2)
    }

    private fun getTextWidth(bitmapFont: BitmapFont, value: String): Float {
        val glyphLayout = GlyphLayout()
        glyphLayout.setText(bitmapFont, value)
        return glyphLayout.width
    }

}

