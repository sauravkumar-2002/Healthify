package com.example.save_yourself.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Activities.Doctor.Doctor_records
import com.example.save_yourself.Activities.Doctor.appointment_request
import com.example.save_yourself.Activities.Patient.activeChats
import com.example.save_yourself.Adapters.dash_patient_adv_adapter
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityDashboardDoctorBinding
import com.example.save_yourself.view_models.Dashboard_patient_view_model
import java.util.*

class Dashboard_doctor : AppCompatActivity() {
    lateinit var binding: ActivityDashboardDoctorBinding
    lateinit var dashboardPatientViewModel: Dashboard_patient_view_model
    lateinit var doc_id_shared_pref: String
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: dash_patient_adv_adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_doctor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setSupportActionBar(binding.myToolbar)

        dashboardPatientViewModel =
            ViewModelProvider(this).get(Dashboard_patient_view_model::class.java)

    }

    override fun onResume() {
        super.onResume()
        // setProgressBar()
        setSharedpref()
        loadAdvertisement();


    }


    private fun loadAdvertisement() {
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recv.layoutManager = layoutManager

        adapter = dash_patient_adv_adapter(this)
        binding.recv.adapter = adapter
        settimeforauto_scroll()


        dashboardPatientViewModel.urllist.observe(this, {
            if (it != null) {

                adapter.seturllist(it)

            } else {
                //error
                Toast.makeText(this, "eroor", Toast.LENGTH_LONG).show()
            }
        })
        dashboardPatientViewModel.errorMessage.observe(this, {
            Toast.makeText(this, "Eroor due to - Server", Toast.LENGTH_LONG).show()
        })
        dashboardPatientViewModel.check()
    }

    private fun settimeforauto_scroll() {
        var time: Long = 9000
        var linearSnapHelper = LinearSnapHelper()

        binding.recv.onFlingListener = null
        linearSnapHelper.attachToRecyclerView(binding.recv)

        var timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (layoutManager.findLastCompletelyVisibleItemPosition() < adapter.getItemCount() - 1) {
                    layoutManager.smoothScrollToPosition(
                        binding.recv,
                        RecyclerView.State(),
                        layoutManager.findLastCompletelyVisibleItemPosition() + 1
                    )
                } else if (layoutManager.findLastCompletelyVisibleItemPosition() === adapter.getItemCount() - 1) {
                    layoutManager.smoothScrollToPosition(
                        binding.recv,
                        RecyclerView.State(),
                        0
                    )
                }
            }
        }, 0L, time)
    }


    fun appoint_request(view: android.view.View) {
        val intent = Intent(this, appointment_request::class.java)
        startActivity(intent)
    }


    private fun setSharedpref() {
        val sp = getSharedPreferences("login", MODE_PRIVATE)

        doc_id_shared_pref = sp.getString("doctor_id", "").toString()
        binding.showName.text = "Heyy, " + doc_id_shared_pref

    }
}