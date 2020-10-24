package com.mygdx.game

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.mygdx.game.databinding.ActivityMainBinding
import com.mygdx.game.fragments.LoginFragment
import com.mygdx.game.fragments.MenuFragment

class MainActivity : AppCompatActivity() {

    private lateinit var playButton: Button
    private lateinit var binding: ActivityMainBinding
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        playButton = binding.button
        frameLayout = binding.fragmentContainer

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = MenuFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    fun playButtonPressed(view: View){
        val intent = Intent(this, AndroidLauncher::class.java)
        startActivity(intent)
    }

    fun loginButtonPressed(){
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, LoginFragment()).commit()
    }
}