package com.mygdx.game

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.mygdx.game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var playButton: Button
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        playButton = binding.button
    }

    fun playButtonPressed(view: View){
        val intent = Intent(this, AndroidLauncher::class.java)
        startActivity(intent)
    }
}