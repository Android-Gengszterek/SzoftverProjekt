package com.mygdx.game.ui.activitys

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.data.classes.USER_CLASS
import com.mygdx.game.databinding.ActivityMainBinding
import com.mygdx.game.ui.constants.Key
import com.mygdx.game.ui.constants.ToastMessage
import com.mygdx.game.ui.fragments.MenuFragment
import com.mygdx.game.ui.fragments.UserFragment


const val MAIN_TAG = "MainActivity"
const val SHARED_PREF_NAME = "login"

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
        sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        playButton.setOnClickListener { playButtonPressed() }
    }

    override fun onStart() {
        super.onStart()
        //auto Login
        Log.d(MAIN_TAG, "Logged " + sp.getBoolean(Key.SHARED_PREF_LOGGED, false) + " " + sp.getString("userKey", ""))
        if (!sp.getBoolean(Key.SHARED_PREF_LOGGED, false) || !isOnline(this)) {
            goTo(MenuFragment(), MAIN_TAG)
        }
        else {
            val userFragment = UserFragment()
            val bundle = Bundle()
            bundle.putSerializable(USER_CLASS, sp.getString(Key.USER_KEY, ""))
            userFragment.arguments = bundle
            Toast.makeText(this, ToastMessage.AUTO_LOGIN, Toast.LENGTH_SHORT).show()
            goTo(userFragment, MAIN_TAG)
        }
    }

    private fun playButtonPressed() {
        val intent = Intent(this, AndroidLauncher::class.java)
        startActivity(intent)
    }

    private fun goTo(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                fragment,
                tag
        ).commit()
    }

    companion object {

        @SuppressLint("ShowToast")
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            if (activeNetwork?.isConnectedOrConnecting != true) {
                Toast.makeText(context, ToastMessage.NETWORK, Toast.LENGTH_LONG).show()
                return false
            }
            return true

        }
    }

}


