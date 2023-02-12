package com.example.save_yourself.Auth

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    lateinit var binding: ActivityLogInBinding
    lateinit var logInViewModel: Log_in_view_model
    lateinit var dialog: BottomSheetDialog
    lateinit var docid: EditText
    lateinit var sharedPref: SharedPreferences
    var signUpModelObject: sign_up_log_in_model? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        logInViewModel = ViewModelProvider(this)
            .get(Log_in_view_model::class.java)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        binding.xmlloginviewmodel = logInViewModel



        binding.proggressBar.visibility = View.GONE

        sharedPref = getSharedPreferences("login", MODE_PRIVATE)
        if (sharedPref.getBoolean("logged", false)) go_to_patientDashbord()


        binding.continueButton.setOnClickListener {
            binding.proggressBar.visibility = View.VISIBLE

            if (validatePhone(binding.loginPhone.text.toString().trim()))
                validatePhoneandPassword()
        }


        binding.newUser.setOnClickListener {
            val intent = Intent(this@Log_in, sign_up::class.java)
            startActivity(intent)
        }


        binding.loginAsDoctor.setOnClickListener {
            dialog = BottomSheetDialog(this)

            val view = layoutInflater.inflate(R.layout.doctors_login, null)
            var cont = view.findViewById<Button>(R.id.continue_button_doctor)

            docid = view.findViewById<EditText>(R.id.login_doctor_id)


            cont.setOnClickListener {
                if (docid.text.toString().trim().length > 0)
                    validateDoctorId(docid.text.toString().trim())
                else {
                    Toast.makeText(this, "Fill The Doctor Id", Toast.LENGTH_LONG).show()
                }

            }


            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()

        }


        logInViewModel.phone_liveData.observe(this, {
            if (it.length > 0)
                validatePhone(it)
        })


    }


    private fun validateDoctorId(doctorid: String) {

        var reqcall = Auth_interface_1.getInstance().log_in_doctor(doctorid)
        reqcall.enqueue(object : Callback<List<log_in_model_doctor>> {

            override fun onResponse(
                call: Call<List<log_in_model_doctor>>,
                response: Response<List<log_in_model_doctor>>
            ) {
                if (response.isSuccessful) {

                    Toast.makeText(this@Log_in, "User Register Successfully", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(this@Log_in, Dashboard_doctor::class.java)
                    sharedPref.edit().putString("doctor_id", doctorid).apply()

                    startActivity(intent)
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<List<log_in_model_doctor>>, t: Throwable) {

                Toast.makeText(this@Log_in, "Check Your Doctor Id", Toast.LENGTH_LONG).show()

                docid.setError("invalid DoctorId")
                docid.requestFocus()
            }

        })

    }

    private fun validatePhoneandPassword() {

        if (binding.loginPhone.equals("") || binding.loginPassword.equals("")) {

            binding.proggressBar.visibility = View.GONE
            Toast.makeText(this@Log_in, "Check credentials", Toast.LENGTH_LONG).show()

        } else {

            val reqcall = Auth_interface_1.getInstance().log_in_user(

                binding.loginPhone.text.toString().trim(),
                binding.loginPassword.text.toString().trim()
            )
            reqcall.enqueue(object : Callback<List<sign_up_log_in_model>> {
                override fun onResponse(
                    call: Call<List<sign_up_log_in_model>>,
                    response: Response<List<sign_up_log_in_model>>
                ) {
                    if (response.isSuccessful) {
                        binding.proggressBar.visibility = View.GONE
                        Toast.makeText(this@Log_in, "User Register Successfully", Toast.LENGTH_LONG)
                            .show()


                        signUpModelObject = response.body()!![0]

                        val gson = Gson()
                        sharedPref.edit().putString(
                            "signUpModelObject",
                            gson.toJson(signUpModelObject).toString()
                        ).apply()

                        sharedPref.edit().putBoolean("logged", true).apply()
                        go_to_patientDashbord()

                    }

                }

                override fun onFailure(call: Call<List<sign_up_log_in_model>>, t: Throwable) {

                    Toast.makeText(this@Log_in, "Check Your Credential", Toast.LENGTH_LONG).show()

                    binding.proggressBar.visibility = View.GONE

                    binding.loginPhone.requestFocus()
                    binding.loginPassword.requestFocus()
                }

            })
        }
    }

    private fun go_to_patientDashbord() {

        val intent = Intent(this@Log_in, Dashboard_patient::class.java)
        startActivity(intent)
        finish()
    }

    private fun validatePhone(it: String): Boolean {
        if ((it.length in 1..9) || it.length > 10) {

            binding.loginPhone.setError("Check Phone No.")
            binding.proggressBar.visibility = View.GONE
            binding.loginPhone.requestFocus()

            return false
        } else if ((it.length < 10)) {

            Toast.makeText(this, "Fill The Phone No.", Toast.LENGTH_LONG).show()
            binding.proggressBar.visibility = View.GONE
            return false
        }

        return true
    }
}