package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {






    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerViewHome = view.findViewById<RecyclerView>(R.id.recycleViewQuotes)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

         val listQuotes = ArrayList<ListQuotes>()
         val listDisplay = ArrayList<ListQuotes>()



        listQuotes.add(ListQuotes("Anjing Lah","Ranti"))
        listQuotes.add(ListQuotes("Haloooooo","Ranti"))
        listQuotes.add(ListQuotes("Manusia Kuat","Jason"))
        listQuotes.add(ListQuotes("Ga jelassss","Tulus"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))


        listDisplay.addAll(listQuotes)
        listQuotes.forEach {
            Log.d("hasil",it.quotes)
        }

        recyclerViewHome.adapter = AdapterViewQuotes(listDisplay)
        recyclerViewHome.layoutManager = LinearLayoutManager(context)
        recyclerViewHome.setHasFixedSize(true)

        toolbar.setOnMenuItemClickListener {item->
            when(item.itemId){
                R.id.refresh -> {
                    Toast.makeText(context,"Referesh",Toast.LENGTH_LONG).show()
                    true
                }
                R.id.search -> {
                    val searchView  = SearchView(context)
                    searchView.queryHint = "Search"
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            listDisplay.clear()
                            recyclerViewHome.adapter!!.notifyDataSetChanged()
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
                                    recyclerViewHome.adapter!!.notifyDataSetChanged()
                                }
                            }else{
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


}
