package org.wit.careapp.models.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.careapp.models.AccountInfoModel
import org.wit.careapp.models.AccountInfoStore

class AccountInfoFireStore(val context: Context) : AccountInfoStore, AnkoLogger {

    val accounts = ArrayList<AccountInfoModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun add(accountInfo: AccountInfoModel) {

    }

    override fun getAccountInfo(id: Long): AccountInfoModel? {
        val userInfo: AccountInfoModel? = accounts.find {a -> a.id == id }
        return userInfo
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
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        accounts.clear()
        db.child("users").child(userId).addListenerForSingleValueEvent(valueEventListener)
    }

}