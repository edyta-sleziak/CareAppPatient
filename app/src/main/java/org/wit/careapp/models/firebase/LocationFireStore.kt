package org.wit.careapp.models.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.AnkoLogger
import org.wit.careapp.models.LocationModel
import org.wit.careapp.models.LocationStore

class LocationFireStore() : LocationStore, AnkoLogger {

    val auth = FirebaseDatabase.getInstance()
    private var db = auth.reference


    override fun add(location: LocationModel) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.child("Users").child(userId).child("LatestActivity").child("LatestLocation").child("data").setValue(location)
        val key = db.child("Users").child(userId).child("LocationHistory").push().key
        key?.let {
            db.child("Users").child(userId).child("LocationHistory").child(key).setValue(location)
        }
    }
}
