package org.wit.careapp.views.ToDo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.wit.careapp.models.TodoModel
import org.wit.careapp.models.firebase.TodoFireStore

class ToDoListViewModel : ViewModel() {
    val toDoFireStore = TodoFireStore()
    var mItemsList: MutableLiveData<ArrayList<TodoModel>> = MutableLiveData()

    fun getToDoList(): LiveData<ArrayList<TodoModel>> {
        return toDoFireStore.getActiveOnly()
    }
}
