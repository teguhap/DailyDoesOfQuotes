package com.project.dailydoesofquotes

import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class AdapterViewQuotes(val listData : List<ListQuotes>) : RecyclerView.Adapter<AdapterViewQuotes.ViewHolderView>() {

    inner class ViewHolderView(itemView : View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_card_quotes,parent,false)
        return ViewHolderView(view)
    }

    override fun onBindViewHolder(holder: ViewHolderView, position: Int) {
        holder.itemView.apply {
            val tvNama = findViewById<TextView>(R.id.textQuotes)
            val tvAuthor = findViewById<TextView>(R.id.textAuthor)

            val item = listData[position]
            val quotes = Html.fromHtml(item.quotes)
            tvNama.text = "\"$quotes\""
            tvAuthor.text = item.author



        }
    }

    override fun getItemCount(): Int {

        return  listData.size
    }


}