package com.project.dailydoesofquotes

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView


class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnMyquotes = view.findViewById<Button>(R.id.btnMyQuotes)
        val btnMyProfile = view.findViewById<Button>(R.id.btnEditProfile)

        btnMyquotes.setOnClickListener {
            val intent = Intent(context,MyQuotes::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
    }

}