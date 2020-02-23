package org.wit.careapp.views.Notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_notes.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.careapp.R
import org.wit.careapp.views.main.MainActivity

class NotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        title = "Your notes"

        floatingActionButton.setOnClickListener() {
            toast("You clicked button to add new note")
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
