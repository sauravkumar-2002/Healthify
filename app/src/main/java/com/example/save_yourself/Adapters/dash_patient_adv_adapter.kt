package com.example.save_yourself.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.save_yourself.Models.log_in_model_doctor
import com.example.save_yourself.Models.url_model
import com.example.save_yourself.R

class dash_patient_adv_adapter(var context: Context):RecyclerView.Adapter<dash_patient_adv_adapter.ViewHolder>() {
    var url_list= mutableListOf<url_model>()
    fun seturllist(url_list: List<url_model>){
        this.url_list= url_list as MutableList<url_model>
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quotes=itemView.findViewById<TextView>(R.id.img_adv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_advertisement, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // Log.i("this",url_list.get(position).data)
      //Glide.with(context).load(url_list.get(position).url).centerCrop().into(holder.img)
        holder.quotes.text=url_list.get(position).data
    }

    override fun getItemCount(): Int {
       return url_list.size
    }
}