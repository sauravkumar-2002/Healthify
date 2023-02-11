package com.example.save_yourself.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityDashboardPatientBinding
import com.example.save_yourself.view_models.Dashboard_patient_view_model
import java.util.*
import androidx.recyclerview.widget.RecyclerView
import com.example.save_yourself.Activities.Patient.*
import com.example.save_yourself.Adapters.dash_patient_adv_adapter
import com.example.save_yourself.Models.sign_up_log_in_model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Dashboard_patient : AppCompatActivity() {
    lateinit var binding:ActivityDashboardPatientBinding
    lateinit var dashboardPatientViewModel: Dashboard_patient_view_model
    lateinit var layoutManager:LinearLayoutManager
    lateinit var adapter:dash_patient_adv_adapter
    lateinit var signUpModelObject: sign_up_log_in_model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_patient)
        dashboardPatientViewModel=ViewModelProvider(this).get(Dashboard_patient_view_model::class.java)
        binding.xmldashboardpatientviewmodel=dashboardPatientViewModel
    }
    override fun onResume() {
        super.onResume()
       // setProgressBar()
        setSharedpref()
        loadAdvertisement();


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.menu_extra, menu)

        // below line is to get our menu item.
        menu.findItem(R.id.notification_tool).setOnMenuItemClickListener {
            var intent=Intent(this,Extra_Appointment::class.java)
            startActivity(intent)
            return@setOnMenuItemClickListener true
        }
        return true
    }

    private fun loadAdvertisement() {
         layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recv.layoutManager = layoutManager
         adapter= dash_patient_adv_adapter(this)
        binding.recv.adapter=adapter
        settimeforauto_scroll()

//Log.i("sign_up_log_in_model",signUpModelObject.Phone)

        //Toast.makeText(this,"load",Toast.LENGTH_LONG).show()
        dashboardPatientViewModel.urllist.observe(this, {
            if (it != null) {
                //  binding.progressBar.visibility = View.GONE
                // binding.progress.visibility = View.GONE
                //Log.i("chec    recv",it.toString())
                //Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                adapter.seturllist(it)

            }
            else{
                //error
                Toast.makeText(this,"eroor",Toast.LENGTH_LONG).show()
            }
        })
        dashboardPatientViewModel.errorMessage.observe(this,{
            Toast.makeText(this,"Eroor due to - "+it.toString()+"\n"+"offline data", Toast.LENGTH_LONG).show()
        })
        dashboardPatientViewModel.check()
    }

    private fun settimeforauto_scroll() {
        var time:Long=4000
        var linearSnapHelper=LinearSnapHelper()
        binding.recv.onFlingListener=null
        linearSnapHelper.attachToRecyclerView(binding.recv)
        var timer= Timer()
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

    fun find_your_doctors(view: android.view.View) {
        var intent=Intent(this, findYourDoctor::class.java)
        startActivity(intent)
    }
    fun active_chats(view: android.view.View) {
        var intent=Intent(this, activeChats::class.java)
        startActivity(intent)
    }
    fun records(view: android.view.View) {
        var intent=Intent(this, records::class.java)
        startActivity(intent)
    }
    fun my_profile(view: android.view.View) {
        var intent=Intent(this, myProfile::class.java)
        startActivity(intent)
    }
    private fun setSharedpref() {
        val sp = getSharedPreferences("login",MODE_PRIVATE)
        var gson= Gson()
        var json=sp.getString("signUpModelObject","")
        val type: Type = object : TypeToken<sign_up_log_in_model>() {}.type

        signUpModelObject=gson.fromJson(json,sign_up_log_in_model::class.java)
    }
}