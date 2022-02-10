package com.project.dailydoesofquotes

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class AddFragment : Fragment() {

    lateinit var loader : ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)
        val etQuotes = view.findViewById<EditText>(R.id.etQuotes)
        val etName = view.findViewById<EditText>(R.id.etName)
        val btnPost = view.findViewById<Button>(R.id.btnPost)
        val btnRegist = view.findViewById<Button>(R.id.btnRegistAdd)
        val llRegist = view.findViewById<LinearLayout>(R.id.llRegistAdd)
        val llAdd = view.findViewById<LinearLayout>(R.id.llMakeQuotes)

        val setting = this.activity?.getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        loader = ProgressDialog(context)

        val statusLogin = setting?.getString("username","")
        if(statusLogin.isNullOrEmpty()){
            llAdd.visibility = View.GONE
            llRegist.visibility = View.VISIBLE
        }

        btnRegist.setOnClickListener {
            Intent(context,LoginActivity :: class.java).also {
                startActivity(it)
            }
        }

        btnPost.setOnClickListener {

            if(etQuotes.text.isEmpty()){
                etQuotes.error = "Kata-katanya masih kosong nih"
            }else if(etName.text.isEmpty()){
                etName.error = "Namamu masih kosong"
            }else{
                loader.setTitle("Tunggu Sebentar Ya")
                loader.show()
                loader.setCancelable(false)

                val quote = etQuotes.text.toString()
                val nama = etName.text.toString()

                addQuote(quote, nama,statusLogin)
                etQuotes.setText("")
                etName.setText("")
            }

        }





        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
    }

    fun addQuote(quote:String,nama:String,status:String?){

        val ACTION = "https://bod-thinker.000webhostapp.com/api/quotes?action=add&author=$nama&quote=$quote&username=$status"

        val stringRequest = object : StringRequest(Request.Method.GET,ACTION,
            Response.Listener<String>{ response ->
                try{
                    val obj = JSONObject(response)
                    Log.i("hasil",obj.getString("message"))
                    if(obj.getString("message") == "Quote berhasil ditambahkan."){
                        Toast.makeText(context,"Quote Berhasil Ditambahkan",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"Sepertinya ada yang salah,yuk coba lagi",Toast.LENGTH_SHORT).show()
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