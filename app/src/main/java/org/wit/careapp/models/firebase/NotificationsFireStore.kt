package org.wit.careapp.models.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.wit.careapp.models.NotificationsModel
import org.wit.careapp.models.NotificationsStore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationsFireStore() : NotificationsStore {


    private var listOfItems = ArrayList<NotificationsModel>()
    private var mListOfItems = MutableLiveData<NotificationsModel>()
    private var db = FirebaseDatabase.getInstance().reference
    private var userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun getData(id: String): MutableLiveData<NotificationsModel> {
        db.child("Users").child(userId).child("Notifications").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.w("Database error" , "Retrieving data failed")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfItems.clear()
                dataSnapshot.children.mapNotNullTo(listOfItems) { it.getValue<NotificationsModel>(
                    NotificationsModel::class.java) }
                val selectedAlert = listOfItems.filter { n -> n.id == id }
                mListOfItems.postValue(selectedAlert[0])
            }
        })
        return mListOfItems
    }

    override fun markAsDone(id: String) {
        val date = getDate()
        db.child("Users").child(userId).child("Notifications").child(id).child("completedTime").setValue(date)
    }

    fun getDate() :  String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }

    override fun displayLater(id: String) {
        val date = LocalDateTime.now().plusMinutes(15).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        db.child("Users").child(userId).child("Notifications").child(id).child("displayTime").setValue(date)
    }

}