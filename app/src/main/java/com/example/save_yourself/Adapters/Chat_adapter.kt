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

class Chat_adapter(var context: Context,var list:List<chat_model>):RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
   /* var doct_list= mutableListOf<chat_model>()
    fun setDoctorList(chatss: List<chat_model>){
        this.doct_list= chatss as MutableList<chat_model>
        notifyDataSetChanged()
    }*/
   val sp = context.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
    var gson= Gson()
    var json=sp.getString("signUpModelObject","")

    var signUpModelObject=gson.fromJson(json,sign_up_log_in_model::class.java)

    class mvh1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name1: TextView? = null
        var msg1: TextView

        init {
            // name1=itemView.findViewById(R.id.ic_singlerow1name);
            msg1 = itemView.findViewById(R.id.ic_singlerow1msg)
        }
    }
    class mvh2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name2: TextView? = null
        var msg2: TextView

        init {
            // name2=itemView.findViewById(R.id.ic_singlerow2name);
            msg2 = itemView.findViewById(R.id.ic_singlerow2msg)
        }
    }



    override fun getItemViewType( position: Int):Int{
       if (list.get(position).sender.equals(signUpModelObject.Phone))return 0
       else return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==0) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.single_row_1, parent, false)
            return mvh1(v)
        }
        else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.single_row_2, parent, false)
            return mvh2(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass==mvh1::class.java){
           var viewHolder= holder as mvh1
            viewHolder.msg1.text=list.get(position).msg
        }
        else{
            var viewHolder= holder as mvh2
            viewHolder.msg2.text=list.get(position).msg
        }
    }

    override fun getItemCount(): Int {
        Log.i("itemcvoubt",list.size.toString())
        return list.size
    }

}