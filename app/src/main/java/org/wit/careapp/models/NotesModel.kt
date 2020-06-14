package org.wit.careapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotesModel(
    var id: String = "",
    var title: String = "",
    var note: String = "",
    var updatedDate: String = "",
    var updatedBy: String = "",
    var isActive: Boolean = true
) : Parcelable
