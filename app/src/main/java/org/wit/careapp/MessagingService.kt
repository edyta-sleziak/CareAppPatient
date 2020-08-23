package org.wit.careapp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.wit.careapp.models.firebase.AccountInfoFireStore


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
        if(remoteMessage.notification != null) {
            Log.d("FCM notification", remoteMessage.data.toString())
        }
    }

}