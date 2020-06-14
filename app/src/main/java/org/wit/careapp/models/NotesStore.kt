package org.wit.careapp.models

import androidx.lifecycle.MutableLiveData

interface NotesStore {
    fun add(notes: NotesModel)
    fun getAll(): MutableLiveData<ArrayList<NotesModel>>
    fun getActiveNotes(): MutableLiveData<ArrayList<NotesModel>>
    fun getRemovedNotes(): MutableLiveData<ArrayList<NotesModel>>
    fun edit(note: NotesModel)
    fun remove(noteId: String)
}