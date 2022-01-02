package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvLogo = findViewById<TextView>(R.id.tvLogo)

        tvLogo.text = "\"Q"

        val intent = Intent(this,LoginActivity :: class.java)
        Handler().postDelayed({startActivity(intent)
                              finish()
                              },2000L)

    }


}