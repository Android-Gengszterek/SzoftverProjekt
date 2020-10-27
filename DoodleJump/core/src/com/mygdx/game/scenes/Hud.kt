package com.mygdx.game.scenes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.mygdx.game.MyGdxGame

class Hud {
    var stage: Stage
    var viewport: Viewport
    var score: Int

    private var fontGenerator: FreeTypeFontGenerator
    private var fontParameter: FreeTypeFontGenerator.FreeTypeFontParameter
    private var bitmapFont: BitmapFont
    var spriteBatch: SpriteBatch

    constructor(){
        spriteBatch = SpriteBatch()
        score = 0.toInt()
        viewport = FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), OrthographicCamera())
        stage = Stage(viewport, spriteBatch)

        var table: Table = Table()
        table.top()
        table.setFillParent(true)

        fontGenerator = FreeTypeFontGenerator(Gdx.files.internal("VCRFont.ttf"))
        fontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParameter.size = 150
        fontParameter.borderWidth = 5f
        fontParameter .borderColor = Color.BLACK
        fontParameter.color = Color.GREEN
        bitmapFont = fontGenerator.generateFont(fontParameter)

        bitmapFont.draw(spriteBatch, "sadasdsadas", Gdx.graphics.width.toFloat()/2 - 200f, Gdx.graphics.height-100f)

        //table.add(bitmapFont).expandX().padTop(600f)

        stage.addActor(table)
    }
}