package com.mygdx.game.ui.fragments

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
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.data.MyFirebaseDatabase
import com.mygdx.game.data.classes.USER_CLASS
import com.mygdx.game.data.classes.User
import com.mygdx.game.ui.constants.ErrorMessage
import com.mygdx.game.ui.constants.ToastMessage
import java.util.regex.Pattern

const val REGISTER_TAG = "Register"

class RegisterFragment : Fragment() {
    private lateinit var backButton: Button
    private lateinit var registerButton: Button
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confPassEditText: EditText

    private lateinit var database: MyFirebaseDatabase
    private lateinit var users: ArrayList<User?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_register, container, false)
        setupUI(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        getUsers()
        userNameEditText.isEmptyFieldListener()
        passwordEditText.isEmptyFieldListener()
        confPassEditText.isEmptyFieldListener()
    }


    private fun setupUI(view: View) {
        backButton = view.findViewById(R.id.back_button)
        registerButton = view.findViewById(R.id.register_button)
        userNameEditText = view.findViewById(R.id.username_editTextTextPersonName)
        passwordEditText = view.findViewById(R.id.password_editTextTextPassword)
        confPassEditText = view.findViewById(R.id.password_editTextTextPassword2)

        database = MyFirebaseDatabase()
        backButton.setOnClickListener { backButtonPressed() }
        registerButton.setOnClickListener { registerButtonPressed() }
    }

    private fun writeNewUser(userName: String, password: String) {
        database.writeNewUser(userName, password, this.context, fragmentManager)
    }

    private fun getUsers(){
        users = database.getUsers()
    }

    private fun emptyFieldCheck() = (userNameEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() && confPassEditText.text.isNotEmpty())

    private fun registerButtonPressed(){
        if (emptyFieldCheck()) {
            if (userDataCheck()) {
                writeNewUser(userNameEditText.text.toString(), passwordEditText.text.toString())
            }
        }
        else {
            Toast.makeText(this.context, ToastMessage.FILL_OUT_FIELDS, Toast.LENGTH_SHORT).show()
        }
    }

    private fun userDataCheck() = when {
        userNameEditText.text.length < 5 || userNameEditText.text.length > 10 -> {
            userNameEditText.error = ErrorMessage.USERNAME_LENGTH
            false
        }
        !checkUniqueUsername() -> {
            userNameEditText.error = ErrorMessage.USERNAME_EXIST
            false
        }
        !Pattern.compile("(?=.*[0-9]).{8,}").matcher(passwordEditText.text).matches() -> {
            passwordEditText.error = ErrorMessage.PASSWORD_LENGTH
            false
        }
        passwordEditText.text.toString() != confPassEditText.text.toString() -> {
            confPassEditText.error = ErrorMessage.CONFIRM_PASSWORD
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

fun EditText.isEmptyFieldListener() {
    this.afterTextChanged {
        if (it.isEmpty()){
            this.error = ErrorMessage.EMPTY_FIELD
        }
    }
}