package org.wit.careapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(
    var latitude:  Double,
    var longitude: Double,
    var zoom: Float,
    var dateAndTime: String = ""
) : Parcelable