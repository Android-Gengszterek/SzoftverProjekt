package com.mygdx.game.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mygdx.game.R
import com.mygdx.game.classes.USER_CLASS
import com.mygdx.game.classes.User

const val MENU_TAG = "MenuFragment"

class MenuFragment : Fragment() {

    private  lateinit var loginButton: Button
    private  lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        loginButton = view.findViewById(R.id.log_in_button)
        registerButton = view.findViewById(R.id.register_button)
        loginButton.setOnClickListener{
            loginButtonPressed()
        }
        registerButton.setOnClickListener {
            registerButtonPressed()
        }

        return view
    }


    companion object {
        fun newInstance() = MenuFragment()
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

