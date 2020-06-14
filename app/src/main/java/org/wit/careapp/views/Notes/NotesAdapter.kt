package org.wit.careapp.views.Notes

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.intentFor
import org.wit.careapp.models.NotesModel
import org.wit.careapp.R
import org.wit.careapp.models.firebase.NotesFireStore
import org.wit.careapp.views.Notes.addNote.AddNoteActivity


class NotesAdapter(var notesList: List<NotesModel>
) : RecyclerView.Adapter<NotesAdapter.MainHolder>() {

    private var mContext: Context? = null
    private var dbNotesList = NotesFireStore()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        this.mContext=parent.context
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.listitem_notes,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val mNotesListItem = notesList!!.get(position)

        if(mNotesListItem.title != null) {
            holder.noteTitle.setText(mNotesListItem.title)
        }

        if(mNotesListItem.updatedDate != null) {
            holder.updateDate.setText(mNotesListItem.updatedDate)
        }

        if(mNotesListItem.updatedBy != null) {
            holder.updatedBy.setText(mNotesListItem.updatedBy)
        }

        holder.itemView.setOnClickListener{
            Log.d("CLICKED:", " clicked on item ${mNotesListItem?.id}")
            val intent = Intent(holder.itemView.context, AddNoteActivity::class.java)
            intent.putExtra("note", mNotesListItem)
            holder.itemView.context.startActivity(intent)
         }
    }

    override fun getItemCount(): Int = notesList!!.size

    class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.note_title)
        val updateDate: TextView = itemView.findViewById(R.id.note_update_date)
        val updatedBy: TextView = itemView.findViewById(R.id.note_updated_by)

    }

}
