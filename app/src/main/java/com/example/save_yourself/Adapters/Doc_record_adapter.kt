package com.example.save_yourself.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Models.*
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Doc_record_adapter(var context: Context): RecyclerView.Adapter<Doc_record_adapter.ViewHolder>()  {

    var list= mutableListOf<Users>()
    var status:String="ch"
    val sp = context.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)

    var doctId= sp.getString("doctor_id","").toString()
    fun setUserList(doct_list: List<Users>, status:String){
        this.list= doct_list as MutableList<Users>
        this.status=status
        list= list.filter {it.status==status} as MutableList<Users>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user_name=itemView.findViewById<TextView>(R.id.doctor_name)
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
        holder.date.text=list.get(position).date
        holder.problems.text=list.get(position).problem
        holder.user_name.text=list.get(position).username
        holder.status.text=list.get(position).status

            holder.card.setOnClickListener {
                if(status=="pending"){
                setDialog_here(context,position,list,doctId)
            }
        }
    }

    private fun setDialog_here(context: Context, position: Int, list: List<Users>, docId: String) {

        val dialog= AlertDialog.Builder(context)
        dialog.setTitle("Confirm Appointment?")


        dialog.setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->

            bookappointment_here(context,position,list,docId)
            dialog.dismiss()
        })
        dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        dialog.show()

    }

    private fun bookappointment_here(context: Context, position: Int, list1: List<Users>, docId: String) {

//updating user side
        var list_doctor=ArrayList<Doctors>()
        var doctor=Doctors(list.get(position).date,list.get(position).doctorname,"Not_applicable",list.get(position).problem
        ,"in progress",list.get(position).username,docId)
        list_doctor.add(doctor)
       var appointmentUserDoctor=Appointment_user_doctor(list_doctor,list.get(position).user)
        var reqCall= Auth_interface_1.getInstance().update_appointment_list(list.get(position).user,appointmentUserDoctor)
        reqCall.enqueue(object : Callback<Appointment_user_doctor> {
            override fun onResponse(
                call: Call<Appointment_user_doctor>,
                response: Response<Appointment_user_doctor>
            ) {
                Log.i("status updated user",response.toString())
            }

            override fun onFailure(call: Call<Appointment_user_doctor>, t: Throwable) {

            }

        })


        //update doctor side
        //var list_user=list


        var user=Users(list.get(position).date,list.get(position).doctorname,"Not_applicable",list.get(position).problem
            ,"in progress",list.get(position).username,list.get(position).user)


        list.get(position).status="in progress"
        var list_user=list
        notifyDataSetChanged()
        var appointmentDoctorUser=Appointment_doctor_user(docId,list_user as ArrayList<Users>)
        var reqcall= Auth_interface_1.getInstance().add_appointment_to_doctor(docId,appointmentDoctorUser)
        reqcall.enqueue(object : Callback<Appointment_doctor_user> {
            override fun onResponse(
                call: Call<Appointment_doctor_user>,
                response: Response<Appointment_doctor_user>
            ) {
                Log.i("status updated doc",response.toString())
            }

            override fun onFailure(call: Call<Appointment_doctor_user>, t: Throwable) {

            }

        })

    }

    override fun getItemCount(): Int {
       return  list.size
    }
}