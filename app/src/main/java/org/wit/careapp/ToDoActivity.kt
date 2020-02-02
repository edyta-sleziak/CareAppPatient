package org.wit.careapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_to_do.*
import org.jetbrains.anko.startActivityForResult
import org.wit.careapp.models.TodoModel

class ToDoActivity : AppCompatActivity() {

    var todolist = ArrayList<TodoModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)
        title = "Your to-do items"
        todolist.add(TodoModel("item1"))
        todolist.add(TodoModel("item2"))
        todolist.add(TodoModel("item3"))
        todolist.add(TodoModel("item4"))
        todolist.add(TodoModel("item5"))
        todolist.add(TodoModel("item6"))

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

