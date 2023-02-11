package com.example.save_yourself.Activities.Doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.save_yourself.Adapters.dash_patient_adv_adapter
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityDashboardPatientBinding
import com.example.save_yourself.databinding.ActivityDoctorRecordsBinding

class Doctor_records : AppCompatActivity() {
    lateinit var binding: ActivityDoctorRecordsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_records)

        loadRecycler()
    }

    private fun loadRecycler() {
       var  layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recv.layoutManager = layoutManager
        var adapter= dash_patient_adv_adapter(this)
        binding.recv.adapter=adapter
    }
}