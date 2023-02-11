package com.example.save_yourself.Services_and_interface

import com.example.save_yourself.Activities.Patient.findYourDoctor
import com.example.save_yourself.Models.Appointment_doctor_user
import com.example.save_yourself.Models.Appointment_user_doctor
import com.example.save_yourself.Models.log_in_model_doctor
import com.example.save_yourself.Models.sign_up_log_in_model
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Auth_interface_1 {

    @GET("register")
    fun log_in_user(@Query("Phone")Phone:String,@Query("Password")Password:String):Call<List<sign_up_log_in_model>>
    @GET("doctor")
    fun log_in_doctor(@Query("DoctorId")DoctorId:String):Call<List<log_in_model_doctor>>
    @GET("doctor")
    fun get_doctor_list():Call<List<log_in_model_doctor>>
    @GET("doctor")
    fun get_doctor_search_name(@Query("Name")Name:String,@Query("Speciality")Speciality:String):Call<List<log_in_model_doctor>>

    @POST("register")
    fun add_sign_up_user(@Body userData:sign_up_log_in_model): Call<sign_up_log_in_model>

    @PATCH("register/{Phone}")
    suspend fun update_image(@Query("Phone")Phone:String,@Body userData:String):Call<List<sign_up_log_in_model>>//change to list

    @GET("register")
    fun check_existing_user(@Query("Phone")Phone:String):Call<List<sign_up_log_in_model>>

    @POST("appointments/userdoctor")
    fun add_appointment(@Body userData:Appointment_user_doctor):Call<Appointment_user_doctor>

    @PATCH("appointments/doctoruser/{doctor}")
    fun add_appointment_to_doctor(@Path("doctor")doctor:String,@Body userData:Appointment_doctor_user):Call<Appointment_doctor_user>

    @GET("appointments/doctoruser")
    fun find_doctor_for_appointment(@Query("doctor")doctor: String):Call<List<Appointment_doctor_user>>

    @PATCH("appointments/userdoctor/{user}")
     fun update_appointment_list(@Path("user")user:String,@Body userData:Appointment_user_doctor):Call<Appointment_user_doctor>//change to list



    @GET("appointments/userdoctor")
    fun check_prev_appointment(@Query("user")user:String):Call<List<Appointment_user_doctor>>



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