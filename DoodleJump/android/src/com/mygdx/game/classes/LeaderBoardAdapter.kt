package com.mygdx.game.classes

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mygdx.game.R
import com.mygdx.game.fragments.USER_TAG
import kotlinx.android.synthetic.main.leaderboard_item.view.*
import kotlinx.android.synthetic.main.score_item.view.*

class LeaderBoardAdapter(private val myDataset: ArrayList<Score>) : RecyclerView.Adapter<LeaderBoardAdapter.MySceondViewHolder>() {

    class MySceondViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val numberTextView: TextView = itemView.leader_number
        val valueTextView: TextView = itemView.leader_value
        val userNameTextView: TextView = itemView.leader_user
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySceondViewHolder {
        val leaderItem = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_item, parent, false)
        return MySceondViewHolder(leaderItem)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MySceondViewHolder, position: Int) {
        val currentItem = myDataset[position]
        val database = Firebase.database.reference
        val db = database.child("users")

        holder.numberTextView.text = "$position."
        holder.valueTextView.text = currentItem.scoreValue.toString()
        //holder.numberTextView.text = database.child("users").child(currentItem.userId).child("userName").value
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.userNameTextView.text = currentItem.userId?.let { snapshot.child(it).child("userName").value.toString() }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(USER_TAG, "onCancelled")
            }
        })
    }

    override fun getItemCount() = myDataset.size

}