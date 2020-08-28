package org.wit.careapp.models.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.AnkoLogger
import org.wit.careapp.models.HrModel
import org.wit.careapp.models.HrStore
import org.wit.careapp.models.LocationModel
import org.wit.careapp.models.LocationStore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HrFireStore() : HrStore, AnkoLogger {

    val auth = FirebaseDatabase.getInstance()
    private var db = auth.reference
    var hrRecords = ArrayList<HrModel>()
    var latestHr = ArrayList<HrModel>()
    val hrRecordsMut = MutableLiveData<ArrayList<HrModel>>()


    override fun add(hr: HrModel) {
        if (hr.dateTime == "") {
            hr.dateTime = getDate()
        }
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.child("Users").child(userId).child("LatestActivity").child("LatestHr").child("data").setValue(hr)
        val key = db.child("Users").child(userId).child("HrHistory").push().key
        key?.let {
            db.child("Users").child(userId).child("HrHistory").child(key).setValue(hr)
        }
    }

    fun getDate() :  String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }

    fun findRecordByDate(time: String): MutableLiveData<ArrayList<HrModel>> {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.child("Users").child(userId).child("HrHistory").orderByChild("dateTime").startAt(time).endAt(time).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.w("Database error" , "Retrieving data failed")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hrRecords.clear()
                dataSnapshot.children.mapNotNullTo(hrRecords) { it.getValue<HrModel>(
                    HrModel::class.java) }
                hrRecordsMut.postValue(ArrayList(hrRecords))
            }
        })
        return hrRecordsMut
    }
}