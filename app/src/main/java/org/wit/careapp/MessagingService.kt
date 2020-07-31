package org.wit.careapp

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.intentFor
import org.wit.careapp.models.NotificationsModel
import org.wit.careapp.views.notification.NotificationView
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String?) {
        Log.d("Tag",p0)
        super.onNewToken(p0)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null) {
            Log.d("FCM notification", remoteMessage.data.toString())
        }
    }

}