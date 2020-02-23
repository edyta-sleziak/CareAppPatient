package org.wit.careapp.models

import java.time.LocalDateTime

data class NotificationsModel(
    var id: Long = 0,
    var notification: String = "",
    var displayTime: LocalDateTime,
    var completedTime: LocalDateTime
)