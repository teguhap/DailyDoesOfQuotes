package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val etUser = findViewById<EditText>(R.id.etUsernameLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)


        btnLogin.setOnClickListener {

            if(etUser.text.isEmpty()){
                etUser.error = "Username Requiered"
            }else if(etPassword.text.isEmpty()){
                etPassword.error = "Password Required"
            }else{
                val username = etUser.text.toString()
                val password = etPassword.text.toString()

               getDataLogin(username,password)



            }

        tvRegister.setOnClickListener {
            Intent(this,RegisterActivity :: class.java).also {
                startActivity(it)
            }
        }
    }

    }

    @SuppressLint("CommitPrefEdits")
    fun getDataLogin(username:String, password:String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bod-thinker.000webhostapp.com/api/user?action=login&username=$username&password=$password"
        val setting: SharedPreferences = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        val editor = setting.edit()

        val stringRequest = StringRequest(
            Request.Method.GET,url,
            { response ->
                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val data = jsonObject.getJSONObject("data").getString("username")

                    if(data == username){
                        editor.putString("username",username)
                        editor.apply()
                        Intent(this,ActivityUtama :: class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }else{
                        Toast.makeText(this,"Usenarme atau Password tidak valid",Toast.LENGTH_SHORT).show()
                    }
            }, {})
        queue.add(stringRequest)
    }
}