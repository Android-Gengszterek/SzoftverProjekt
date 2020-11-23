package com.mygdx.game.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.data.classes.User
import com.mygdx.game.ui.constants.DatabasePath

const val FIREBASE_TAG = "firebase"

class FirebaseDatabase {
    private val database = Firebase.database.reference

    private fun getUsers(): List<User> {
        val users = ArrayList<User>()
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
}