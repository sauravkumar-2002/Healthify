package com.example.save_yourself.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Activities.ChatActivity
import com.example.save_yourself.Activities.ChatActivityDoctor
import com.example.save_yourself.Models.*
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Doc_record_adapter(var context: Context) :
    RecyclerView.Adapter<Doc_record_adapter.ViewHolder>() {

    var list = mutableListOf<Users>()
    var status: String = "ch"
    val sp = context.getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)

    var doctId = sp.getString("doctor_id", "").toString()


    fun setUserList(doct_list: List<Users>, status: String) {
        this.list = doct_list as MutableList<Users>
        this.status = status
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user_name = itemView.findViewById<TextView>(R.id.doctor_name)
        val problems = itemView.findViewById<TextView>(R.id.related_problem)
        val date = itemView.findViewById<TextView>(R.id.date)
        val status = itemView.findViewById<TextView>(R.id.status)
        var card = itemView.findViewById<CardView>(R.id.cardview)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_records, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = list.get(position).date
        holder.problems.text = list.get(position).problem
        holder.user_name.text = list.get(position).username
        holder.status.text = list.get(position).status


        holder.card.setOnLongClickListener {
            if (status == "pending") {

                setDialog_here(context, position, list, doctId)
            }
            return@setOnLongClickListener true
        }


        holder.card.setOnClickListener {
            var intent = Intent(context, ChatActivityDoctor::class.java)
            var gson = Gson()
            intent.putExtra("doctor_detail", gson.toJson(list.get(position)).toString())
            context.startActivity(intent)
        }

    }


    private fun setDialog_here(context: Context, position: Int, list: List<Users>, docId: String) {

        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Confirm Appointment?")


        dialog.setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->

            bookappointment_here(context, position, list, docId)
            dialog.dismiss()
        })
        dialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        dialog.show()

    }

    private fun bookappointment_here(
        context: Context,
        position: Int,
        list1: List<Users>,
        docId: String
    ) {


        var list_doctor = ArrayList<Doctors>()


        var reqCall1 =
            Auth_interface_1.getInstance().check_prev_appointment(list.get(position).user)
        reqCall1.enqueue(object : Callback<List<Appointment_user_doctor>> {

            override fun onResponse(
                call: Call<List<Appointment_user_doctor>>,
                response: Response<List<Appointment_user_doctor>>
            ) {
                list_doctor = response.body()!![0].doctors as ArrayList<Doctors>


                var doctor = Doctors(
                    list.get(position).date,
                    list.get(position).doctorname,
                    "Not_applicable",
                    list.get(position).problem,
                    "in progress",
                    list.get(position).username,
                    docId
                )

                var doctor_delete = Doctors(
                    list.get(position).date,
                    list.get(position).doctorname,
                    "Not_applicable",
                    list.get(position).problem,
                    "pending",
                    list.get(position).username,
                    docId
                )

                list_doctor.add(doctor)

                list_doctor.remove(doctor_delete)

                var appointmentUserDoctor =
                    Appointment_user_doctor(list_doctor, list.get(position).user)

                var reqCall = Auth_interface_1.getInstance()
                    .update_appointment_list(list.get(position).user, appointmentUserDoctor)


                reqCall.enqueue(object : Callback<Appointment_user_doctor> {
                    override fun onResponse(
                        call: Call<Appointment_user_doctor>,
                        response: Response<Appointment_user_doctor>
                    ) {

                    }

                    override fun onFailure(call: Call<Appointment_user_doctor>, t: Throwable) {

                    }

                })


            }

            override fun onFailure(call: Call<List<Appointment_user_doctor>>, t: Throwable) {

            }

        })


        //update doctor side
        //var list_user=list

        var list_user = list
        list_user.get(position).status = "in progress"

        notifyDataSetChanged()


        var appointmentDoctorUser = Appointment_doctor_user(docId, list_user as ArrayList<Users>)

        var reqcall =
            Auth_interface_1.getInstance().add_appointment_to_doctor(docId, appointmentDoctorUser)

        reqcall.enqueue(object : Callback<Appointment_doctor_user> {
            override fun onResponse(
                call: Call<Appointment_doctor_user>,
                response: Response<Appointment_doctor_user>
            ) {

            }

            override fun onFailure(call: Call<Appointment_doctor_user>, t: Throwable) {

            }

        })

    }

    override fun getItemCount(): Int {
        return list.size
    }
}