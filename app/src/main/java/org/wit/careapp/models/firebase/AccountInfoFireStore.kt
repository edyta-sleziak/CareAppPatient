package org.wit.careapp.models.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.careapp.models.AccountInfoModel
import org.wit.careapp.models.AccountInfoStore
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.ExecutionException
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
//                    tcs.setResult(data)
                    phoneNumber = data
                }

                override fun onCancelled(databaseError: DatabaseError) {
//                    tcs.setException(databaseError.toException())
                }
        })
    }

    override fun getContactNumber(): String {
        return phoneNumber
    }

//    override fun getContactNumber(): String {
////        val tcs = TaskCompletionSource<String>()
//        var number = ""
//
//        db.child("Users")
//            .child(userId)
//            .child("Settings")
//            .child("sosContactNumber")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val data = snapshot.value.toString()
////                    tcs.setResult(data)
//                    phoneNumber = data
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
////                    tcs.setException(databaseError.toException())
//                }
//        })
//
////        var t = tcs.task
////
////        try {
////            Tasks.await(t)
////        } catch (e: ExecutionException) {
////            t = Tasks.forException(e)
////        } catch (e: InterruptedException) {
////            t = Tasks.forException(e)
////        }
////
////        if (t.isSuccessful) {
////            val result = t.result
////        }
//
//        return number
//    }

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