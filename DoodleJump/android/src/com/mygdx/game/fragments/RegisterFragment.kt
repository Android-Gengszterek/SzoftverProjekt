package com.mygdx.game.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
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
import java.util.regex.Pattern

class RegisterFragment : Fragment() {

    private lateinit var backButton: Button
    private lateinit var registerButton: Button
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confPassEditText: EditText

    private lateinit var database: DatabaseReference
    private lateinit var myUserKey: String
    private var users = ArrayList<User?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_register, container, false)
        setupUI(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        userNameEditText.afterTextChanged {
            if (it.isEmpty()){
                userNameEditText.error = "This field is empty!"
            }
        }
        passwordEditText.afterTextChanged {
            if (it.isEmpty()){
                passwordEditText.error = "This field is empty!"
            }
        }
        confPassEditText.afterTextChanged {
            if (it.isEmpty()){
                confPassEditText.error = "This field is empty!"
            }
        }
    }


    private fun setupUI(view: View) {
        backButton = view.findViewById(R.id.back_button)
        registerButton = view.findViewById(R.id.register_button)
        userNameEditText = view.findViewById(R.id.username_editTextTextPersonName)
        passwordEditText = view.findViewById(R.id.password_editTextTextPassword)
        confPassEditText = view.findViewById(R.id.password_editTextTextPassword2)

        database = Firebase.database.reference

        backButton.setOnClickListener { this.backButtonPressed() }
        registerButton.setOnClickListener { registerButtonPressed() }

        val db = database.child("users")

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val key = d.key.toString()
                    val username = d.child("userName").value.toString()
                    val password = d.child("password").value.toString()
                    val user = User(key, username,password)
                    users.add(user)
                    Log.d("Users", user.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun writeNewUser(userName: String, password: String) {
        val key = database.child("users").push().key.toString()
        myUserKey = key
        val user = User(null, userName, password )
        database.child("users").child(key).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this.context, "Registration success", Toast.LENGTH_LONG).show()
                    val userFragment = UserFragment()
                    val bundle = Bundle()
                    bundle.putSerializable(USER_CLASS, myUserKey)
                    userFragment.arguments = bundle

                    fragmentManager?.beginTransaction()?.replace(
                            R.id.fragment_container,
                            userFragment,
                            LOGIN_TAG
                    )?.commit()

                }
                .addOnFailureListener {
                    Toast.makeText(this.context, "Registration fail", Toast.LENGTH_LONG).show()
                }
    }

    private fun emptyFieldCheck() = (userNameEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() && confPassEditText.text.isNotEmpty())


    private fun registerButtonPressed(){
        if (emptyFieldCheck()) {
            if (userDataCheck()) {
                writeNewUser(
                        userNameEditText.text.toString(),
                        passwordEditText.text.toString(),
                )
            }
        }
        else {
            Toast.makeText(this.context, "Please fill out all the fields!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun userDataCheck() = when {
        userNameEditText.text.length < 5 || userNameEditText.text.length > 10 -> {
            userNameEditText.error = "Minimum 5, maximum 10 character"
            false
        }
        !checkUniqueUsername() -> {
            userNameEditText.error = "This username is already exist"
            false
        }
        !Pattern.compile("(?=.*[0-9]).{8,}").matcher(passwordEditText.text).matches() -> {
            passwordEditText.error = "Minimum 8 character, at least 1 digit"
            false

        }
        passwordEditText.text.toString() != confPassEditText.text.toString() -> {
            confPassEditText.error = "Not the same password"
            false
        }
        else -> true
    }

    private fun checkUniqueUsername(): Boolean {
        users.forEach {
            if ( it?.userName == userNameEditText.text.toString()) {
                return false
            }
        }
        return true
    }

    private fun backButtonPressed(){
        fragmentManager?.popBackStack()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}