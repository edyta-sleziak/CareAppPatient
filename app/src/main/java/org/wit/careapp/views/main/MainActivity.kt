package org.wit.careapp.views.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.util.Log
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
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.ConnectionCallbacks
import com.google.android.gms.common.api.internal.OnConnectionFailedListener
import android.renderscript.Element.DataType
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.common.ConnectionResult
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.fitness.FitnessStatusCodes
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource


class MainActivity : AppCompatActivity(), OnDataPointListener,
GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {

    private val REQUEST_OAUTH = 1001
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var AUTH_PENDING = "auth_state_pending"
    private var authInProgress = false
    private val sosFirestore = SosFireStore()
    private val accountFirestore = AccountInfoFireStore()
    private val locationFirestore = LocationFireStore()
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)  // Required for SensorsApi calls
                    // Optional: specify more APIs used with additional calls to addApi
                    .useDefaultAccount()
                    .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()

        mGoogleApiClient.connect()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 200f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                if (locationResult.lastLocation.latitude != 0.0 && locationResult.lastLocation.longitude != 0.0) {
                    val location = locationResult.lastLocation
                    locationFirestore.add(LocationModel(location!!.latitude, location.longitude, 17f))
                }
            }
        }
        startLocationUpdates()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Fitness.SENSORS_API)  // Required for SensorsApi calls
            // Optional: specify more APIs used with additional calls to addApi
            .useDefaultAccount()
            .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        mGoogleApiClient.connect()

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

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onConnected(connectionHint: Bundle?) {
        Fitness.SensorsApi.add(
            mGoogleApiClient,
            SensorRequest.Builder()
                .setDataType(com.google.android.gms.fitness.data.DataType.TYPE_HEART_RATE_BPM)
                .build(),
            this
        )
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_OAUTH)
            }
            catch ( e: IntentSender.SendIntentException) {
            }
        } else {
            Log.e( "GoogleFit", "authInProgress : $connectionResult" )
        }
    }

    override fun onDataPoint(dataPoint: DataPoint) {
        var fields = dataPoint.dataType.fields
        Log.d("Google Fit fields", fields.toString())
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, data: Intent? ) {
        super.onActivityResult(requestCode,resultCode,data)
        if( requestCode == REQUEST_OAUTH ) {
        authInProgress = false
        if( resultCode == RESULT_OK ) {
            if( !mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected() ) {
                mGoogleApiClient.connect()
            }
        } else if( resultCode == RESULT_CANCELED ) {
            Log.e( "GoogleFit", "RESULT_CANCELED" );
        }
    } else {
        Log.e("GoogleFit", "requestCode NOT request_oauth");
    }
}

}
