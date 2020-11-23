package com.mygdx.game.ui.fragments

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.data.classes.*
import com.mygdx.game.ui.adapters.LeaderBoardAdapter
import com.mygdx.game.ui.adapters.MyAdapter

const val USER_TAG = "User"
const val USER_KEY = "userKey"

@Suppress("UNCHECKED_CAST")
class UserFragment : Fragment() {

    private lateinit var logOutButton: Button
    private lateinit var userNameTextView: TextView
    private lateinit var scoreRecyclerView: RecyclerView
    private lateinit var leaderRecyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewManager2: RecyclerView.LayoutManager

    private lateinit var myUser: User
    private lateinit var myUserId: String
    private lateinit var myScores: ArrayList<Score>
    private lateinit var allScores: ArrayList<Score>
    private lateinit var database: DatabaseReference
    private lateinit var sp: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        setupUI(view)
        loadData(view)

        return view
    }

    companion object {
        fun newInstance(userId: String?): UserFragment {
            val args = Bundle()
            args.putSerializable(USER_CLASS, userId)

            val fragment = UserFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun setupUI(view: View) {
        userNameTextView = view.findViewById(R.id.textView2)
        logOutButton = view.findViewById(R.id.log_out_button)
        logOutButton.setOnClickListener {
            sp.edit().putBoolean("logged", false).apply();
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MenuFragment())?.commit()
        }
        database = Firebase.database.reference
        myScores = ArrayList()
        allScores = ArrayList()
        viewManager = LinearLayoutManager(this.context)
        viewManager2 = LinearLayoutManager(this.context)
        sp = activity!!.getSharedPreferences("login", MODE_PRIVATE)
    }

    private fun getAllScores(view: View) {
        val dbScores = database.child("scores")
        dbScores.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (d in snapshot.children) {
                    val scoreValue = d.child("scoreValue").value.toString().toInt()
                    val userId = d.child("userId").value.toString()
                    allScores.add(Score(userId, scoreValue))
                    Log.d(USER_TAG, "Leaderboard:  $scoreValue")
                }

                allScores.forEach {
                    if (it.userId == myUser.userId) {
                        myScores.add(it)
                    }
                }
                myScores.sortByDescending {
                    it.scoreValue
                }
                allScores.sortByDescending {
                    it.scoreValue
                }
                viewAdapter = MyAdapter(myScores)
                scoreRecyclerView = view.findViewById<RecyclerView>(R.id.myscores_recyclerView).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }

                leaderRecyclerView = view.findViewById<RecyclerView>(R.id.leaderboard_recycleView).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager2
                    adapter = LeaderBoardAdapter(allScores)
                }
                //  Log.d(USER_TAG, allScores[0].scoreValue.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(USER_TAG, "Didn't get scores")
            }
        })
    }

    @SuppressLint("CommitPrefEdits")
    private fun loadData(view: View) {
        val args: Bundle? = arguments
        if (sp.getBoolean("logged", false)) {
            myUserId = sp.getString("userKey", "").toString()
        } else {
            myUserId = args?.getSerializable(USER_CLASS) as String
            sp.edit().putString("userKey", myUserId).apply()
            sp.edit().putBoolean("logged", true).apply()
        }
        val db = database.child("users")
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userNameTextView.text = snapshot.child(myUserId).child("userName").value.toString()
                myUser = User(
                        myUserId,
                        snapshot.child(myUserId).child("userName").value.toString(),
                        snapshot.child(myUserId).child("password").value.toString(),
                )
                Log.d(USER_TAG, myUser.toString())
                getAllScores(view)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(USER_TAG, "onCancelled")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d(USER_TAG, "Game exited, onResume")

        try {
            val file = Gdx.files.local("scores.txt")
            try {
                file.readString()
                updateScore(file)
            } catch (e: Exception) {
                Log.d(USER_TAG, "Invalid file")
            }
            Gdx.files.local("scores.txt").delete()
        } catch (e: NullPointerException) {
            Log.d(USER_TAG, "Unable to read scores.txt")
        }
    }

    private fun updateScore(file: FileHandle) {
        val string = file.readString()
        val newScores = string.split(" ")
        newScores.forEach {
            if (it != "" && it != " ") {
                val key = database.child("scores").push().key.toString()
                val newScore = Score(myUserId, it.toInt())
                database.child("scores").child(key).setValue(newScore)
                        .addOnSuccessListener {
                            Log.d(USER_TAG, "Success score UPDATE -- $it")
                        }
                        .addOnCanceledListener {
                            Log.d(USER_TAG, "Can not write new score to scores -- $it")
                        }
            }
        }
    }


}