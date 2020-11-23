package com.mygdx.game.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mygdx.game.R
import com.mygdx.game.data.classes.User
import com.mygdx.game.ui.constants.ErrorMessage
import com.mygdx.game.ui.constants.ToastMessage
import kotlin.collections.ArrayList
import com.mygdx.game.data.FirebaseDatabase

const val LOGIN_TAG = "Login"

class LoginFragment : Fragment() {
    private lateinit var backButton: Button
    private lateinit var loginButton: Button
    private lateinit var userName: EditText
    private lateinit var password: EditText

    private lateinit var database: FirebaseDatabase
    private lateinit var users: ArrayList<User?>
    private lateinit var myUser: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)
        setupUI(view)
        return view
    }

    override fun onResume() {
        userName.text = null
        password.text = null
        super.onResume()
    }

    override fun onStart() {
        getUsers()
        super.onStart()
    }

    private fun setupUI(view: View){
        backButton = view.findViewById(R.id.back_button)
        loginButton = view.findViewById(R.id.log_in_button)
        userName = view.findViewById(R.id.username_editTextTextPersonName)
        password = view.findViewById(R.id.password_editTextTextPassword)
        users = ArrayList()
        database = FirebaseDatabase()
        backButton.setOnClickListener { this.backButtonPressed() }
        loginButton.setOnClickListener { this.logInButtonPressed() }
    }

    private fun getUsers(){

    }

    private fun checkEmptyFields(): Boolean {
        when{
            this.userName.text.isEmpty() -> userName.error = ErrorMessage.EMPTY_FIELD
            this.password.text.isEmpty() -> password.error = ErrorMessage.EMPTY_FIELD
            else -> return true
        }
        return false
    }

    private fun checkUsernameAndPassword(): Boolean {
        if (users.size == 0){
            userName.error = ErrorMessage.USERNAME_NOT_EXIST
            return false
        }
        users.forEach {
            if (it?.userName == userName.text.toString()) {
                return if (it.password == password.text.toString()) {
                    myUser = it
                    true
                } else{
                    password.error = ErrorMessage.PASSWORD
                    false
                }
            }
        }
        userName.error = ErrorMessage.USERNAME_NOT_EXIST
        return false
    }

    private fun backButtonPressed() {
        fragmentManager?.popBackStack()
    }

    private  fun logInButtonPressed() {
        if (checkEmptyFields() && checkUsernameAndPassword()) {
            val userFragment = UserFragment.newInstance(myUser.userId)
            Toast.makeText(this.context, ToastMessage.LOGIN_SUCCESS, Toast.LENGTH_SHORT).show()
            goTo(userFragment, LOGIN_TAG)
        }
        else {
            Log.d(LOGIN_TAG, "Can not login")
        }
    }

    private fun goTo(fragment: Fragment, tag: String) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                fragment,
                tag
        )?.commit()
    }

}