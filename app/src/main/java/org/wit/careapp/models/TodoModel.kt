package org.wit.careapp.models

import java.time.LocalDateTime

data class TodoModel(
    var id: Long = 0,
    var task: String = "",
    var isCompleted:  Boolean = false,
    var dateCompleted: LocalDateTime,
    var userId: Long = 0
)