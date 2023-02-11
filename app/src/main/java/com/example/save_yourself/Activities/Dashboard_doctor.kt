package com.example.save_yourself.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityDashboardDoctorBinding

class Dashboard_doctor : AppCompatActivity() {
    lateinit var binding:ActivityDashboardDoctorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_dashboard_doctor)
    }

    fun records(view: android.view.View) {}
    fun appoint_request(view: android.view.View) {}
}