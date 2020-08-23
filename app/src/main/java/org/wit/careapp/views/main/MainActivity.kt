package org.wit.careapp.views.main

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.intentFor
import org.wit.careapp.R
import org.wit.careapp.models.LocationModel
import org.wit.careapp.models.SosModel
import org.wit.careapp.models.firebase.AccountInfoFireStore
import org.wit.careapp.models.firebase.LocationFireStore
import org.wit.careapp.models.firebase.SosFireStore
import org.wit.careapp.views.Notes.NotesView
import org.wit.careapp.views.ToDo.ToDoView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private val sosFirestore = SosFireStore()
    private val accountFirestore = AccountInfoFireStore()
    private val locationFirestore = LocationFireStore()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocation()

        SOS.setOnClickListener {
            toast("SOS alert sent! Your carer is already notified")
            val current = LocalDateTime.now()
            val alertDate = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val alertTime = current.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            val newSosAlert = SosModel(alertDate = alertDate, alertTime = alertTime)

            sosFirestore.runSosAlert(newSosAlert)
        }

        callEmergencyNumber.setOnClickListener {
            toast("You are calling to your emergency contact")
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

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Token", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result?.token
                accountFirestore.addToken(token.toString())
                Log.d("Tag token", token)
            })
    }

    private fun checkLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                locationFirestore.add(LocationModel(location!!.latitude, location.longitude, 17f))
            }
    }

}
