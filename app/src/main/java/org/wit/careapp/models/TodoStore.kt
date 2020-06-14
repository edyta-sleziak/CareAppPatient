package org.wit.careapp.models

import androidx.lifecycle.MutableLiveData

interface TodoStore {
    fun getAll(): MutableLiveData<ArrayList<TodoModel>>
    fun getActiveOnly(): MutableLiveData<ArrayList<TodoModel>>
    fun markAsDone(item: TodoModel)
}