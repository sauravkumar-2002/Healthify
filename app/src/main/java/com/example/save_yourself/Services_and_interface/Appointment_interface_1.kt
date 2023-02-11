package com.example.save_yourself.Services_and_interface

import com.example.save_yourself.Models.sign_up_log_in_model
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Appointment_interface_1 {
    @POST("register")
    fun add_sign_up_user(@Body userData: sign_up_log_in_model): Call<sign_up_log_in_model>
}