package org.wit.careapp.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.content.Intent
import android.net.Uri
import android.util.Log
import org.jetbrains.anko.intentFor
import org.wit.careapp.R
import org.wit.careapp.models.SosModel
import org.wit.careapp.models.firebase.AccountInfoFireStore
import org.wit.careapp.models.firebase.SosFireStore
import org.wit.careapp.views.Notes.NotesView
import org.wit.careapp.views.ToDo.ToDoView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    val sosFirestore = SosFireStore()
    val accountFirestore = AccountInfoFireStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SOS.setOnClickListener {
            toast("You clicked SOS option")
            val current = LocalDateTime.now()
            val alertDate = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val alertTime = current.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val newSosAlert = SosModel(alertDate = alertDate, alertTime = alertTime)
            sosFirestore.runSosAlert(newSosAlert)
        }

        callEmergencyNumber.setOnClickListener {
            toast("You clicked CALL option")
            val number = accountFirestore.getContactNumber()
            Log.d("number", number)
            val call = Intent(Intent.ACTION_DIAL)
            call.data = Uri.parse("tel:" + number)
            startActivity(call)
        }

        Notes.setOnClickListener {
            startActivityForResult(intentFor<NotesView>(), 0)
        }

        ToDo.setOnClickListener {
            startActivityForResult(intentFor<ToDoView>(), 0)
        }
    }


}
