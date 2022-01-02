package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MyQuotes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_quotes)

        val rvMyQuotes = findViewById<RecyclerView>(R.id.recycleViewMyQuotes)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMyquotes)

        val listQuotes = ArrayList<ListQuotes>()
        val listDisplay = ArrayList<ListQuotes>()



        listQuotes.add(ListQuotes("Anjing Lah","Ranti"))
        listQuotes.add(ListQuotes("Haloooooo","Ranti"))
        listQuotes.add(ListQuotes("Manusia Kuat Bnagetee gilaa keen banget kamu","Jason"))
        listQuotes.add(ListQuotes("Ga jelassss","Tulus"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))
        listQuotes.add(ListQuotes("Hai Kafiiir","JAson"))


        listDisplay.addAll(listQuotes)
        listQuotes.forEach {
            Log.d("hasil",it.quotes)
        }

        rvMyQuotes.adapter = AdapterViewMyQuotes(listDisplay)
        rvMyQuotes.layoutManager = LinearLayoutManager(this)
        rvMyQuotes.setHasFixedSize(true)


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

}