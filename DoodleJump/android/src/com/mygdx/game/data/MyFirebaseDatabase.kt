package com.mygdx.game.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.data.classes.User
import com.mygdx.game.ui.constants.DatabasePath
import com.mygdx.game.ui.constants.ToastMessage
import com.mygdx.game.ui.fragments.LOGIN_TAG
import com.mygdx.game.ui.fragments.UserFragment

const val FIREBASE_TAG = "firebase"

class MyFirebaseDatabase {
    private val database = Firebase.database.reference

    fun getUsers(): ArrayList<User?> {
        val users = ArrayList<User?>()
        val db = database.child(DatabasePath.USERS)
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (d in dataSnapshot.children) {
                    val key = d.key.toString()
                    val username = d.child(DatabasePath.USER_NAME).value.toString()
                    val password = d.child(DatabasePath.PASSWORD).value.toString()
                    val user = User(key, username, password)
                    users.add(user)
                    Log.d(FIREBASE_TAG, user.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(FIREBASE_TAG, "getUsers() - onCancelled")
            }
        })
        return users
    }

    fun writeNewUser(userName: String, password: String, context: Context?, manager: FragmentManager?) {
        val key = database.child(DatabasePath.USERS).push().key.toString()
        val user = User(null, userName, password )
        database.child(DatabasePath.USERS).child(key).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(context, ToastMessage.REG_SUCCESS, Toast.LENGTH_LONG).show()
                    val userFragment = UserFragment.newInstance(key)
                    manager?.beginTransaction()?.replace(
                            R.id.fragment_container,
                            userFragment,
                            LOGIN_TAG
                    )?.commit()

                }
                .addOnFailureListener {
                    Toast.makeText(context, ToastMessage.REG_FAIL, Toast.LENGTH_LONG).show()
                }
    }

}