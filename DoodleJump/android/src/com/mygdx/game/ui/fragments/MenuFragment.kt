package com.mygdx.game.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mygdx.game.R
import com.mygdx.game.ui.constants.ToastMessage

const val MENU_TAG = "MenuFragment"

class MenuFragment : Fragment() {

    private  lateinit var loginButton: Button
    private  lateinit var registerButton: Button
    private  lateinit var infoButton: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        loginButton = view.findViewById(R.id.log_in_button)
        registerButton = view.findViewById(R.id.register_button)
        infoButton = view.findViewById(R.id.info_button)
        loginButton.setOnClickListener{
            loginButtonPressed()
        }
        registerButton.setOnClickListener {
            registerButtonPressed()
        }
        infoButton.setOnClickListener {
            Toast.makeText(this.context, ToastMessage.SAVE_SCORE, Toast.LENGTH_LONG).show()
        }
        return view
    }

    @SuppressLint("CommitTransaction")
    private fun loginButtonPressed(){
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, LoginFragment())?.
        addToBackStack(MENU_TAG)?.commit()
    }

    private fun registerButtonPressed(){
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, RegisterFragment())?.
        addToBackStack(MENU_TAG)?.commit()
    }
}

