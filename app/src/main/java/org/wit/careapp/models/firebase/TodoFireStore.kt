package org.wit.careapp.models.firebase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.wit.careapp.models.TodoModel
import org.wit.careapp.models.TodoStore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TodoFireStore() : TodoStore {

    private var listOfItems = ArrayList<TodoModel>()
    private var mListOfItems = MutableLiveData<ArrayList<TodoModel>>()
    private var db = FirebaseDatabase.getInstance().reference
    private var userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun getAll(): MutableLiveData<ArrayList<TodoModel>> {
        fetchData()
        return mListOfItems
    }

    override fun getActiveOnly(): MutableLiveData<ArrayList<TodoModel>> {
        fetchData()
        val activeItems = listOfItems.filter { n -> !n.isCompleted  }
        mListOfItems.value = ArrayList(activeItems)
        return mListOfItems
    }

    fun getDate() :  String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }

    override fun markAsDone(item: TodoModel) {
        val now = getDate()
        db.child("Users").child(userId).child("ToDoItems").child(item.id).child("completed").setValue(true)
        db.child("Users").child(userId).child("ToDoItems").child(item.id).child("dateCompleted").setValue(now)
    }

    fun fetchData() {
        db.child("Users").child(userId).child("ToDoItems").orderByChild("updatedDate").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.w("Database error" , "Retrieving data failed")
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                clear()
                Log.d("snapshot ", "$dataSnapshot")
                dataSnapshot.children.mapNotNullTo(listOfItems) { it.getValue<TodoModel>(
                    TodoModel::class.java) }
                Log.d("list ", "$listOfItems")
            }
        })
    }

    fun clear() {
        listOfItems.clear()
    }

}