package com.mygdx.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.mygdx.game.MyGdxGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = MyGdxGame.WIDTH
        config.height = MyGdxGame.HEIGHT
        config.title = MyGdxGame.TITLE
        LwjglApplication(MyGdxGame(), config)
    }
}