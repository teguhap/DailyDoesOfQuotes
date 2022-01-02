package com.project.dailydoesofquotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView


class AddFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        val etQuotes = view.findViewById<EditText>(R.id.etQuotes)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etTags = view.findViewById<EditText>(R.id.etTags)
        val btnPost = view.findViewById<Button>(R.id.btnPost)

        btnPost.setOnClickListener {
            toast("${etQuotes.text},${etName.text},${etTags.text}")
        }





        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
    }

    fun toast(text:String){
        Toast.makeText(context, text,Toast.LENGTH_LONG).show()
    }

}