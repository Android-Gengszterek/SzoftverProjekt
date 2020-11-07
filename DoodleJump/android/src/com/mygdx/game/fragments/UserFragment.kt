package com.mygdx.game.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.classes.*

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
    private lateinit var myScores: ArrayList<Score>
    private lateinit var allScores: ArrayList<Score>
    private lateinit var database: DatabaseReference
    private lateinit var sp: SharedPreferences
    private var preferences: Preferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        setupUI(view)
        loadData(view)

        return view
    }

    private fun setupUI(view: View){
        userNameTextView = view.findViewById(R.id.textView2)
        logOutButton = view.findViewById(R.id.log_out_button)
        logOutButton.setOnClickListener {
            sp.edit().putBoolean("logged",false).apply();
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MenuFragment())?.commit()
        }
        database = Firebase.database.reference
        myScores = ArrayList()
        allScores = ArrayList()
        viewManager = LinearLayoutManager(this.context)
        viewManager2 = LinearLayoutManager(this.context)
        sp = activity!!.getSharedPreferences("login", MODE_PRIVATE)

        //        val scores = ArrayList<String>()
        //        scores.add("newScoreID")
        //        database.child("users").child(userId).child("scores").setValue(scores)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun getMyScores(view: View) {
        val dbs = database.child("scores")
        dbs.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (d in snapshot.children){
                    val scoreValue = d.child("scoreValue").value.toString()
                    val userId = d.child("userId").value.toString()
                    allScores.add(Score(userId, scoreValue))
                    Log.d(USER_TAG, "$scoreValue $userId")

                }
                myUser.scores?.forEach {
                    val scoreValue = snapshot.child(it.toString()).child("scoreValue").value.toString()
                    myScores.add(Score(null , scoreValue  ))
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

               // Log.d(USER_TAG, myScores[0].scoreValue.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(USER_TAG, "Didn't get scores")
            }

        })
        Log.d(USER_TAG, myScores.toString())
    }

    @SuppressLint("CommitPrefEdits")
    private fun loadData(view: View) {
        val args: Bundle? = arguments
        val userId = args?.getSerializable(USER_CLASS) as String
        sp.edit().putString("userKey",userId).apply()
        sp.edit().putBoolean("logged",true).apply()
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
                //Log.d(USER_TAG, myUser.toString())
                getMyScores(view)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(USER_TAG, "onCancelled")
            }
        })

    }


}