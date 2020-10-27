package com.mygdx.game

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.databinding.ActivityMainBinding
import com.mygdx.game.fragments.LoginFragment
import com.mygdx.game.fragments.MenuFragment

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var playButton: Button
    private lateinit var binding: ActivityMainBinding
    private lateinit var frameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
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
}