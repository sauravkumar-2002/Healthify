package com.example.save_yourself.Models

data class log_in_model_doctor (

    var Name:String,
    var Dob:String,
    var Email:String,
    var Phone:String,
    var Password:String,
    var Aadhar:String,
    var DoctorId:String,
    var Certificate:String,
    var Rating:String,
    var Speciality:List<String>
        )