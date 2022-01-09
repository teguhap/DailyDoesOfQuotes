package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
    lateinit var loader : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_quotes)

        val rvMyQuotes = findViewById<RecyclerView>(R.id.recycleViewMyQuotes)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMyquotes)
        val setting: SharedPreferences = getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
        val username = setting.getString("username","")
        val llNoInternet = findViewById<LinearLayout>(R.id.llNoInternetMyQuotes)
        val llNoQuotes = findViewById<LinearLayout>(R.id.llMyQuotesIsEmpty)
        val btnRefresh  = findViewById<Button>(R.id.btnRefreshMyQuotes)


        loader = ProgressDialog(this)


        val listQuotes = ArrayList<ListQuotes>()
        val listDisplay = ArrayList<ListQuotes>()


        loader.setCancelable(false)
        loader.setTitle("Mohon Tunggu")
        loader.show()

        isOnline()
        if(isOnline()){
            llNoInternet.visibility = View.GONE
            rvMyQuotes.visibility = View.VISIBLE
            getQuotes(username!!,listQuotes,listDisplay, rvMyQuotes)

        }else{
            llNoInternet.visibility = View.VISIBLE
            rvMyQuotes.visibility = View.GONE
        }

        btnRefresh.setOnClickListener {
            loader.show()
            isOnline()
            if(isOnline()){
                llNoInternet.visibility = View.GONE
                rvMyQuotes.visibility = View.VISIBLE
                getQuotes(username!!,listQuotes,listDisplay, rvMyQuotes)
            }else{
                llNoInternet.visibility = View.VISIBLE
                rvMyQuotes.visibility = View.GONE
            }
        }

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

        val rvMyQuotes = findViewById<RecyclerView>(R.id.recycleViewMyQuotes)
        val llNoQuotes = findViewById<LinearLayout>(R.id.llMyQuotesIsEmpty)

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

                if(listQuotes.isEmpty()){
                    llNoQuotes.visibility = View.VISIBLE
                    rvMyQuotes.visibility = View.GONE
                }
                val adapter  = AdapterViewMyQuotes(displayListQuotes)
                rvQuotes.adapter = adapter
                rvQuotes.layoutManager = LinearLayoutManager(this)
                rvQuotes.setHasFixedSize(true)
                loader.dismiss()
            }, {})
        queue.add(stringRequest)
    }



    fun isOnline(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        loader.dismiss()
        return false
    }

}