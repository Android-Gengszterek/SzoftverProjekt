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
import com.mygdx.game.ui.activitys.SHARED_PREF_NAME
import com.mygdx.game.ui.adapters.LeaderBoardAdapter
import com.mygdx.game.ui.adapters.UserScoresAdapter
import com.mygdx.game.ui.constants.DatabasePath
import com.mygdx.game.ui.constants.Key

const val USER_TAG = "User"
const val SCORES_LOCAL = "scores.txt"

@Suppress("UNCHECKED_CAST")
class UserFragment : Fragment() {

    private lateinit var logOutButton: Button
    private lateinit var userName: TextView
    private lateinit var scoreRecyclerView: RecyclerView
    private lateinit var leaderRecyclerView: RecyclerView
    private lateinit var scoreViewManager: RecyclerView.LayoutManager
    private lateinit var leaderViewManager: RecyclerView.LayoutManager

    private lateinit var myUser: User
    private lateinit var myUserId: String
    private lateinit var myScores: ArrayList<Score>
    private lateinit var allScores: ArrayList<Score>
    private lateinit var database: DatabaseReference
    private lateinit var sp: SharedPreferences
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_user, container, false)
        setupUI()
        loadDataFromDatabase()
        return mView
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

    private fun setupUI() {
        userName = mView.findViewById(R.id.textView2)
        logOutButton = mView.findViewById(R.id.log_out_button)
        logOutButton.setOnClickListener {
            sp.edit().putBoolean(Key.SHARED_PREF_LOGGED, false).apply();
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MenuFragment())?.commit()
        }
        database = Firebase.database.reference
        myScores = ArrayList()
        allScores = ArrayList()
        scoreViewManager = LinearLayoutManager(this.context)
        leaderViewManager = LinearLayoutManager(this.context)
        sp = activity!!.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
    }

    private fun getAllScores() {
        val dbScores = database.child(DatabasePath.SCORES)
        dbScores.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (d in snapshot.children) {
                    val scoreValue = d.child(DatabasePath.SCORE_VALUE).value.toString().toInt()
                    val userId = d.child(DatabasePath.SCORE_USERID).value.toString()
                    allScores.add(Score(userId, scoreValue))
                    Log.d(USER_TAG, "Leaderboard:  $scoreValue")
                }
                setUpRecyclerViews()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(USER_TAG, "Didn't get scores")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d(USER_TAG, "Game exited, onResume")
        getCurrentPlayedScores()
    }

    private fun setUpRecyclerViews() {
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
        scoreRecyclerView = mView.findViewById<RecyclerView>(R.id.myscores_recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = scoreViewManager
            adapter = UserScoresAdapter(myScores)
        }

        leaderRecyclerView = mView.findViewById<RecyclerView>(R.id.leaderboard_recycleView).apply {
            setHasFixedSize(true)
            layoutManager = leaderViewManager
            adapter = LeaderBoardAdapter(allScores)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun loadDataFromDatabase() {
        //Check if the user was logged or it's the first time
        val args: Bundle? = arguments
        if (sp.getBoolean(Key.SHARED_PREF_LOGGED, false)) {
            myUserId = sp.getString(Key.USER_KEY, "").toString()
        } else {
            myUserId = args?.getSerializable(USER_CLASS) as String
            sp.edit().putString(Key.USER_KEY, myUserId).apply()
            sp.edit().putBoolean(Key.SHARED_PREF_LOGGED, true).apply()
        }

        getCurrentUser()
    }

    private fun getCurrentUser() {
        val db = database.child(DatabasePath.USERS)
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName.text = snapshot.child(myUserId).child(DatabasePath.USER_NAME).value.toString()
                myUser = User(
                        myUserId,
                        snapshot.child(myUserId).child(DatabasePath.USER_NAME).value.toString(),
                        snapshot.child(myUserId).child(DatabasePath.PASSWORD).value.toString(),
                )
                Log.d(USER_TAG, myUser.toString())
                getAllScores()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(USER_TAG, "onCancelled")
            }
        })
    }

    private fun getCurrentPlayedScores(){
        try {
            val scoresFile = Gdx.files.local(SCORES_LOCAL)
            try {
                scoresFile.readString()
                updateNewScores(scoresFile)
            } catch (e: Exception) {
                Log.d(USER_TAG, "Invalid file")
            }
            Gdx.files.local(SCORES_LOCAL).delete()
        } catch (e: NullPointerException) {
            Log.d(USER_TAG, "Unable to read scores.txt")
        }
    }

    private fun updateNewScores(file: FileHandle) {
        val scoresString = file.readString()
        val newScores = scoresString.split(" ")
        newScores.forEach {
            if (it != "" && it != " ") {
                val key = database.child(DatabasePath.SCORES).push().key.toString()
                val newScore = Score(myUserId, it.toInt())
                database.child(DatabasePath.SCORES).child(key).setValue(newScore)
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