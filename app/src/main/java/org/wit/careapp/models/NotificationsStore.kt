package org.wit.careapp.models

import androidx.lifecycle.MutableLiveData

interface NotificationsStore {
    fun getData(id: String): MutableLiveData<NotificationsModel>
    fun markAsDone(id: String)
    fun displayLater(id: String)
}