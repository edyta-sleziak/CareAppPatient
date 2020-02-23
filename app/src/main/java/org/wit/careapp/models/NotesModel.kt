package org.wit.careapp.models
import java.time.LocalDateTime

data class NotesModel(
    var id: Long = 0,
    var note: String = "",
    var createdDate: LocalDateTime = LocalDateTime.now(),
    var createdBy: String = "",
    var updatedDate: LocalDateTime = LocalDateTime.now(),
    var updatedBy: String = "",
    var isActive: Boolean = true,
    var userId: Long = 0
)

