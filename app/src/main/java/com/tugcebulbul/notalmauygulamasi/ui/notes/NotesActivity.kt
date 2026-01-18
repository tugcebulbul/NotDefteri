package com.tugcebulbul.notalmauygulamasi.ui.notes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.tugcebulbul.notalmauygulamasi.R
import com.tugcebulbul.notalmauygulamasi.data.db.NoteDatabaseHelper
import com.tugcebulbul.notalmauygulamasi.databinding.ActivityNotesBinding
import com.tugcebulbul.notalmauygulamasi.util.SessionManager

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private lateinit var dbHelper: NoteDatabaseHelper
    private lateinit var adapter: NotesAdapter
    private var kullaniciId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = NoteDatabaseHelper(this)
        kullaniciId = SessionManager.getUserId(this)
        if (kullaniciId == -1) {
            finish()
            return
        }

        // Toolbar + Drawer
        setupDrawer()

        // RecyclerView
        binding.recyclerNotes.layoutManager = GridLayoutManager(this, 2)

        // Notları yükle
        loadActiveNotes()

        // FAB tıklama
        binding.fabAddNote.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }
    }

    private fun setupDrawer() {
        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_trash -> {
                    startActivity(Intent(this, TrashActivity::class.java))
                    true
                }
                else -> false
            }
        }


    }

    private fun loadActiveNotes() {
        val notes = dbHelper.aktifNotlariGetir(kullaniciId)

        adapter = NotesAdapter(
            notes,
            tikla = { note ->
                val intent = Intent(this, AddEditNoteActivity::class.java)
                intent.putExtra("noteId", note.id)
                startActivity(intent)
            },
            uzunTikla = { note ->
                showDeleteDialog(note.id)
            }
        )

        binding.recyclerNotes.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadActiveNotes()
    }

    private fun showDeleteDialog(noteId: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_note_title))
            .setMessage(getString(R.string.delete_note_message))
            .setPositiveButton(getString(R.string.move)) { _, _ ->
                dbHelper.notCopeAt(noteId)
                loadActiveNotes()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}
