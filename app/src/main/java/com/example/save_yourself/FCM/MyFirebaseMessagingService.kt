package com.example.save_yourself.FCM

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.save_yourself.Activities.Dashboard_patient
import com.example.save_yourself.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage

const val channelId="channelid"
const val channelName="chanenelname"
class MyFirebaseMessagingService:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
       if(message!=null){
           generateNotification(message.notification!!.title!!,message.notification!!.body!!)
       }
    }
fun generateNotification(title:String,msg:String){
    val intent= Intent(this,Dashboard_patient::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
    //chaneel id,name
    var builder=NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.drawable.ic_baseline_double_arrow_24)
        .setAutoCancel(true)
        .setVibrate(longArrayOf(1000,1000,1000,1000))
        .setOnlyAlertOnce(true)
        .setContentIntent(pendingIntent)
    builder=builder.setContent(getRemoteView(title,msg))
    val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
val notificationChannel=NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
     notificationManager.createNotificationChannel(notificationChannel)
    }
    notificationManager.notify(0,builder.build())
}

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, msg: String): RemoteViews {
val remoteView=RemoteViews("com.example.save_yourself",R.layout.notification)
    remoteView.setTextViewText(R.id.notific_text,title)
    remoteView.setTextViewText(R.id.notific_msg,msg)
    remoteView.setImageViewResource(R.id.notific_image,R.drawable.ic_baseline_self_improvement_24)
    return remoteView
    }


}