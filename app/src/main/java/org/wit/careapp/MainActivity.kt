package org.wit.careapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SOS.setOnClickListener {
            toast("You clicked SOS option")
        }

        callEmergencyNumber.setOnClickListener {
            toast("You clicked CALL option")
        }

        Notes.setOnClickListener {
            toast("You clicked NOTES option")
        }

        ToDo.setOnClickListener {
            toast("You clicked TODO LIST option")
        }
    }


}
