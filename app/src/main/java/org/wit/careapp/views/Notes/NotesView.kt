package org.wit.careapp.views.Notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_notes.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivityForResult
import org.wit.careapp.R
import org.wit.careapp.views.Notes.addNote.AddNoteActivity
import org.wit.careapp.views.main.MainActivity

class NotesView : AppCompatActivity(), AnkoLogger {

    private lateinit var mRecycleView: RecyclerView
    private lateinit var mRecyclerViewAdapter: NotesAdapter
    private lateinit var viewModel: NotesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        title = "CareApp - Your notes"

        viewModel =
            ViewModelProviders.of(this).get(NotesViewModel::class.java)

        mRecycleView = findViewById(R.id.notes_recyclerView)
        mRecycleView.layoutManager = LinearLayoutManager(
            this.applicationContext, RecyclerView.VERTICAL,false)


        viewModel.getNotesList()
            .observe(this, Observer{ noteslist ->
                mRecycleView.adapter =
                    NotesAdapter(
                        noteslist
                    )
                mRecyclerViewAdapter = NotesAdapter(noteslist)
                })

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("note", "new note")
            startActivity(intent)
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
