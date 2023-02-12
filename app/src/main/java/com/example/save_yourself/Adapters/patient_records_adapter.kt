package com.example.save_yourself.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Activities.Appointment_Activity
import com.example.save_yourself.Models.Doctors
import com.example.save_yourself.Models.log_in_model_doctor
import com.example.save_yourself.R
import com.google.gson.Gson

class patient_records_adapter(var context:Context):RecyclerView.Adapter<patient_records_adapter.ViewHolder>() {
var list= mutableListOf<Doctors>()

    fun setDoctorList(doct_list: List<Doctors>,status:String){
        this.list= doct_list as MutableList<Doctors>
       list= list.filter {it.status==status} as MutableList<Doctors>
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doc_name=itemView.findViewById<TextView>(R.id.doctor_name)
        val problems=itemView.findViewById<TextView>(R.id.related_problem)

        val date=itemView.findViewById<TextView>(R.id.date)
        val status=itemView.findViewById<TextView>(R.id.status)
        var card=itemView.findViewById<CardView>(R.id.cardview)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_records, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var gson= Gson()


     holder.date.text=list.get(position).date
     holder.problems.text=list.get(position).problem
     holder.doc_name.text=list.get(position).doctorname
     holder.status.text=list.get(position).status

        holder.card.setOnClickListener {
            var intent=Intent(context,Appointment_Activity::class.java)
            intent.putExtra("doctor_detail",gson.toJson(list.get(position)).toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
     return list.size
    }
}