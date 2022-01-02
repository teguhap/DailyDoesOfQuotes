package com.project.dailydoesofquotes

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


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

            val item = listData[position]
            val quotes = item.quotes
            tvNama.text = "\"$quotes\""
            tvAuthor.text = item.author

            cvQuotes.setOnClickListener {
                val popup = PopupMenu(context,it)
                popup.inflate(R.menu.menu_my_quotes);

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete -> {
                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
                        }
                        R.id.update -> {
                            Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show()
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