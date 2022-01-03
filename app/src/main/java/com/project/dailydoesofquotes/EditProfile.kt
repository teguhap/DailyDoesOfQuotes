package com.project.dailydoesofquotes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val tvUSername = findViewById<TextView>(R.id.usernameProfile)
        val etName = findViewById<EditText>(R.id.etNamaProfile)
        val etOldPassword = findViewById<EditText>(R.id.etOldPassword)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val btnUbahPassword = findViewById<Button>(R.id.btnUbahPassword)
        val btnUbahProfile = findViewById<Button>(R.id.btnUbah)
        val btnBatal = findViewById<Button>(R.id.btnBatalUbah)
        val setting: SharedPreferences = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        val username = setting.getString("username","")

        getDataUser(username!!)
        tvUSername.text = username

        btnUbahPassword.setOnClickListener {
            btnUbahPassword.visibility = View.GONE
            btnBatal.visibility = View.VISIBLE
            etOldPassword.visibility  =View.VISIBLE
            etNewPassword.visibility  =View.VISIBLE
        }
        btnBatal.setOnClickListener {
            btnUbahPassword.visibility = View.VISIBLE
            btnBatal.visibility = View.GONE
            etOldPassword.visibility  =View.GONE
            etNewPassword.visibility  =View.GONE
        }

        btnUbahProfile.setOnClickListener {
            val nama = etName.text.toString()
            val oldPassword = etOldPassword.text.toString()
            val newPassword = etNewPassword.text.toString()

            if( etName.text.isEmpty()){
              etName.error = "Nama Masih Kosong"
            }else if(etOldPassword.text.isNotEmpty() && etNewPassword.text.isNotEmpty()){
                ubahDataPassword(oldPassword,newPassword, username)
            }else{
                ubahDataNama(nama, username)
            }


        }
    }


    fun ubahDataNama(nama:String,username:String){

        val ACTION = "https://bod-thinker.000webhostapp.com/api/user?action=edit-profile&username=$username&full_name=$nama"

        val stringRequest = object : StringRequest(
            Request.Method.GET,ACTION,
            Response.Listener<String>{ response ->
                try{
                    val obj = JSONObject(response)
                    Log.i("hasil",obj.getString("message"))
                    Toast.makeText(this,"Nama Berhasil Diubah",Toast.LENGTH_SHORT).show()
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

    fun ubahDataPassword(oldPassword:String,newPassword:String,username:String){

        val ACTION = "https://bod-thinker.000webhostapp.com/api/user?action=change-password&username=$username&old_password=$oldPassword&new_password=$newPassword"

        val stringRequest = object : StringRequest(
            Request.Method.GET,ACTION,
            Response.Listener<String>{ response ->
                try{
                    val obj = JSONObject(response)
                    Log.i("hasil",obj.getString("message"))
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

    fun getDataUser(username:String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bod-thinker.000webhostapp.com/api/user?action=get-profile&username=$username"
        val etName = findViewById<EditText>(R.id.etNamaProfile)
        val stringRequest = StringRequest(
            Request.Method.GET,url,
            { response ->
                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val name = jsonObject.getJSONObject("data").getString("full_name")
                etName.setText(name)
            }, {})
        queue.add(stringRequest)
    }
}