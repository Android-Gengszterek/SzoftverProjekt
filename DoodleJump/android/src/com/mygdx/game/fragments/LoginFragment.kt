package com.mygdx.game.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mygdx.game.R


class LoginFragment : Fragment() {

    private lateinit var backButton: Button
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        backButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MenuFragment())?.commit()
        }
        loginButton = view.findViewById(R.id.log_in_button)
        loginButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, UserFragment())?.commit()
        }
        return view
    }


}