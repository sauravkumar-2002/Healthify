package com.example.save_yourself.Models

import java.io.Serializable

data class sign_up_log_in_model (
    var Name:String,
    var Dob:String,
    var Email:String,
    var Phone:String,
    var Password:String,
    var Aadhar:String,
    var DoctorId:String
        ):Serializable
/*
Name
D.O.B
Email (Auth)
Phone
Password
Aadhar card  number
Doctor id (only for doct

 */