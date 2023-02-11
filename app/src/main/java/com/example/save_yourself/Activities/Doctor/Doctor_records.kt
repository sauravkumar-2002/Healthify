package com.example.save_yourself.Activities.Doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.save_yourself.Adapters.Doc_record_adapter
import com.example.save_yourself.Adapters.dash_patient_adv_adapter
import com.example.save_yourself.Models.Appointment_doctor_user
import com.example.save_yourself.Models.Appointment_user_doctor
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.example.save_yourself.databinding.ActivityDashboardPatientBinding
import com.example.save_yourself.databinding.ActivityDoctorRecordsBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Doctor_records : AppCompatActivity() {
    lateinit var binding: ActivityDoctorRecordsBinding
    lateinit var doc_id_shared_pref:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_records)


    }
    override fun onResume() {
        super.onResume()
        setSharedpref()
        loadRecycler()
    }
    private fun loadRecycler() {
       var  layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recv.layoutManager = layoutManager
        var adapter= Doc_record_adapter(this)
        binding.recv.adapter=adapter

        var reqCall= Auth_interface_1.getInstance().find_doctor_for_appointment(doc_id_shared_pref)
        reqCall.enqueue(object : Callback<List<Appointment_doctor_user>> {
            override fun onResponse(
                call: Call<List<Appointment_doctor_user>>,
                response: Response<List<Appointment_doctor_user>>
            ) {
                adapter.setUserList(response.body()!![0].users,"in progress")
                Log.i("check_records_doc",response.body().toString())
            }

            override fun onFailure(call: Call<List<Appointment_doctor_user>>, t: Throwable) {
                Log.i("check_records",t.message.toString())
            }

        })

    }
    private fun setSharedpref() {
        val sp = getSharedPreferences("login",MODE_PRIVATE)

        doc_id_shared_pref= sp.getString("doctor_id","").toString()

    }
}