package org.wit.careapp.views.main


import android.app.AlarmManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataSourcesRequest
import org.wit.careapp.models.HrModel
import org.wit.careapp.models.firebase.HrFireStore
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private val REQUEST_OAUTH = 1001
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var AUTH_PENDING = "auth_state_pending"
    private var authInProgress = false
    private val sosFirestore = SosFireStore()
    private val accountFirestore = AccountInfoFireStore()
    private val locationFirestore = LocationFireStore()
    private val hrFireStore = HrFireStore()
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING)
        }

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
            .addApi(Fitness.HISTORY_API)
            .useDefaultAccount()
            .addScope(Fitness.SCOPE_ACTIVITY_READ)
            .addScope(Fitness.SCOPE_BODY_READ)
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

    //region Google Fit API connection

    override fun onConnected(bundle: Bundle?) {
        Log.d("Fit", "connected")
        DataSourcesRequest.Builder()
            .setDataTypes(DataType.TYPE_HEART_RATE_BPM)
            .setDataSourceTypes(DataSource.TYPE_RAW)
            .build()

        var endDate = System.currentTimeMillis()
        var startDate = endDate - 36000000

        val pendingResult = Fitness.HistoryApi.readData(
            mGoogleApiClient,
            DataReadRequest.Builder()
            .read(DataType.TYPE_HEART_RATE_BPM)
            .setTimeRange(startDate,endDate, TimeUnit.MILLISECONDS)
            .build())

        pendingResult.setResultCallback {
            val dataSet = it.getDataSet(DataType.TYPE_HEART_RATE_BPM)
            var size = dataSet.dataPoints.size
            if ( size > 0) {
                val data = dataSet.dataPoints[size-1].getValue(Field.FIELD_BPM).asFloat()
                val recordDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(dataSet.dataPoints[size-1].getEndTime(TimeUnit.MILLISECONDS)), TimeZone.getDefault().toZoneId())
                val time = recordDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                val existingRecords = hrFireStore.findRecordByDate(time)
                if (existingRecords.value?.size == null) {
                    hrFireStore.add(HrModel(data.toInt(), time.toString()))
                    Log.d("HRRecord","Record with date $time added")
                } else {
                    Log.d("HRRecord","Record with date $time already exists. Record not added")
                }
            }
        }
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

    override fun onActivityResult(requestCode : Int, resultCode : Int, data: Intent? ) {
        super.onActivityResult(requestCode,resultCode,data)
        if( requestCode == REQUEST_OAUTH ) {
            authInProgress = false
            if( resultCode == RESULT_OK ) {
                if( !mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected() ) {
                    mGoogleApiClient.connect()
                }
            } else if( resultCode == RESULT_CANCELED ) {
                Log.e( "GoogleFit", "RESULT_CANCELED" )
            }
        }
    }

    //endregion

}
