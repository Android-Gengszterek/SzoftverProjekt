package com.mygdx.game.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.classes.USER_CLASS
import com.mygdx.game.classes.User

const val USER_TAG = "User"

class UserFragment : Fragment() {

    private lateinit var logOutButton: Button
    private lateinit var userNameTextView: TextView

    private lateinit var  myUser: User
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        userNameTextView = view.findViewById(R.id.textView2)
        logOutButton = view.findViewById(R.id.log_out_button)
        logOutButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MenuFragment())?.commit()
        }
        database = Firebase.database.reference

        val args: Bundle? = arguments
        val userId = args?.getSerializable(USER_CLASS) as String

        val db = database.child("users")

        db.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userNameTextView.text = snapshot.child(userId).child("userName").value.toString()
                myUser = User(
                        userId,
                        snapshot.child(userId).child("userName").value.toString(),
                        snapshot.child(userId).child("password").value.toString(),
                        snapshot.child(userId).child("scores").value as ArrayList<String>?
                )
                Log.d(USER_TAG, myUser.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
//        val scores = ArrayList<String>()
//        scores.add("newScoreID")
//        database.child("users").child(userId).child("scores").setValue(scores)

        return view
    }

    override fun onStart() {
        super.onStart()
    }


}