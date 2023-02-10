package com.example.save_yourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.save_yourself.Auth.Log_in
import com.example.save_yourself.Auth.sign_up

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gotoSignup(view: android.view.View) {
        var intent=Intent(this,Log_in::class.java)
        startActivity(intent)
    }
}