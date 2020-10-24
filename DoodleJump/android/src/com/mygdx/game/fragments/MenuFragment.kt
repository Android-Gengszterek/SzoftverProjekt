package com.mygdx.game.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mygdx.game.R


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
            loginButtonPressed(it)
        }
        registerButton.setOnClickListener {
            registerButtonPressed(it)
        }

        return view
    }

    companion object {
        fun newInstance() = MenuFragment()
    }

    @SuppressLint("CommitTransaction")
    private fun loginButtonPressed(view: View){
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, LoginFragment())?.commit()
    }

    private fun registerButtonPressed(view: View){
        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, RegisterFragment())?.commit()
    }


}