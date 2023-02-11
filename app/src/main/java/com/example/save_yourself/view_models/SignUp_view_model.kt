package com.example.save_yourself.view_models

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp_view_model:ViewModel() {
var name_liveData= MutableLiveData<String>()
var email_liveData= MutableLiveData<String>()
var password_liveData= MutableLiveData<String>()
var dob_liveData= MutableLiveData<String>()
var phone_liveData= MutableLiveData<String>()
var aaddhar_liveData= MutableLiveData<String>()
var doctorId_liveData= MutableLiveData<String>()
    var if_exist_liveData=MutableLiveData<String>("true")
var continue_register_liveData= MutableLiveData<String>("Not_Clicked")
    fun continue_register(){
       continue_register_liveData.value="Clicked"
    }
    fun check_if_exists(it:String){

        var reqcall= Auth_interface_1.getInstance().check_existing_user(it)
        reqcall.enqueue(object: Callback<List<sign_up_log_in_model>> {
            override fun onResponse(
                call: Call<List<sign_up_log_in_model>>,
                response: Response<List<sign_up_log_in_model>>
            ) {
                if(response.isSuccessful) {
                    if_exist_liveData.postValue("false")
                }
            }

            override fun onFailure(call: Call<List<sign_up_log_in_model>>, t: Throwable) {
                if_exist_liveData.postValue("true")
            }

        })
    }
}