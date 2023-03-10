package com.example.save_yourself.Activities.Patient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.save_yourself.Adapters.patient_records_adapter
import com.example.save_yourself.Models.Appointment_user_doctor
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.example.save_yourself.databinding.ActivityActiveChatsBinding
import com.example.save_yourself.databinding.ActivityRecordsBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activeChats : AppCompatActivity() {
    lateinit var binding: ActivityActiveChatsBinding
    lateinit var signUpModelObject: sign_up_log_in_model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_active_chats)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setSupportActionBar(binding.myToolbar)
    }


    override fun onResume() {
        super.onResume()
        setSharedpref()
        loadRecyclerview()
    }


    private fun loadRecyclerview() {
        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recv.layoutManager = layoutManager

        var adapter= patient_records_adapter(this)
        binding.recv.adapter=adapter

        var reqCall= Auth_interface_1.getInstance().check_prev_appointment(signUpModelObject.Phone)
        reqCall.enqueue(object : Callback<List<Appointment_user_doctor>> {
            override fun onResponse(
                call: Call<List<Appointment_user_doctor>>,
                response: Response<List<Appointment_user_doctor>>
            ) {
                if(response.body()!!.size>0) {
                    adapter.setDoctorList(response.body()!![0].doctors, "in progress")
                }

            }

            override fun onFailure(call: Call<List<Appointment_user_doctor>>, t: Throwable) {

            }

        })

    }


    private fun setSharedpref() {
        val sp = getSharedPreferences("login",MODE_PRIVATE)
        var gson= Gson()
        var json=sp.getString("signUpModelObject","")

        signUpModelObject=gson.fromJson(json,sign_up_log_in_model::class.java)

    }
}