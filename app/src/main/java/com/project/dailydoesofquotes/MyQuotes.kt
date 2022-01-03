package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MyQuotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_quotes)

        val rvMyQuotes = findViewById<RecyclerView>(R.id.recycleViewMyQuotes)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMyquotes)
        val setting: SharedPreferences = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        val username = setting.getString("username","")

        val listQuotes = ArrayList<ListQuotes>()
        val listDisplay = ArrayList<ListQuotes>()


      getQuotes(username!!,listQuotes,listDisplay,rvMyQuotes)

        toolbar.setOnMenuItemClickListener {item->
            when(item.itemId){
                R.id.searchMyquotes -> {
                    val searchView  = SearchView(this)
                    searchView.queryHint = "Search"
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            listDisplay.clear()
                            rvMyQuotes.adapter!!.notifyDataSetChanged()
                            return false
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        override fun onQueryTextChange(newText: String?): Boolean {
                            if(newText!!.isNotEmpty()){
                                listDisplay.clear()
                                val search = newText.lowercase(Locale.getDefault())
                                listQuotes.forEach {
                                    if(it.quotes.lowercase(Locale.getDefault()).contains(search)
                                        ||it.author.lowercase(Locale.getDefault()).contains(search)){
                                        listDisplay.add(it)
                                    }
                                    rvMyQuotes.adapter!!.notifyDataSetChanged()
                                }
                            }else{
                                listDisplay.clear()
                                listDisplay.addAll(listQuotes)
                                rvMyQuotes.adapter!!.notifyDataSetChanged()

                            }
                            return true
                        }
                    })

                    item.actionView = searchView

                    true
                }
                else -> false
            }
        }

    }

    fun getQuotes(username:String,listQuotes : MutableList<ListQuotes>, displayListQuotes: MutableList<ListQuotes>,rvQuotes: RecyclerView){
        val queue = Volley.newRequestQueue(this)
        val url = "https://bod-thinker.000webhostapp.com/api/quotes?action=get-mine&username=$username"

        val stringRequest = StringRequest(
            Request.Method.GET,url,
            {
                    response ->

                val strRespon = response.toString()
                val jsonObject = JSONObject(strRespon)
                val jsonArray: JSONArray = jsonObject.getJSONArray("data")
                listQuotes.clear()
                displayListQuotes.clear()
                for(i in 0 until jsonArray.length()){
                    val jsonInner : JSONObject = jsonArray.getJSONObject(i)
                    val id = jsonInner.get("id").toString()
                    val quote = jsonInner.get("quote").toString()
                    val author =  jsonInner.get("author").toString()
                    listQuotes.add(ListQuotes(id,quote,author))
                }

                displayListQuotes.addAll(listQuotes)
                val adapter  = AdapterViewMyQuotes(displayListQuotes)
                rvQuotes.adapter = adapter
                rvQuotes.layoutManager = LinearLayoutManager(this)
                rvQuotes.setHasFixedSize(true)
            }, {})
        queue.add(stringRequest)
    }
}