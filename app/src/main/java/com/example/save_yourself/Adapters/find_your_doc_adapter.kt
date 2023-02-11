package com.example.save_yourself.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.lang.UCharacter
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Activities.Patient.myProfile
import com.example.save_yourself.Models.*
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Appointment_interface_1
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class find_your_doc_adapter(var context:Context,signUpModelObject: sign_up_log_in_model):RecyclerView.Adapter<find_your_doc_adapter.ViewHolder>() {
    var doct_list= mutableListOf<log_in_model_doctor>()
     var signUpModelObject=signUpModelObject
    fun setDoctorList(doct_list: List<log_in_model_doctor>){
        this.doct_list= doct_list as MutableList<log_in_model_doctor>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val doc_name=itemView.findViewById<TextView>(R.id.doctor_name)
        val rating=itemView.findViewById<TextView>(R.id.rating)
        val speciality=itemView.findViewById<TextView>(R.id.speciality)
        var card=itemView.findViewById<CardView>(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_find_your_doc, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.doc_name.text=doct_list.get(position).Name
    holder.rating.text=doct_list.get(position).Rating
        var arr:List<String>?
        var tag:String="Speciality in -> "
arr= doct_list.get(position).Speciality
   if (arr != null) {
       for(i in 0..arr.size-1){
          tag=tag+arr[i]+","
           if(i%3==2)tag=tag+"\n"
       }
   }
   holder.speciality.text=tag
        holder.card.setOnClickListener {
           setDialog(context,position,doct_list,signUpModelObject)
        }
}

override fun getItemCount(): Int {
   return doct_list.size
}
}

fun setDialog(context:Context,position: Int,doct_list: List<log_in_model_doctor>,signUpModelObject:sign_up_log_in_model){
    var datePicker=DatePicker(context)
    var related_prob_edittext=EditText(context)
    var msg=TextView(context)
    msg.text="Upload Previous Details"
    msg.setTextColor(777)
    var title=TextView(context)
    title.text="Book Appointment?"
    related_prob_edittext.layout
    related_prob_edittext.hint="Write your Problems"
    datePicker.minDate=(System.currentTimeMillis() - 1000)
    var ll=LinearLayout(context)
    ll.orientation=LinearLayout.VERTICAL
    ll.addView(related_prob_edittext)
    ll.addView(datePicker)
val dialog=AlertDialog.Builder(context)
    dialog.setCustomTitle(title)
    dialog.setView(ll)

    dialog.setPositiveButton("Confirm",DialogInterface.OnClickListener { dialog, which -> 
        var day=datePicker.dayOfMonth
        var month=datePicker.month
        var year=datePicker.year
        var choosen_date="$day/$month/$year"
        var rel_problems=related_prob_edittext.text.toString()
        bookappointment(context,choosen_date,rel_problems,position,doct_list,signUpModelObject)
        Log.i("date",choosen_date+rel_problems,)
        dialog.dismiss()
    })
    dialog.setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    })
    dialog.show()
}

fun bookappointment(context:Context,choosen_date:String,rel_problems:String,position:Int,doct_list: List<log_in_model_doctor>,signUpModelObject:sign_up_log_in_model) {

var reqCall=Auth_interface_1.getInstance().check_prev_appointment(signUpModelObject.Phone)
   reqCall.enqueue(object:Callback<List<Appointment_user_doctor>>{
       override fun onResponse(
           call: Call<List<Appointment_user_doctor>>,
           response: Response<List<Appointment_user_doctor>>
       ) {
           Log.i("user_exists",response.body()!!.size.toString())
           if(response.body()!!.size==0){
               Log.i("user_not exist",response.body().toString())
               var doc_list= ArrayList<Doctors>()
               var add_doctor=Doctors(choosen_date,doct_list.get(position).Name,"Not_applicable",rel_problems,"pending",
                   signUpModelObject.Name,doct_list.get(position).DoctorId)
               doc_list.add(add_doctor)
                var obj=Appointment_user_doctor(doc_list,signUpModelObject.Phone)
               var reqCall=Auth_interface_1.getInstance().add_appointment(obj)
               reqCall.enqueue(object :Callback<Appointment_user_doctor>{
                   override fun onResponse(
                       call: Call<Appointment_user_doctor>,
                       response: Response<Appointment_user_doctor>
                   ) {
                       Log.i("check_Post_new",response.toString())
                   }

                   override fun onFailure(call: Call<Appointment_user_doctor>, t: Throwable) {

                   }

               })

           }
           else{
               Log.i("user_exists",response.body().toString())
               var obj=response.body()!![0]
               var doc_list=obj.doctors as MutableList<Doctors>
               var add_doctor=Doctors(choosen_date,doct_list.get(position).Name,"Not_applicable",rel_problems,"pending",
                   signUpModelObject.Name,doct_list.get(position).DoctorId)
               doc_list.add(add_doctor)
               obj.doctors=doc_list


               var reqCall=Auth_interface_1.getInstance().update_appointment_list(signUpModelObject.Phone,obj)
               reqCall.enqueue(object :Callback<Appointment_user_doctor>{
                   override fun onResponse(
                       call: Call<Appointment_user_doctor>,
                       response: Response<Appointment_user_doctor>
                   ) {
                       Log.i("check_Post",response.toString())
                   }

                   override fun onFailure(call: Call<Appointment_user_doctor>, t: Throwable) {
                       Log.i("check_Post_fail",response.toString())
                       Log.i("check_Post_fail",response.errorBody().toString())
                   }

               })




           }


       }

       override fun onFailure(call: Call<List<Appointment_user_doctor>>, t: Throwable) {
           Toast.makeText(context,t.message.toString(),Toast.LENGTH_LONG).show()
       }

   })

    /*
    var add_doctor=Doctors(choosen_date,doct_list.get(position).Name,"Not_applicable",rel_problems,"pending",
                   signUpModelObject.Name,doct_list.get(position).DoctorId)
               doc_list.add(add_doctor)
                var obj=Appointment_user_doctor(doc_list,signUpModelObject.Phone)

    var add_user=Users(choosen_date,doct_list.get(position).Name,"Not_applicable",rel_problems,"pending",
        signUpModelObject.Name,signUpModelObject.Phone)
    var appointmentDoctorUser=Appointment_doctor_user(doct_list.get(position).DoctorId,add_user)
    var reqcall=Auth_interface_1.getInstance().add_appointment_to_doctor()


     */
    var reqcall=Auth_interface_1.getInstance().find_doctor_for_appointment(doct_list.get(position).DoctorId)
    reqcall.enqueue(object :Callback<List<Appointment_doctor_user>>{
        override fun onResponse(
            call: Call<List<Appointment_doctor_user>>,
            response: Response<List<Appointment_doctor_user>>
        ) {
           Log.i("iii","888")

           var userlist=response.body()!![0].users
            var add_user=Users(choosen_date,doct_list.get(position).Name,"Not_applicable",rel_problems,"pending",
                signUpModelObject.Name,signUpModelObject.Phone)
            userlist.add(add_user)
            var appointmentDoctorUser=Appointment_doctor_user(doct_list.get(position).DoctorId,userlist)
            var reqcall=Auth_interface_1.getInstance().add_appointment_to_doctor(doct_list.get(position).DoctorId,appointmentDoctorUser)
            reqcall.enqueue(object :Callback<Appointment_doctor_user>{
                override fun onResponse(
                    call: Call<Appointment_doctor_user>,
                    response: Response<Appointment_doctor_user>
                ) {
                   Toast.makeText(context,"Appointment Requested",Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<Appointment_doctor_user>, t: Throwable) {

                }

            })



        }

        override fun onFailure(call: Call<List<Appointment_doctor_user>>, t: Throwable) {

        }

    })
}

