package org.wit.careapp.views.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_notification.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.wit.careapp.R
import org.wit.careapp.models.firebase.AccountInfoFireStore
import org.wit.careapp.views.login.LoginView
import org.wit.careapp.views.main.MainActivity

class NotificationView : AppCompatActivity(), AnkoLogger {

    var accountFirebase = AccountInfoFireStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val alertId = intent.getStringExtra("id")

        val viewModel = NotificationsViewModel()
        viewModel.getData(alertId!!)
            .observe(this, Observer { data ->
                notification_title.setText(data.notification)
            })

        button_done.setOnClickListener {
            viewModel.markAsDone(alertId)
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        button_reschedule.setOnClickListener {
            viewModel.reschedule(alertId)
            startActivity(Intent(baseContext, MainActivity::class.java))
        }
    }


    override fun onBackPressed() {
        if (accountFirebase.getUser() != null) {
            startActivity(Intent(baseContext, MainActivity::class.java))
        } else {
            startActivityForResult(intentFor<LoginView>(), 0)
        }

    }
}