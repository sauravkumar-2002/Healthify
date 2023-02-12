package com.example.save_yourself.Services_and_interface

import com.example.save_yourself.Models.url_model
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Adv_interface_1 {
    @GET("quotes")
    fun getImageUrl(): Call<List<url_model>>


    companion object {
        val URL = "https://hospital-app-production.up.railway.app/api/"


        var retrofitService: Adv_interface_1? = null
        fun getInstance(): Adv_interface_1 {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(Adv_interface_1::class.java)
            }
            return retrofitService!!
        }

    }
}