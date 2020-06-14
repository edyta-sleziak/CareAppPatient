package org.wit.careapp.views.Notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.wit.careapp.models.NotesModel
import org.wit.careapp.models.firebase.NotesFireStore

class NotesViewModel : ViewModel() {

    val notesFireStore = NotesFireStore()
    val mItemsList: LiveData<ArrayList<NotesModel>> get() = notesFireStore.getActiveNotes()

    fun getNotesList(): LiveData<ArrayList<NotesModel>> {
        return mItemsList
    }
}