package org.wit.careapp.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.content.Intent
import android.net.Uri
import org.jetbrains.anko.intentFor
import org.wit.careapp.R
import org.wit.careapp.views.Notes.NotesView
import org.wit.careapp.views.ToDo.ToDoView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SOS.setOnClickListener {
            toast("You clicked SOS option")
        }

        callEmergencyNumber.setOnClickListener {
            toast("You clicked CALL option")
            val call = Intent(Intent.ACTION_DIAL)
            call.data = Uri.parse("tel:0123456789")
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
