package com.project.dailydoesofquotes

import android.app.ProgressDialog
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
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class RegisterActivity : AppCompatActivity() {
    lateinit var loader : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUser = findViewById<TextInputEditText>(R.id.etUsernameDaftar)
        val etPassword = findViewById<TextInputEditText>(R.id.etPasswordDaftar)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val btnRegist = findViewById<Button>(R.id.btnRegister)
        val tvLogin= findViewById<TextView>(R.id.tvLogin)
        loader = ProgressDialog(this)


        btnRegist.setOnClickListener {



            if(etUser.text?.isEmpty() == true){
                etUser.error = "Username Requiered"
            }else if(etPassword.text?.isEmpty() == true){
                etPassword.error = "Password Required"
            }else if(etConfirmPassword.text.toString() != etPassword.text.toString()){
                etConfirmPassword.error = "Password Not Same"
            }else{
                loader.setTitle("Mohon tunggu")
                loader.setCancelable(false)
                loader.show()
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
                    val message = obj.getString("message")
                    Log.i("hasil",message)
                    if(message == "Register berhasil."){
                        Intent(this,LoginActivity :: class.java).also {
                            startActivity(it)
                            finish()
                        }
                        Toast.makeText(this,"Registrasi Berhasil,Silahkan Login",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                    }
                    loader.dismiss()
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