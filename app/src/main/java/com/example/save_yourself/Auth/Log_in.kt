package com.example.save_yourself.Auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.save_yourself.Activities.Dashboard_doctor
import com.example.save_yourself.Activities.Dashboard_patient
import com.example.save_yourself.Models.log_in_model_doctor
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.example.save_yourself.databinding.ActivityLogInBinding
import com.example.save_yourself.view_models.Log_in_view_model
import com.example.save_yourself.view_models.SignUp_view_model
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.lang.ProcessBuilder.Redirect.to

class Log_in : AppCompatActivity() {
    lateinit var binding:ActivityLogInBinding
    lateinit var logInViewModel: Log_in_view_model
    lateinit var dialog:BottomSheetDialog
    lateinit var docid:EditText
    lateinit var  sharedPref:SharedPreferences
    public var signUpModelObject: sign_up_log_in_model?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        logInViewModel = ViewModelProvider(this)
            .get(Log_in_view_model::class.java)
        binding.xmlloginviewmodel = logInViewModel
        sharedPref=getSharedPreferences("login", MODE_PRIVATE)
        if(sharedPref.getBoolean("logged",false))go_to_patientDashbord()
        binding.continueButton.setOnClickListener {
            validatePhoneandPassword()
        }
        binding.newUser.setOnClickListener {
            val intent = Intent(this@Log_in,sign_up::class.java)
            startActivity(intent)
        }
        binding.loginAsDoctor.setOnClickListener {
            dialog= BottomSheetDialog(this)
            val view=layoutInflater.inflate(R.layout.doctors_login,null)
            var cont=view.findViewById<Button>(R.id.continue_button_doctor)
            docid=view.findViewById<EditText>(R.id.login_doctor_id)
            Log.i("edittext",docid.toString().trim())
            cont.setOnClickListener {
                validateDoctorId(docid.text.toString().trim())
            // dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
        logInViewModel.phone_liveData.observe(this,{
            validatePhone(it)
        })
    }

    private fun validateDoctorId(doctorid:String) {

        var reqcall=Auth_interface_1.getInstance().log_in_doctor(doctorid)
        reqcall.enqueue(object: Callback<List<log_in_model_doctor>>{

            override fun onResponse(
                call: Call<List<log_in_model_doctor>>,
                response: Response<List<log_in_model_doctor>>
            ) {
                if(response.isSuccessful) {
                    Toast.makeText(this@Log_in, "User Register Successfully", Toast.LENGTH_LONG)
                        .show()
                    Log.i("checkApi_loginn", response.body().toString())
                    val intent = Intent(this@Log_in, Dashboard_doctor::class.java)

                    startActivity(intent)
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<List<log_in_model_doctor>>, t: Throwable) {
                Log.i("checkApi_login",t.message.toString())
                Toast.makeText(this@Log_in,"Check Your Doctor Id", Toast.LENGTH_LONG).show()
                docid.setError("invalid DoctorId")
                docid.requestFocus()
            }

        })

    }

    private fun validatePhoneandPassword() {

        var reqcall=Auth_interface_1.getInstance().log_in_user(binding.loginPhone.text.toString().trim(),binding.loginPassword.text.toString().trim())
        reqcall.enqueue(object: Callback<List<sign_up_log_in_model>>{
            override fun onResponse(
                call: Call<List<sign_up_log_in_model>>,
                response: Response<List<sign_up_log_in_model>>
            ) {
                if(response.isSuccessful) {
                    Toast.makeText(this@Log_in, "User Register Successfully", Toast.LENGTH_LONG)
                        .show()
                    Log.i("checkApi_loginn", response.body().toString())

                    signUpModelObject=response.body()!![0]
                    val gson=Gson()
                    Log.i("checkApi_login",gson.toJson(signUpModelObject).toString())
                    sharedPref.edit().putString("signUpModelObject",gson.toJson(signUpModelObject).toString()).apply()
                    sharedPref.edit().putBoolean("logged",true).apply()
                    go_to_patientDashbord()

                }
            }

            override fun onFailure(call: Call<List<sign_up_log_in_model>>, t: Throwable) {
                Log.i("checkApi_login",t.message.toString())
                Toast.makeText(this@Log_in,"Check Your Credential", Toast.LENGTH_LONG).show()
                binding.loginPhone.clearFocus()
                binding.loginPassword.clearFocus()
            }

        })
    }

    private fun go_to_patientDashbord() {
       /* var gson=Gson()

        var json=sharedPref.getString("signUpModelObject",null)
        if(json!=null)
        signUpModelObject=gson.fromJson(json,sign_up_log_in_model::class.java)

        */
        val intent = Intent(this@Log_in, Dashboard_patient::class.java)
       // intent.putExtra("signUpModelObject", signUpModelObject as Serializable)
        startActivity(intent)

        finish()
    }

    private fun validatePhone(it: String): Boolean {
        if (it.length < 10 || it.length > 10) {
            binding.loginPhone.setError("Check Phone No.")
            binding.loginPhone.requestFocus()
            return false
        }
        return true
    }
}