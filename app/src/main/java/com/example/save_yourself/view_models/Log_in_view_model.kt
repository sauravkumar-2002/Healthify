package com.example.save_yourself.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Log_in_view_model:ViewModel() {
    var password_liveData= MutableLiveData<String>()
    var phone_liveData= MutableLiveData<String>()
}