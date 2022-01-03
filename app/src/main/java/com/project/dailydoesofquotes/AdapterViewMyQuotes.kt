package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class AdapterViewMyQuotes(val listData : List<ListQuotes>) : RecyclerView.Adapter<AdapterViewMyQuotes.ViewHolderView>() {
    inner class ViewHolderView(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_card_myquotes,parent,false)
        return ViewHolderView(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderView, position: Int) {
        holder.itemView.apply {
            val tvNama = findViewById<TextView>(R.id.textQuotes)
            val tvAuthor = findViewById<TextView>(R.id.textAuthor)
            val cvQuotes = findViewById<CardView>(R.id.cvListMyQuotes)


            val currentItem = listData[position]
            val quotes = currentItem.quotes
            tvNama.text = "\"$quotes\""
            tvAuthor.text = currentItem.author


            cvQuotes.setOnClickListener {
                val popup = PopupMenu(context,it)
                popup.inflate(R.menu.menu_my_quotes);

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.update -> {
                            val intent = Intent(context,EditQuotes :: class.java)
                            intent.putExtra("id",currentItem.id)
                            intent.putExtra("quote",quotes)
                            intent.putExtra("author",currentItem.author)
                            startActivity(context,intent, Bundle.EMPTY)
                            (context as Activity).finish()
                        }
                    }
                    true
                }
                popup.show();

            }
        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }





}