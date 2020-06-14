package org.wit.careapp.views.ToDo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.wit.careapp.R
import org.wit.careapp.models.TodoModel
import org.wit.careapp.models.firebase.TodoFireStore


class ToDoListAdapter(var toDoList: List<TodoModel>
) : RecyclerView.Adapter<ToDoListAdapter.MainHolder>() {

    private var mContext: Context? = null
    private var dbToDo = TodoFireStore()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        this.mContext=parent.context
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.listitem_todo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val mToDoListItem = toDoList!!.get(position)

        if(mToDoListItem.task != null) {
            holder.task.setText(mToDoListItem.task)
        }

        holder.buttonDone.setOnClickListener {
            dbToDo.markAsDone(mToDoListItem)
        }
    }

    override fun getItemCount(): Int = toDoList!!.size

    class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val task: TextView = itemView.findViewById(R.id.task_title)
        val buttonDone: Button = itemView.findViewById(R.id.task_done)

    }

}
