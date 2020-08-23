package org.wit.careapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_notification.*
import org.wit.careapp.models.firebase.AccountInfoFireStore
import org.wit.careapp.views.notification.NotificationsViewModel
import java.text.SimpleDateFormat
import java.util.*


class MessagingService : FirebaseMessagingService() {

    val accountFirestore = AccountInfoFireStore()


    override fun onNewToken(token: String?) {
        Log.d("Tag",token)
        if(token != null) {
            accountFirestore.addToken(token)
        }
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM","Message data payload: ${remoteMessage.data}")
        val notificationId = remoteMessage.data["id"]
        val scheduledTime = remoteMessage.data["scheduled"]
        val title = "CareApp"
        val message = remoteMessage.data["message"]
        scheduleAlarm(scheduledTime, title, message, notificationId)
    }

    private fun scheduleAlarm(
        scheduledTimeString: String?,
        title: String,
        message: String?,
        id: String?
    ) {
        val alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            Intent(applicationContext, NotificationBroadcastReceiver::class.java).let { intent ->
                intent.putExtra("title", title)
                intent.putExtra("message", message)
                intent.putExtra("id", id)
                PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
            }

        val scheduledTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .parse(scheduledTimeString!!)
        Log.d("Notification time",scheduledTime.toString())

        scheduledTime?.let {
            alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                it.time,
                alarmIntent
            )
        }
    }


}