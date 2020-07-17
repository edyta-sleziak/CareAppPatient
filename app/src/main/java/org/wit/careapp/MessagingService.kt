package org.wit.careapp

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.intentFor
import org.wit.careapp.views.notification.NotificationView

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(p0: String?) {
        //super.onNewToken(p0)
        Log.d("Tag",p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val messageReceived: String = p0.data["message"]!!
        Log.d("Message", messageReceived)

        passMessageToActivity(messageReceived)
    }

    private fun passMessageToActivity(message: String) {
        startActivity(Intent(baseContext, NotificationView::class.java))

    }
}