package com.example.save_yourself.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.save_yourself.Adapters.Chat_adapter
import com.example.save_yourself.Adapters.Chat_adapter_Doctor
import com.example.save_yourself.Models.Doctors
import com.example.save_yourself.Models.Users
import com.example.save_yourself.Models.chat_model
import com.example.save_yourself.Models.sign_up_log_in_model
import com.example.save_yourself.R
import com.example.save_yourself.databinding.ActivityChatBinding
import com.example.save_yourself.databinding.ActivityChatDoctorBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ChatActivityDoctor : AppCompatActivity() {
    lateinit var binding: ActivityChatDoctorBinding
    lateinit var appointment_detail: Users
    var list = ArrayList<chat_model>()
    lateinit var doct_id_shared: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_doctor)

    }


    override fun onResume() {
        super.onResume()
        var gson = Gson()
        var json = intent.getStringExtra("doctor_detail")
        appointment_detail = gson.fromJson(json, Users::class.java)

        setSharedpref()
        chatImplementation()

        binding.send.setOnClickListener {
            sendMsg()
        }
    }


    private fun sendMsg() {
        var reference = FirebaseDatabase.getInstance().getReference("chats")
            .child(appointment_detail.user + doct_id_shared)

        var msg = binding.msg.text.toString()
        if (msg.length == 0) {
            Toast.makeText(this, "enter a message", Toast.LENGTH_LONG).show()
        } else {

            var chat = chat_model(doct_id_shared, msg)
            reference.push().setValue(chat)
            binding.msg.setText("")
        }
    }

    private fun chatImplementation() {
        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvChats.layoutManager = layoutManager

        var adapter = Chat_adapter_Doctor(this, list)
        binding.rvChats.adapter = adapter
        binding.rvChats.hasFixedSize()
        binding.userMssg.text = appointment_detail.username


        var reference = FirebaseDatabase.getInstance().getReference("chats")
            .child(appointment_detail.user + doct_id_shared)


        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()

                for (s in snapshot.children) {
                    var obj: chat_model? = s.getValue(chat_model::class.java)
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
        var gson = Gson()
        doct_id_shared = sp.getString("doctor_id", "").toString()
    }
}