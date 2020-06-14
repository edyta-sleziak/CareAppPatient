package org.wit.careapp.models

import java.time.LocalDateTime

data class TodoModel(
    var id: String = "",
    var task: String = "",
    var isCompleted:  Boolean = false,
    var dateCompleted: String = ""
)