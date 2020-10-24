package com.mygdx.game.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mygdx.game.R

class RegisterFragment : Fragment() {

    private lateinit var backButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_register, container, false)
        backButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MenuFragment())?.commit()
        }
        registerButton = view.findViewById(R.id.register_button)
        registerButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, UserFragment())?.commit()
        }
        return view
    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance() = RegisterFragment()
//
//    }
}