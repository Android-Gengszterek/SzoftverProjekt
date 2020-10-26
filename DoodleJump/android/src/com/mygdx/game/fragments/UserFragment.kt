package com.mygdx.game.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mygdx.game.R
import com.mygdx.game.classes.USER_CLASS
import com.mygdx.game.classes.User

class UserFragment : Fragment() {

    private lateinit var logOutButton: Button
    private lateinit var userNameTextView: TextView

    private  lateinit var  myUser: User

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

        val args: Bundle? = arguments
        if (args != null) {
            this.myUser = args.getSerializable(USER_CLASS) as User
        }

        userNameTextView.text = this.myUser.userName

        return view
    }


}