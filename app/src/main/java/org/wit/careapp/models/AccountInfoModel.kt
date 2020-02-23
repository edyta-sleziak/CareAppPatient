package org.wit.careapp.models

data class AccountInfoModel(
    var id: Long = 0,
    var email: String = "",
    var password: String = "",
    var carerName: String = "",
    var epName: String = "",
    var sosContactNumber: String = "",
    var saveHrRangeHigh: Int = 0,
    var saveHrRangeLow: Int = 0,
    var saveHomeDistance: Int = 0,
    var dailyStepsGoal: Int = 0
)