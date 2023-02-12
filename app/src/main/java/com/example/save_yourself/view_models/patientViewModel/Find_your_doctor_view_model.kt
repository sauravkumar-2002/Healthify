package com.example.save_yourself.view_models.patientViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_yourself.Models.log_in_model_doctor
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Find_your_doctor_view_model : ViewModel() {

    var doc_list = MutableLiveData<List<log_in_model_doctor>>();
    var errorMessage = MutableLiveData<String>()


    fun getDoctor_list() {
        var reqcall = Auth_interface_1.getInstance().get_doctor_list()
        reqcall.enqueue(object : Callback<List<log_in_model_doctor>> {
            override fun onResponse(
                call: Call<List<log_in_model_doctor>>,
                response: Response<List<log_in_model_doctor>>
            ) {

                doc_list.postValue(response.body())
            }

            override fun onFailure(call: Call<List<log_in_model_doctor>>, t: Throwable) {
                errorMessage.postValue(t.message.toString())
            }

        })
    }


    fun getDoctor_search_name(msg: String) {

        var reqcall = Auth_interface_1.getInstance().get_doctor_search_name(msg, msg)

        reqcall.enqueue(object : Callback<List<log_in_model_doctor>> {
            override fun onResponse(
                call: Call<List<log_in_model_doctor>>,
                response: Response<List<log_in_model_doctor>>
            ) {
                doc_list.postValue(response.body())
            }

            override fun onFailure(call: Call<List<log_in_model_doctor>>, t: Throwable) {
                errorMessage.postValue(t.message.toString())
            }

        })
    }
}