package org.wit.careapp.views.ToDo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_notes.*
import org.jetbrains.anko.startActivityForResult
import org.wit.careapp.R
import org.wit.careapp.views.main.MainActivity

class ToDoView : AppCompatActivity() {
    private lateinit var mRecycleView: RecyclerView
    private lateinit var mRecyclerViewAdapter: ToDoListAdapter
    private lateinit var viewModel: ToDoListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)

        viewModel =
            ViewModelProviders.of(this).get(ToDoListViewModel::class.java)

        mRecycleView = findViewById(R.id.recyclerView_todo)
        mRecycleView.layoutManager = LinearLayoutManager(
            this.applicationContext, RecyclerView.VERTICAL, false
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.getToDoList()
            .observe(this, Observer{ noteslist ->
                mRecycleView.adapter =
                    ToDoListAdapter(
                        noteslist
                    )
                mRecyclerViewAdapter = ToDoListAdapter(noteslist)
            })
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

