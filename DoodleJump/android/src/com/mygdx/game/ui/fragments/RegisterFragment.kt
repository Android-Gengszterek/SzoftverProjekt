package com.mygdx.game.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mygdx.game.R
import com.mygdx.game.data.MyFirebaseDatabase
import com.mygdx.game.data.classes.User
import com.mygdx.game.ui.activitys.MainActivity
import com.mygdx.game.ui.constants.ErrorMessage
import com.mygdx.game.ui.constants.ToastMessage
import java.util.regex.Pattern

const val REGISTER_TAG = "Register"

class RegisterFragment : Fragment() {
    private lateinit var backButton: Button
    private lateinit var registerButton: Button
    private lateinit var userName: EditText
    private lateinit var password: EditText
    private lateinit var confirmPass: EditText

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
        userName.isEmptyFieldListener()
        password.isEmptyFieldListener()
        confirmPass.isEmptyFieldListener()
    }


    private fun setupUI(view: View) {
        backButton = view.findViewById(R.id.back_button)
        registerButton = view.findViewById(R.id.register_button)
        userName = view.findViewById(R.id.username_editTextTextPersonName)
        password = view.findViewById(R.id.password_editTextTextPassword)
        confirmPass = view.findViewById(R.id.password_editTextTextPassword2)

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

    private fun emptyFieldCheck() = (userName.text.isNotEmpty() && password.text.isNotEmpty() && confirmPass.text.isNotEmpty())

    private fun registerButtonPressed(){
        if (MainActivity.isOnline(requireContext()) && emptyFieldCheck()) {
            if (userDataCheck()) {
                writeNewUser(userName.text.toString(), password.text.toString())
            }
        }
        else {
            Toast.makeText(this.context, ToastMessage.FILL_OUT_FIELDS, Toast.LENGTH_SHORT).show()
        }
    }

    private fun userDataCheck() = when {
        userName.text.length < 5 || userName.text.length > 10 -> {
            userName.error = ErrorMessage.USERNAME_LENGTH
            false
        }
        !checkUniqueUsername() -> {
            userName.error = ErrorMessage.USERNAME_EXIST
            false
        }
        !Pattern.compile("(?=.*[0-9]).{8,}").matcher(password.text).matches() -> {
            password.error = ErrorMessage.PASSWORD_LENGTH
            false
        }
        password.text.toString() != confirmPass.text.toString() -> {
            confirmPass.error = ErrorMessage.CONFIRM_PASSWORD
            false
        }
        else -> true
    }

    private fun checkUniqueUsername(): Boolean {
        users.forEach {
            if ( it?.userName == userName.text.toString()) {
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