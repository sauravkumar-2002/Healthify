package com.example.save_yourself.Activities.Doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityAppointmentRequestBinding
import com.example.save_yourself.databinding.ActivityDashboardPatientBinding

class appointment_request : AppCompatActivity() {
    lateinit var binding: ActivityAppointmentRequestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_request)
    }
}