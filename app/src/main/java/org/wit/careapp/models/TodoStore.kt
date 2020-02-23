package org.wit.careapp.models

interface TodoStore {
    fun getAll(userId: Long): List<TodoModel>
    fun getActiveOnly(userId: Long): List<TodoModel>
    fun markAsDone(todoItemId: Long)
}