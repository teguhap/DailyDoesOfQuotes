package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class ProfileFragment : Fragment() {

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnMyquotes = view.findViewById<Button>(R.id.btnMyQuotes)
        val btnMyProfile = view.findViewById<Button>(R.id.btnEditProfile)
        val tvHello = view.findViewById<TextView>(R.id.textHallo)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val llRegist = view.findViewById<LinearLayout>(R.id.llRegistAddProfile)
        val llProfile = view.findViewById<LinearLayout>(R.id.llProfileHasLogin)
        val btnRegist = view.findViewById<Button>(R.id.btnRegistAddProfile)

        val setting = this.activity?.getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        val username = setting?.getString("username","")
        val edit = setting?.edit()

        if(username.isNullOrEmpty()){
            llProfile.visibility = View.GONE
            llRegist.visibility = View.VISIBLE
        }

        btnRegist.setOnClickListener {
            Intent(context,LoginActivity :: class.java).also {
                startActivity(it)
            }
        }


        tvHello.text = "Hallo $username"

        btnMyquotes.setOnClickListener {
            val intent = Intent(context,MyQuotes::class.java)
            startActivity(intent)
        }
        btnMyProfile.setOnClickListener {
            val intent = Intent(context,EditProfile::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setIcon(R.drawable.logoapp)
            dialog.setTitle("Anda yakin ingin logout?")
            dialog.setNegativeButton("No"){_,_->

            }
            dialog.setPositiveButton("Yes"){_,_->
                edit?.clear()
                edit?.apply()
                if(username.isNullOrEmpty()){
                    llProfile.visibility = View.GONE
                    llRegist.visibility = View.VISIBLE
                }
                val intent = Intent(context,ActivityUtama::class.java)
                startActivity(intent)
                Toast.makeText(context,"Logout Berhasil",Toast.LENGTH_SHORT).show()
            }
            dialog.create().show()


        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
    }

}