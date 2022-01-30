package com.project.dailydoesofquotes

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class EditQuotes : AppCompatActivity() {
    lateinit var loader : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_quotes)

        val etQuote = findViewById<EditText>(R.id.etQuotesUpdate)
        val etAuthor = findViewById<EditText>(R.id.etAuthorUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDeleteQuotes)
        val btnUpdate = findViewById<Button>(R.id.btnUpdateQuotes)

        val setting: SharedPreferences = this.getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        val username = setting.getString("username","")
        val id = intent.getStringExtra("id")
        val quote = intent.getStringExtra("quote")
        val author = intent.getStringExtra("author")
        Log.d("idQuote",id.toString())
        etQuote.setText(quote)
        etAuthor.setText(author)

        loader = ProgressDialog(this)
        loader.setCancelable(false)
        loader.setTitle("Mohon Tunggu")

        btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setIcon(R.drawable.logoapp)
            dialog.setTitle("Quote ini akan dihapus")

            dialog.setNegativeButton("No"){_,_->

            }
            dialog.setPositiveButton("Yes"){_,_->
                loader.show()
                deleteQuote(username!!,id!!)
            }
            dialog.create().show()

        }

        btnUpdate.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setIcon(R.drawable.logoapp)
            dialog.setTitle("Quote ini akan diubah")

            dialog.setNegativeButton("No"){_,_->

            }
            dialog.setPositiveButton("Yes"){_,_->
                loader.show()
                val quoteBaru = etQuote.text.toString()
                val authorBaru = etAuthor.text.toString()
                updateQuote(username!!,id!!,authorBaru,quoteBaru)
            }
            dialog.create().show()

        }
    }

    fun deleteQuote(username:String,id:String){

        val ACTION = "https://bod-thinker.000webhostapp.com/api/quotes?action=delete&id=$id&username=$username"

        val stringRequest = object : StringRequest(
            Request.Method.GET,ACTION,
            Response.Listener<String>{ response ->
                try{
                    val obj = JSONObject(response)
                    Log.i("hasil",obj.getString("message"))
                    if(obj.getString("message") == obj.getString("message")){
                        Toast.makeText(this,"Quote berhasil di delete", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,MyQuotes :: class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Sepertinya ada yang salah,yuk coba lagi",Toast.LENGTH_SHORT).show()
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

    fun updateQuote(username:String,id:String,author:String,quote:String){

        val ACTION = "https://bod-thinker.000webhostapp.com/api/quotes?action=edit&id=$id&author=$author&quote=$quote&username=$username"

        val stringRequest = object : StringRequest(
            Request.Method.GET,ACTION,
            Response.Listener<String>{ response ->
                try{
                    val obj = JSONObject(response)
                    if(obj.getString("message") == obj.getString("message")){
                        Toast.makeText(this,"Quote berhasil di update", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,MyQuotes :: class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Sepertinya ada yang salah,yuk coba lagi",Toast.LENGTH_SHORT).show()
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