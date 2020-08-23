package org.wit.careapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(
    var latitude:  Double = 0.0,
    var longitude: Double = 0.0,
    var zoom: Float = 0f,
    var dateAndTime: String = ""
) : Parcelable