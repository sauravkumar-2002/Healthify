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
    var list= ArrayList<chat_model>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        var gson= Gson()
        var json=intent.getStringExtra("doctor_detail")
        appointment_detail=gson.fromJson(json,Doctors::class.java)
        setSharedpref()
        Log.i("sharefb",appointment_detail.doctorname)
    chatImplementation()
        binding.send.setOnClickListener {
            sendMsg()
        }
    }

    private fun sendMsg() {
        var reference=FirebaseDatabase.getInstance().getReference("chats").child(signUpModelObject.Phone+appointment_detail.doctor)

        var msg=binding.msg.text.toString()
        if(msg.length==0){
            Toast.makeText(this,"enter a message",Toast.LENGTH_LONG).show()
        }
        else{
            Log.i("msgsent",msg.toString())
            var chat=chat_model(signUpModelObject.Phone,msg)
            reference.push().setValue(chat)
            binding.msg.setText("")
        }
    }

    private fun chatImplementation() {
        var layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvChats.layoutManager = layoutManager
        var adapter= Chat_adapter(this,list)
        binding.rvChats.adapter=adapter
        binding.rvChats.hasFixedSize()
        binding.userMssg.text=appointment_detail.doctorname
        Log.i("checkFirebase","check")
        var reference=FirebaseDatabase.getInstance().getReference("chats").child(signUpModelObject.Phone+appointment_detail.doctor)
        Log.i("checkFirebase",reference.toString())
        reference.addValueEventListener(object :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                   list.clear()
                    Log.i("checkFirebase",snapshot.toString())
                    for (s in snapshot.children){
                    var obj:chat_model?=s.getValue(chat_model::class.java)
                        list.add(obj!!)
                    }
                    adapter.notifyDataSetChanged()
                    var x=binding.rvChats.adapter
                    binding.rvChats.smoothScrollToPosition(x!!.itemCount)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("checkFirebase",error.toString())
                }

            })
    }
    private fun setSharedpref() {
        val sp = getSharedPreferences("login",MODE_PRIVATE)
        var gson= Gson()
        var json=sp.getString("signUpModelObject","")
        val type: Type = object : TypeToken<sign_up_log_in_model>() {}.type

        signUpModelObject=gson.fromJson(json, sign_up_log_in_model::class.java)
    }
}