package org.wit.careapp.models

interface NotificationsStore {
    fun displayNote(noteId: Long)
    fun markAsDone(noteId: Long)
    fun displayLater(noteId: Long)
}