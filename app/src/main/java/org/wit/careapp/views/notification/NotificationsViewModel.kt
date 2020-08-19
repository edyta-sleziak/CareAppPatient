package org.wit.careapp.views.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.wit.careapp.models.NotesModel
import org.wit.careapp.models.NotificationsModel
import org.wit.careapp.models.firebase.NotesFireStore
import org.wit.careapp.models.firebase.NotificationsFireStore

class NotificationsViewModel : ViewModel() {

    private val notificationsFireStore = NotificationsFireStore()
    private var alertId = ""
    val mData: LiveData<NotificationsModel> get() = notificationsFireStore.getData(alertId)


    fun getData(id: String): LiveData<NotificationsModel> {
        alertId = id
        return mData
    }

    fun reschedule(id: String) {
        notificationsFireStore.displayLater(id)
    }

    fun markAsDone(id: String) {
        notificationsFireStore.markAsDone(id)
    }
}