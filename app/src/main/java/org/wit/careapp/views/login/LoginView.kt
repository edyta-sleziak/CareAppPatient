package org.wit.careapp.views.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.Context
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.wit.careapp.R
import org.wit.careapp.models.HrModel
import org.wit.careapp.models.LocationModel
import org.wit.careapp.models.firebase.AccountInfoFireStore
import org.wit.careapp.models.firebase.HrFireStore
import org.wit.careapp.models.firebase.LocationFireStore
import org.wit.careapp.views.main.MainActivity

class LoginView : AppCompatActivity() {

    lateinit var context: Context
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: AccountInfoFireStore? = null
    private val locationFirestore = LocationFireStore()
    private val hrFireStore = HrFireStore()
    var accountFirebase = AccountInfoFireStore()

    override fun onCreate(savedInstanceState: Bundle?) {

        //todo add data gathering
        locationFirestore.add(LocationModel(0.0,0.0,0f))
        hrFireStore.add(HrModel(85))

        if (accountFirebase.getUser().isNotEmpty()) {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }
        context = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar.visibility = View.GONE

        login.setOnClickListener() {
            val email = email.text.toString()
            val password = password.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                toast(R.string.enter_credentials)
            } else {
                doLogin(email, password)
            }
        }
    }
    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun doLogin(email: String, password: String) {
        showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchData {
                        hideProgress()
                        startActivity(Intent(baseContext, MainActivity::class.java))
                    }
                } else {
                    hideProgress()
                    startActivity(Intent(baseContext, MainActivity::class.java))
                }
            } else {
                hideProgress()
                toast("Login Failed: ${task.exception?.message}")
            }
        }
    }

    private fun doSignup (email: String, password: String) {
        showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                fireStore!!.fetchData {
                hideProgress()
                startActivity(Intent(baseContext, MainActivity::class.java))
                //}
            } else {
                toast("Sign Up Failed: ${task.exception?.message}")
                hideProgress()
            }
        }
    }

}
