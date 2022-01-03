package com.project.dailydoesofquotes

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
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUser = findViewById<EditText>(R.id.etUsernameDaftar)
        val etPassword = findViewById<EditText>(R.id.etPasswordDaftar)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegist = findViewById<Button>(R.id.btnRegister)
        val tvLogin= findViewById<TextView>(R.id.tvLogin)


        btnRegist.setOnClickListener {

            if(etUser.text.isEmpty()){
                etUser.error = "Username Requiered"
            }else if(etPassword.text.isEmpty()){
                etPassword.error = "Password Required"
            }else{
                val username = etUser.text.toString()
                val password = etPassword.text.toString()

                registerAccount(username,password)

            }


        }

        tvLogin.setOnClickListener {
            Intent(this,LoginActivity:: class.java).also {
                startActivity(it)
            }
        }
    }

    fun registerAccount(username:String,password:String){
        val ACTION = "https://bod-thinker.000webhostapp.com/api/user?action=register&username=$username" +
                "&password=$password"

        val stringRequest = object : StringRequest(
            Request.Method.GET,ACTION,
            Response.Listener<String>{ response ->
                try{
                    val obj = JSONObject(response)
                    Log.i("hasil",obj.getString("message"))
                    Toast.makeText(this,"Registrasi Berhasil,Silahkan Login",Toast.LENGTH_SHORT).show()
                    Intent(this,LoginActivity :: class.java).also {
                        startActivity(it)
                        finish()
                    }
                }catch(e: JSONException){
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    Log.e(
                        "hasil : ",error!!.message.toString()
                    )
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String,String>()
                return params
            }}

        Sender.instance!!.addToRequestQueue(stringRequest)

    }
}