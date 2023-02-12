package com.example.save_yourself.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_yourself.Models.url_model
import com.example.save_yourself.Services_and_interface.Adv_interface_1
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard_patient_view_model:ViewModel() {
    var urllist=MutableLiveData<List<url_model>>()
    var errorMessage = MutableLiveData<String>()


      fun check(){
        var reqcall= Adv_interface_1.getInstance().getImageUrl()
          reqcall.enqueue(object :Callback<List<url_model>>{
              override fun onResponse(
                  call: Call<List<url_model>>,
                  response: Response<List<url_model>>
              ) {
                  urllist.postValue(response.body())
              }

              override fun onFailure(call: Call<List<url_model>>, t: Throwable) {
                  errorMessage.postValue(t.message.toString())
              }

          })
      }

}