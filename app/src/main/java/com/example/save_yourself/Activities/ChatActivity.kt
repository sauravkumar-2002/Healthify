package com.example.save_yourself.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.save_yourself.Adapters.Chat_adapter
import com.example.save_yourself.Adapters.dash_patient_adv_adapter
import com.example.save_yourself.Models.Doctors
import com.example.save_yourself.Models.chat_model
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityChatBinding
import com.example.save_yourself.databinding.ActivitySignUpBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var appointment_detail: Doctors
    lateinit var signUpModelObject: sign_up_log_in_model

    var list = ArrayList<chat_model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        var gson = Gson()
        var json = intent.getStringExtra("doctor_detail")
        appointment_detail = gson.fromJson(json, Doctors::class.java)


        binding.send.setOnClickListener {
            sendMsg()
        }
    }


    override fun onResume() {
        super.onResume()
        setSharedpref()

        chatImplementation()
    }


    private fun sendMsg() {
        var reference = FirebaseDatabase.getInstance().getReference("chats")
            .child(signUpModelObject.Phone + appointment_detail.doctor)

        var msg = binding.msg.text.toString()
        if (msg.length == 0) {
            Toast.makeText(this, "enter a message", Toast.LENGTH_LONG).show()
        } else {

            var chat = chat_model(signUpModelObject.Phone, msg)
            reference.push().setValue(chat)
            binding.msg.setText("")
        }
    }


    private fun chatImplementation() {
        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvChats.layoutManager = layoutManager


        val adapter = Chat_adapter(this, list)
        binding.rvChats.adapter = adapter
        binding.rvChats.hasFixedSize()

        binding.userMssg.text = appointment_detail.doctorname


        val reference = FirebaseDatabase.getInstance().getReference("chats")
            .child(signUpModelObject.Phone + appointment_detail.doctor)

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (s in snapshot.children) {
                    val obj: chat_model? = s.getValue(chat_model::class.java)
                    list.add(obj!!)
                }
                adapter.notifyDataSetChanged()

                val x = binding.rvChats.adapter
                binding.rvChats.smoothScrollToPosition(x!!.itemCount)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    private fun setSharedpref() {
        val sp = getSharedPreferences("login", MODE_PRIVATE)
        val gson = Gson()
        val json = sp.getString("signUpModelObject", "")

        signUpModelObject = gson.fromJson(json, sign_up_log_in_model::class.java)
    }
}