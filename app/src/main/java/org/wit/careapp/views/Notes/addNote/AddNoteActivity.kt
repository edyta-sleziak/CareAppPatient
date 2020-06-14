package org.wit.careapp.views.Notes.addNote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_notes.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.careapp.R
import org.wit.careapp.models.NotesModel
import org.wit.careapp.views.main.MainActivity
import org.wit.careapp.models.firebase.NotesFireStore
import org.wit.careapp.views.Notes.NotesView

class AddNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        title = "Your note"
        val notesList = NotesFireStore()
        val passedData = getIntent().getExtras()?.getParcelable<NotesModel>("note")

        if(passedData != null) {
            note_add_title.setText(passedData.title)
            note_content.setText(passedData.note)
        }


        button_saveNote.setOnClickListener() {
            val title = note_add_title.text.toString()
            val content = note_content.text.toString()
            val updateTime = notesList.getDate()
            if (title == "" || content == "") {
                toast("Please fill all fields!")
            } else {
                if (passedData == null) {
                    val newNote =
                        NotesModel("", title, content, updatedBy = "Patient", updatedDate = updateTime)
                    notesList.add(newNote)
                    toast("Note saved")
                } else {
                    val editedNote =
                        NotesModel(passedData!!.id, title, content, updatedBy = "Patient", updatedDate = updateTime)
                    notesList.edit(editedNote)
                    toast("Note edited")
                }
                startActivityForResult<NotesView>(0)
            }

        }

        button_deleteNote.setOnClickListener() {
            notesList.remove(passedData!!.id)
            toast("Note deleted")
            startActivityForResult<MainActivity>(0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> startActivityForResult<MainActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
