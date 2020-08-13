package org.wit.careapp.models.firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.wit.careapp.models.SosModel
import org.wit.careapp.models.SosStore

class SosFireStore() : SosStore {

    private var db = FirebaseDatabase.getInstance().reference
    private var userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun runSosAlert(sos: SosModel) {
        val key = db.child("Users").child(userId).child("SOS").push().key
        key?.let {
            sos.id = key
            sos.userId = userId
            db.child("Users").child(userId).child("SOS").child(key).setValue(sos)
        }
    }

}