package com.example.save_yourself.Activities.Patient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.save_yourself.Adapters.find_your_doc_adapter
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityFindYourDoctorBinding
import com.example.save_yourself.view_models.patientViewModel.Find_your_doctor_view_model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class findYourDoctor : AppCompatActivity() {
    lateinit var binding:ActivityFindYourDoctorBinding
    lateinit var signUpModelObject: sign_up_log_in_model
    lateinit var findYourDoctorViewModel: Find_your_doctor_view_model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_find_your_doctor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_your_doctor)
        findYourDoctorViewModel = ViewModelProvider(this)
            .get(Find_your_doctor_view_model::class.java)
        binding.xmlfindyourdoctorviewmodel=findYourDoctorViewModel

    }

    override fun onResume() {
        super.onResume()
        setSharedpref()
        loadDoctorList()

    }

    // calling on create option menu
    // layout to inflate our menu file.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.searchtoolbar, menu)

        // below line is to get our menu item.
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)

        // getting search view of our item.
        val searchView: SearchView = searchItem.getActionView() as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                findYourDoctorViewModel.getDoctor_search_name(msg)
                return false
            }
        })
        return true
    }



    private fun loadDoctorList() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.doctorsListRecv.layoutManager = layoutManager
        var adapter= find_your_doc_adapter(this,signUpModelObject)
        binding.doctorsListRecv.adapter=adapter
        binding.doctorsListRecv.visibility=View.VISIBLE
        binding.errorImage.visibility=View.GONE
        binding.errorText.visibility=View.GONE
        findYourDoctorViewModel.doc_list.observe(this,{
            adapter.setDoctorList(it)
            Log.i("check Api",it.toString())
            binding.doctorsListRecv.visibility=View.VISIBLE
            binding.errorImage.visibility=View.GONE
            binding.errorText.visibility=View.GONE
        })
        findYourDoctorViewModel.errorMessage.observe(this, {
//change toast to no such data
            Toast.makeText(this,"Eroor due to - "+it.toString()+"\n"+"offline data", Toast.LENGTH_LONG).show()
            binding.doctorsListRecv.visibility=View.GONE
            binding.errorImage.visibility=View.VISIBLE
            binding.errorText.visibility=View.VISIBLE

        })
        findYourDoctorViewModel.getDoctor_list()
    }
    private fun setSharedpref() {
        val sp = getSharedPreferences("login",MODE_PRIVATE)
        var gson= Gson()
        var json=sp.getString("signUpModelObject","")
        signUpModelObject=gson.fromJson(json, sign_up_log_in_model::class.java)
    }
}