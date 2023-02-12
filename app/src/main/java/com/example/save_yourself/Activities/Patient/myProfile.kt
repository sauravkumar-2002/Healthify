package com.example.save_yourself.Activities.Patient

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.save_yourself.Auth.Log_in
import com.example.save_yourself.Models.prof_img_name
import com.example.save_yourself.R
import com.example.save_yourself.Services_and_interface.Auth_interface_1
import com.example.save_yourself.databinding.ActivityLogInBinding
import com.example.save_yourself.databinding.ActivityMyProfileBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import com.example.save_yourself.MainActivity

import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.save_yourself.Models.sign_up_log_in_model
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.lang.ref.Cleaner.create
import java.lang.reflect.Type
import java.net.URI.create

@RequiresApi(Build.VERSION_CODES.P)
class myProfile : AppCompatActivity() {
    lateinit var binding: ActivityMyProfileBinding
     lateinit var imageUri:Uri
    lateinit var signUpModelObject: sign_up_log_in_model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setSupportActionBar(findViewById(R.id.my_toolbar))

        setSharedpref()
        upload("888")
        binding.browse.setOnClickListener {
          contract.launch("image/*")

        }



        binding.logout.setOnClickListener {
            val sp = getSharedPreferences("login",MODE_PRIVATE)
            sp.edit().putBoolean("logged", false).apply()
            val i = Intent(this, Log_in::class.java)
            startActivity(i)
        }


    }

    private fun setSharedpref() {
        val sp = getSharedPreferences("login",MODE_PRIVATE)
        var gson= Gson()
        var json=sp.getString("signUpModelObject","")

        signUpModelObject=gson.fromJson(json,sign_up_log_in_model::class.java)

    }

    private fun upload(img_base64: String?) {
        signUpModelObject.DoctorId=img_base64!!
        binding.name.text=signUpModelObject.Name
        binding.phone.text=signUpModelObject.Phone
        binding.password.text=signUpModelObject.Password
        binding.aadhar.text=signUpModelObject.Aadhar
        binding.email.text=signUpModelObject.Email
        binding.dob.text=signUpModelObject.Dob
    }

    private fun imageuritoBitmap(imageUri: Uri):Bitmap{
        val source = ImageDecoder.createSource(this.contentResolver, imageUri)
        val bitmap = ImageDecoder.decodeBitmap(source)
       // bitmap.compress(Bitmap.CompressFormat.JPEG,60,ByteArrayOutputStream())
        return  bitmap

    }
    private fun encodeImageToBase64(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    private fun decodeBase64ToBitmap(base64String:String):Bitmap{
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return decodedImage
    }
     val contract=registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.browse.setImageURI(it)
        imageUri=it!!
    }
}
