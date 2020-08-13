package org.wit.careapp.models.firebase


import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.wit.careapp.models.AccountInfoModel
import org.wit.careapp.models.AccountInfoStore
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase

class AccountInfoFireStore() : AccountInfoStore, AnkoLogger {
    val accounts = ArrayList<AccountInfoModel>()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    val db = FirebaseDatabase.getInstance().reference
    private var phoneNumber = ""


    init {
        db.child("Users")
            .child(userId)
            .child("Settings")
            .child("sosContactNumber")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.value.toString()
                    phoneNumber = data
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
        })
    }

    fun addToken(token: String) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.child("Users").child(userId).child("Settings").child("AccountInfo").child("registrationTokenPatient").setValue(token)
    }

    fun getUser(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun getContactNumber(): String {
        return phoneNumber
    }

    fun fetchData(dataReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(accounts) { it.getValue<AccountInfoModel>(AccountInfoModel::class.java) }
                dataReady()
            }
        }

        accounts.clear()
        db.child("Users").child(userId).addListenerForSingleValueEvent(valueEventListener)
    }

}