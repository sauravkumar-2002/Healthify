package com.example.save_yourself.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.save_yourself.Activities.Dashboard_doctor
import com.example.save_yourself.Activities.Dashboard_patient
import com.example.save_yourself.MainActivity
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable


class otp_activity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var signUpModelObject: sign_up_log_in_model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        auth = FirebaseAuth.getInstance()

        // get storedVerificationId from the intent
        val storedVerificationId = intent.getStringExtra("storedVerificationId")
         signUpModelObject = intent.getSerializableExtra("signUpModelObject") as sign_up_log_in_model
        Toast.makeText(this, signUpModelObject.toString(), Toast.LENGTH_LONG).show()
        Log.i("object",signUpModelObject.toString())
        findViewById<Button>(R.id.login).setOnClickListener {
            val otp = findViewById<EditText>(R.id.et_otp).text.trim().toString()
            if (otp.isNotEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
///post data to api
                    postSignUpData()
                    val sharedPref = getSharedPreferences("login",MODE_PRIVATE)
                    var gson= Gson()
                    sharedPref.edit().putString("signUpModelObject",gson.toJson(signUpModelObject).toString()).apply()
                    sharedPref.edit().putBoolean("logged",true).apply()
                        val intent = Intent(this, Dashboard_patient::class.java)
                        startActivity(intent)
                        finish()

                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun postSignUpData() {
     var reqcall= Auth_interface_1.getInstance().add_sign_up_user(signUpModelObject)
    reqcall.enqueue(object :Callback<sign_up_log_in_model>{
        override fun onResponse(
            call: Call<sign_up_log_in_model>,
            response: Response<sign_up_log_in_model>
        ) {
            Toast.makeText(this@otp_activity,"User Added Successfully",Toast.LENGTH_LONG).show()
            Log.i("checkApi","donrrrrrrrrrr")
        }

        override fun onFailure(call: Call<sign_up_log_in_model>, t: Throwable) {
            Log.i("checkApi",t.message.toString())
        }


    })
    }
}

