package com.example.save_yourself.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.save_yourself.MainActivity
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivitySignUpBinding
import com.example.save_yourself.verhoeff
import com.example.save_yourself.view_models.SignUp_view_model
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.io.Serializable
import java.lang.Double.parseDouble
import java.util.concurrent.TimeUnit

class sign_up : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var signupViewModel: SignUp_view_model

    var name_string: String? = null
    var email_string: String? = null
    var password_string: String? = null
    var dob_string: String? = null
    var aadhar_string: String? = null
    var phone_string: String? = null
    var doctorId_string: String? = "Not_Applicable"
    var isCheck: Boolean = true

    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    public var signUpModelObject: sign_up_log_in_model? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        signupViewModel = ViewModelProvider(this)
            .get(SignUp_view_model::class.java)
        binding.xmlsignupviewmodel = signupViewModel
        auth = FirebaseAuth.getInstance()

        signupViewModel.continue_register_liveData.observe(this, {
            if (it == "Clicked") {
                //go to dashboard
                if (phone_string?.let { it1 -> validatePhone(it1) } == true &&
                 aadhar_string?.let { it1 -> validateAadhar_No(it1)} == true &&
                email_string?.let { it1 -> validateEmail(it1) } == true &&
                password_string?.let { it1 -> validatePassword(it1) } == true &&
                 dob_string?.let { it1 -> validateDob(it1) } == true &&
                 doctorId_string?.let { it1 -> validateDoctors_Id(it1) } == true &&
                 name_string?.let { it1 -> validateName(it1) } == true
                ) {
                    var check="true"
                    var ph_no="00"
                    signupViewModel.phone_liveData.observe(this,{
                        signupViewModel.check_if_exists(it)
                    })

                    signupViewModel.if_exist_liveData.observe(this,{
                        if(it=="false"){
                            Toast.makeText(this, "User Already Exists"+it, Toast.LENGTH_SHORT).show()
                           check="false"
                            Log.i("already",check)
                        }
                        else{
                            signUpModelObject = sign_up_log_in_model(
                                name_string!!, dob_string!!, email_string!!, phone_string!!,
                                password_string!!, aadhar_string!!, doctorId_string!!
                            )
                            otp_sending_function()
                        }
                    })
Log.i("already",check)

                }
                /* var intent= Intent(this,MainActivity::class.java)
                 startActivity(intent)

                 */
                else Toast.makeText(this, "Check Credential", Toast.LENGTH_SHORT).show()
            }
        })
        observe_edit_text()

    }

    private fun otp_sending_function() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@sign_up, e.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("checkk ", "onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                val intent = Intent(applicationContext, otp_activity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                intent.putExtra("signUpModelObject", signUpModelObject as Serializable)
                startActivity(intent)
                finish()
            }
        }
        phone_string = "+91$phone_string"

        sendVerificationCode(phone_string!!)

    }

    private fun sendVerificationCode(phoneString: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneString) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("checkk", "Auth started")
    }

    private fun observe_edit_text() {
        signupViewModel.name_liveData.observe(this, {
            name_string = it;
            validateName(it);
        })
        signupViewModel.password_liveData.observe(this, {
            password_string = it;
            validatePassword(it);
        })
        signupViewModel.email_liveData.observe(this, {
            email_string = it
            validateEmail(it);
        })
        signupViewModel.dob_liveData.observe(this, {
            dob_string = it
            // validatePassword(it);
        })
        signupViewModel.doctorId_liveData.observe(this, {
            doctorId_string = it
            // validateName(it);
        })
        signupViewModel.aaddhar_liveData.observe(this, {
            aadhar_string = it
            validateAadhar_No(it);
        })
        signupViewModel.phone_liveData.observe(this, {
            phone_string = it
            //validateName(it);
        })

    }

    private fun validateName(it: String): Boolean {
        if (it == null) {
            return false
        } else {
            var check = it.indexOf(".")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(.)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("/")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(/)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("*")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(*)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("&")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(&)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("%")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(%)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("$")
            if (check != -1) {
                binding.signupName.setError("Invalid Format($)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("#")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(#)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("@")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(@)");
                binding.signupName.requestFocus()
            }
            check = it.indexOf("!")
            if (check != -1) {
                binding.signupName.setError("Invalid Format(!)");
                binding.signupName.requestFocus()
            }
            return true
        }
    }

    private fun validateAadhar_No(it: String): Boolean {
        var res = verhoeff.validateVerhoeff(aadhar_string);
        if (res != true) {
            binding.signupAadharNo.setError("Enter a Valid Aadhar No.")
            return false
        }
        return true
    }

    private fun validatePassword(it: String): Boolean {
        if (it.length < 10) {
            binding.signupPassword.setError("Password Should be of atleast 10 Letters");
            binding.signupPassword.requestFocus()
            return false
        }
        return true
    }

    //validate by api
    private fun validateDoctors_Id(it: String): Boolean {
        if (it == null) {
            return true
        } else {

        }
        return true
    }

    private fun validateDob(it: String): Boolean {
        if (it.length < 10 || it[2] != '/' ||
            it[5] != '/'
        ) {
            binding.signupDob.setError("Check Dob")
            binding.signupDob.requestFocus()
            return false
        }
        return true
    }

    private fun validateEmail(it: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
            binding.signupEmail.setError("Please Enter a Valid Email Id")
            binding.signupEmail.requestFocus()
            return false
        }
        return true
    }

    private fun validatePhone(it: String): Boolean {
        if (it.length < 10 || it.length > 10) {
            binding.signupPhone.setError("Check Phone No.")
            binding.signupPhone.requestFocus()
            return false
        }
        return true
    }


    fun gotoLoginPage(view: android.view.View) {}
}