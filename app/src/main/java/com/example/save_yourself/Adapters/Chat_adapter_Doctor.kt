package com.example.save_yourself.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Models.chat_model
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.google.gson.Gson

class Chat_adapter_Doctor(var context: Context, var list: List<chat_model>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val sp = context.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
    var doct_id_shared = sp.getString("doctor_id", "").toString()


    class mvh1(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var msg1: TextView = itemView.findViewById(R.id.ic_singlerow1msg)

    }


    class mvh2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var msg2: TextView = itemView.findViewById(R.id.ic_singlerow2msg)

    }


    override fun getItemViewType(position: Int): Int {
        if (list.get(position).sender.equals(doct_id_shared)) return 0
        else return 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.single_row_1, parent, false)
            return mvh1(v)
        } else {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.single_row_2, parent, false)
            return mvh2(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.javaClass == mvh1::class.java) {
            var viewHolder = holder as mvh1
            viewHolder.msg1.text = list.get(position).msg
        } else {
            var viewHolder = holder as mvh2
            viewHolder.msg2.text = list.get(position).msg
        }
    }

    override fun getItemCount(): Int {
        Log.i("itemcvoubt", list.size.toString())
        return list.size
    }
}