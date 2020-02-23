package org.wit.careapp.models

import java.time.LocalDateTime

data class StepsModel(
    var id: Long = 0,
    var stepsCount: Int = 0,
    var dateTime: LocalDateTime = LocalDateTime.now(),
    var userId: Long = 0
)