package org.wit.careapp.models
import java.time.LocalDateTime

data class LocationModel(
    var id: Long = 0,
    var longitude: Double = 0.0,
    var latitude:  Double = 0.0,
    var dateTime: LocalDateTime = LocalDateTime.now(),
    var userId: Long = 0
)