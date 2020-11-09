package com.mygdx.game

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.badlogic.gdx.Gdx
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.classes.USER_CLASS
import com.mygdx.game.databinding.ActivityMainBinding
import com.mygdx.game.fragments.LOGIN_TAG
import com.mygdx.game.fragments.MenuFragment
import com.mygdx.game.fragments.UserFragment
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.AccessController.getContext


const val MAIN_TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var playButton: Button
    private lateinit var binding: ActivityMainBinding
    private lateinit var frameLayout: FrameLayout
    private lateinit var sp: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Firebase.database.reference
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        playButton = binding.button
        frameLayout = binding.fragmentContainer
        sp = getSharedPreferences("login", MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        Log.d("Auto", "Logged " + sp.getBoolean("logged", false))
        Log.d("Auto", "Logged " + sp.getString("userKey", ""))

        if (!sp.getBoolean("logged", false)) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, MenuFragment()).commit()
        }
        else {
            val userFragment = UserFragment()
            val bundle = Bundle()
            bundle.putSerializable(USER_CLASS, sp.getString("userKey", ""))
            userFragment.arguments = bundle
            Toast.makeText(this, "Auto Login", Toast.LENGTH_SHORT).show()
            supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    userFragment,
                    LOGIN_TAG
            ).commit()
        }
    }

    fun playButtonPressed(view: View){
        val intent = Intent(this, AndroidLauncher::class.java)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(MAIN_TAG, "Game exited, onRestart")
        if (Gdx.files?.local("scores.txt") != null) {
            val file =   Gdx.files.local("scores.txt")
            Log.d(MAIN_TAG, file.readString())
        }
        else {
            Log.d(MAIN_TAG, "Unable to read scores.txt")
        }
    }

}


