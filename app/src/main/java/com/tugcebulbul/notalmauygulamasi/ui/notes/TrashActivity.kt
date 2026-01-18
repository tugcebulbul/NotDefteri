package com.tugcebulbul.notalmauygulamasi.ui.notes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.tugcebulbul.notalmauygulamasi.R
import com.tugcebulbul.notalmauygulamasi.data.db.NoteDatabaseHelper
import com.tugcebulbul.notalmauygulamasi.databinding.ActivityTrashBinding
import com.tugcebulbul.notalmauygulamasi.util.SessionManager

class TrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrashBinding
    private lateinit var dbHelper: NoteDatabaseHelper
    private var kullaniciId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.toolbarTrash)
        supportActionBar?.title = getString(R.string.trash)

        dbHelper = NoteDatabaseHelper(this)
        kullaniciId = SessionManager.getUserId(this)

        if (kullaniciId == -1) {
            finish()
            return
        }

        binding.recyclerTrash.layoutManager = GridLayoutManager(this, 2)
        loadTrashNotes()
    }

    private fun loadTrashNotes() {
        val trashedNotes = dbHelper.copKutusuNotlariGetir(kullaniciId)

        binding.recyclerTrash.adapter = NotesAdapter(
            trashedNotes,


            tikla = { note ->
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.notu_geri_yukle))
                    .setMessage(getString(R.string.notu_geri_yukle_mesaj))
                    .setPositiveButton(getString(R.string.geri_yukle)) { _, _ ->
                        dbHelper.notGeriAl(note.id)
                        Toast.makeText(
                            this,
                            getString(R.string.not_geri_yuklendi),
                            Toast.LENGTH_SHORT
                        ).show()
                        loadTrashNotes()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            },


            uzunTikla = { note ->
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.kalici_sil))
                    .setMessage(getString(R.string.kalici_sil_mesaj))
                    .setPositiveButton(getString(R.string.sil)) { _, _ ->
                        dbHelper.notKaliciSil(note.id)
                        loadTrashNotes()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            }
        )
    }
}
