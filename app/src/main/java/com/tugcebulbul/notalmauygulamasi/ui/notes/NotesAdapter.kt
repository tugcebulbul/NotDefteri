package com.tugcebulbul.notalmauygulamasi.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tugcebulbul.notalmauygulamasi.R
import com.tugcebulbul.notalmauygulamasi.data.model.Note
import com.tugcebulbul.notalmauygulamasi.databinding.ItemNoteGridBinding
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(
    private var liste: List<Note>,
    private val tikla: (Note) -> Unit,
    private val uzunTikla: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotViewHolder>() {

    inner class NotViewHolder(val binding: ItemNoteGridBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bagla(not: Note) {
            binding.tvTitle.text = not.baslik
            binding.tvContent.text = not.icerik

            val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            binding.tvDate.text = itemView.context.getString(
                R.string.updated_with_date,
                format.format(Date(not.guncellenmeTarihi))
            )

            binding.root.setOnClickListener { tikla(not) }
            binding.root.setOnLongClickListener {
                uzunTikla(not)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotViewHolder {
        val binding = ItemNoteGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotViewHolder, position: Int) {
        holder.bagla(liste[position])
    }

    override fun getItemCount(): Int = liste.size

    fun guncelle(yeniListe: List<Note>) {
        liste = yeniListe
        notifyDataSetChanged()
    }
}
