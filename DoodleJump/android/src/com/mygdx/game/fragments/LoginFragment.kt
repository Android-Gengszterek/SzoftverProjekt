package com.mygdx.game.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.classes.USER_CLASS
import com.mygdx.game.classes.User
import kotlin.collections.ArrayList

const val LOGIN_TAG = "Login"
class LoginFragment : Fragment() {

    private lateinit var backButton: Button
    private lateinit var loginButton: Button
    private lateinit var userName: EditText
    private lateinit var password: EditText


    private lateinit var database: DatabaseReference
    private var users = ArrayList<User?>()
    private  lateinit var myUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    private fun setupUI(view: View){
        database = Firebase.database.reference
        backButton = view.findViewById(R.id.back_button)
        loginButton = view.findViewById(R.id.log_in_button)
        userName = view.findViewById(R.id.username_editTextTextPersonName)
        password = view.findViewById(R.id.password_editTextTextPassword)

        val db = database.child("users")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val key = d.key.toString()
                    val username = d.child("userName").value.toString()
                    val password = d.child("password").value.toString()
                    val user = User(key, username, password)
                    users.add(user)
                    Log.d("Users", user.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


        backButton.setOnClickListener { this.backButtonPressed() }
        loginButton.setOnClickListener { this.logInButtonPressed() }
    }

    private fun checkEmptyFields(): Boolean {
        when{
            this.userName.text.isEmpty() -> userName.error = "Fill out please!"
            this.password.text.isEmpty() -> password.error = "Fill out please!"
            else -> return true
        }
        return false
    }

    private fun checkUsernameAndPassword(): Boolean {
        if (users.size == 0){
            userName.error = "Username not exist!"
            return false
        }
        users.forEach {
            if (it?.userName == userName.text.toString()) {
                return if (it.password == password.text.toString()) {
                    myUser = it
                    true
                } else{
                    password.error = "Wrong password!"
                    false
                }
            }
        }
        userName.error = "Username not exist!"
        return false
    }

    private fun backButtonPressed(){
        fragmentManager?.popBackStack()
    }

    private  fun logInButtonPressed(){
        if (checkEmptyFields() && checkUsernameAndPassword()) {
            val userFragment = UserFragment()
            val bundle = Bundle()
            bundle.putSerializable(USER_CLASS, myUser.userId)
            userFragment.arguments = bundle
            Toast.makeText(this.context, "Login Success", Toast.LENGTH_SHORT).show()
            fragmentManager?.beginTransaction()?.replace(
                    R.id.fragment_container,
                    userFragment,
                    LOGIN_TAG
            )?.commit()
        }
        else {
            Log.d("Login", "Can not login")
        }
    }



}