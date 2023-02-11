package com.example.save_yourself.Services_and_interface

import com.example.save_yourself.Models.prof_img_name
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface profile_img_interface_1 {



    companion object service {
        val URL = "https://hospital-app-production.up.railway.app/api/"

        var retrofitService: Auth_interface_1? = null
        fun getInstance(): Auth_interface_1 {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(Auth_interface_1::class.java)
            }
            return retrofitService!!
        }


    }
}