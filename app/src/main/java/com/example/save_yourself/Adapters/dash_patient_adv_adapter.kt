package com.example.save_yourself.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Models.url_model
import com.example.save_yourself.R

class dash_patient_adv_adapter(var context: Context) :
    RecyclerView.Adapter<dash_patient_adv_adapter.ViewHolder>() {

    private var url_list = mutableListOf<url_model>()


    fun seturllist(url_list: List<url_model>) {
        this.url_list = url_list as MutableList<url_model>
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quotes = itemView.findViewById<TextView>(R.id.img_adv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_advertisement, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.quotes.text = url_list[position].data
    }

    override fun getItemCount(): Int {
        return url_list.size
    }
}