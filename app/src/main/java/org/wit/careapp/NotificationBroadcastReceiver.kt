package org.wit.careapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            Log.d("BroadcastReceiver In",intent.toString())
            val title = it.getStringExtra("title")
            val message = it.getStringExtra("message")
            val id = it.getStringExtra("id")

            val notificationData = Data.Builder()
                .putString("title", title)
                .putString("message", message)
                .putString("id", id)
                .build()

            val work = OneTimeWorkRequest.Builder(ScheduledWorker::class.java)
                .setInputData(notificationData)
                .build()
            Log.d("BroadcastReceiver Out",notificationData.toString())
            WorkManager.getInstance().beginWith(work).enqueue()
        }
    }
}