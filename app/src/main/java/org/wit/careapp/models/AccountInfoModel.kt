package org.wit.careapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountInfoModel(
    var email: String = "",
    var carerName: String = "",
    var epName: String = "",
    var sosContactNumber: String = "",
    var saveHrRangeHigh: String = "",
    var saveHrRangeLow: String = "",
    var saveHomeDistance: String = "",
    var dailyStepsGoal: String = "",
    var notificationResponseTime: String = "",
    var registrationTokenCarer: String = "",
    var registrationTokenPatient: String = "",
    var location: LocationModel = LocationModel()): Parcelable
