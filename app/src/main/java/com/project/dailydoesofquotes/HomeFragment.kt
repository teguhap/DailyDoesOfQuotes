package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.shimmer.ShimmerFrameLayout
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    lateinit var shimmer : ShimmerFrameLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerViewHome = view.findViewById<RecyclerView>(R.id.recycleViewQuotes)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val llNoInternet = view.findViewById<LinearLayout>(R.id.llNoInternetHome)
        val btnRefresh  = view.findViewById<Button>(R.id.btnRefreshHome)
        val swiper = view.findViewById<SwipeRefreshLayout>(R.id.swiperHome)
        shimmer = view.findViewById(R.id.shimmmerFrame)



         val listQuotes = ArrayList<ListQuotes>()
         val listDisplay = ArrayList<ListQuotes>()


        isOnline()
        if(isOnline()){
            llNoInternet.visibility = View.GONE
            recyclerViewHome.visibility = View.VISIBLE
            getQuotes(listQuotes,listDisplay,recyclerViewHome)
        }else{
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            llNoInternet.visibility = View.VISIBLE
            recyclerViewHome.visibility = View.GONE
        }

        btnRefresh.setOnClickListener {
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            isOnline()
            if(isOnline()){
                llNoInternet.visibility = View.GONE
                recyclerViewHome.visibility = View.VISIBLE
                getQuotes(listQuotes,listDisplay,recyclerViewHome)
            }else{
                llNoInternet.visibility = View.VISIBLE
                recyclerViewHome.visibility = View.GONE
            }
        }
        swiper.setOnRefreshListener {
            recyclerViewHome.visibility = View.GONE
            shimmer.startShimmer()
            shimmer.visibility = View.VISIBLE
            listQuotes.clear()
            listDisplay.clear()
            isOnline()
            if(isOnline()){
                llNoInternet.visibility = View.GONE
                recyclerViewHome.visibility = View.VISIBLE
                getQuotes(listQuotes,listDisplay,recyclerViewHome)
            }else{
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
                llNoInternet.visibility = View.VISIBLE
                recyclerViewHome.visibility = View.GONE
            }
            swiper.isRefreshing = false
        }

        toolbar.setOnMenuItemClickListener {item->
            when(item.itemId){
                R.id.search -> {
                    val searchView  = SearchView(context)
                    searchView.queryHint = "Search"
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            shimmer.startShimmer()
                            shimmer.visibility = View.VISIBLE
                            listDisplay.clear()
                            val search = query?.lowercase(Locale.getDefault())
                            if (search != null) {
                                getQuotesSearch(search,listQuotes,listDisplay,recyclerViewHome)
                            }
                            recyclerViewHome.adapter!!.notifyDataSetChanged()
                            return false
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        override fun onQueryTextChange(newText: String?): Boolean {
                            shimmer.startShimmer()
                            shimmer.visibility = View.VISIBLE
                            if(newText!!.isNotEmpty()){
                                listDisplay.clear()
                                val search = newText.lowercase(Locale.getDefault())
                                getQuotesSearch(search,listQuotes,listDisplay,recyclerViewHome)
                                listQuotes.forEach {
                                    if(it.quotes.lowercase(Locale.getDefault()).contains(search)
                                        ||it.author.lowercase(Locale.getDefault()).contains(search)){
                                        listDisplay.add(it)
                                    }
                                    recyclerViewHome.adapter!!.notifyDataSetChanged()
                                }
                            }else{
                                getQuotes(listQuotes,listDisplay,recyclerViewHome)
                                listDisplay.clear()
                                listDisplay.addAll(listQuotes)
                                recyclerViewHome.adapter!!.notifyDataSetChanged()

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


        return view
    }


    fun getQuotes(listQuotes : MutableList<ListQuotes>, displayListQuotes: MutableList<ListQuotes>,rvQuotes: RecyclerView){
        val queue = Volley.newRequestQueue(context)
        val url = "https://bod-thinker.000webhostapp.com/api/quotes?action=get"

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
                val adapter  = AdapterViewQuotes(displayListQuotes)
                rvQuotes.adapter = adapter
                rvQuotes.layoutManager = LinearLayoutManager(context)
                rvQuotes.setHasFixedSize(true)
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
            }, {})
        queue.add(stringRequest)
    }

    fun getQuotesSearch(text:String,listQuotes : MutableList<ListQuotes>, displayListQuotes: MutableList<ListQuotes>,rvQuotes: RecyclerView){
        val queue = Volley.newRequestQueue(context)
        val url = "https://bod-thinker.000webhostapp.com/api/quotes?action=find&search=$text"

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
                val adapter  = AdapterViewQuotes(displayListQuotes)
                rvQuotes.adapter = adapter
                rvQuotes.layoutManager = LinearLayoutManager(context)
                rvQuotes.setHasFixedSize(true)
                shimmer.stopShimmer()
                shimmer.visibility = View.GONE
            }, {})
        queue.add(stringRequest)
    }


    fun isOnline(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        return false
    }

}
