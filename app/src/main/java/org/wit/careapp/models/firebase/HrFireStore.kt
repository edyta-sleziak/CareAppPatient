package org.wit.careapp.models.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.AnkoLogger
import org.wit.careapp.models.HrModel
import org.wit.careapp.models.HrStore
import org.wit.careapp.models.LocationModel
import org.wit.careapp.models.LocationStore

class HrFireStore() : HrStore, AnkoLogger {

    val auth = FirebaseDatabase.getInstance()
    private var db = auth.reference


    override fun add(hr: HrModel) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.child("Users").child(userId).child("LatestActivity").child("LatestHr").child("data").setValue(hr)
        val key = db.child("Users").child(userId).child("HrHistory").push().key
        key?.let {
            db.child("Users").child(userId).child("HrHistory").child(key).setValue(hr)
        }
    }
}