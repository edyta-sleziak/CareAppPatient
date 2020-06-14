package org.wit.careapp.views.ToDo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.wit.careapp.models.TodoModel
import org.wit.careapp.models.firebase.TodoFireStore

class ToDoListViewModel : ViewModel() {

    val toDoFireStore = TodoFireStore()
    val mItemsList: LiveData<ArrayList<TodoModel>> get() = toDoFireStore.getActiveOnly()

    fun getToDoList(): LiveData<ArrayList<TodoModel>> {
        return mItemsList
    }
}