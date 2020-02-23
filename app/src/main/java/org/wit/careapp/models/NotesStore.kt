package org.wit.careapp.models

interface NotesStore {
    fun add(notes: NotesModel)
    fun getAll(userId: Long): List<NotesModel>
    fun getOne(noteId: Long): NotesModel?
    fun edit(noteId: Long)
    fun remove(noteId: Long)
}