package org.wit.careapp.models

data class LocationModel(
    var longitude: Double = 0.0,
    var latitude:  Double = 0.0,
    var zoom: Float = 0f,
    var dateAndTime: String = ""
)