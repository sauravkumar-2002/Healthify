package com.example.save_yourself.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.save_yourself.Models.Doctors
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityAppointmentBinding
import com.example.save_yourself.databinding.ActivityLogInBinding
import com.google.gson.Gson

class Appointment_Activity : AppCompatActivity() {
    lateinit var binding: ActivityAppointmentBinding
    lateinit var appointment_detail:Doctors
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_appointment)
        var gson= Gson()
        var json=intent.getStringExtra("doctor_detail")
        appointment_detail=gson.fromJson(json,Doctors::class.java)
        binding.details.text=appointment_detail.toString()
        binding.chats.setOnClickListener {
            var intent=Intent(this,ChatActivity::class.java)
            intent.putExtra("doctor_detail",json)
            startActivity(intent)
        }
    }
}