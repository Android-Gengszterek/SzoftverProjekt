package com.mygdx.game

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration


class AndroidLauncher : AndroidApplication() {
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(MyGdxGame(), config)
    }
}