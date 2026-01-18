package com.tugcebulbul.notalmauygulamasi.ui.notes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tugcebulbul.notalmauygulamasi.R
import com.tugcebulbul.notalmauygulamasi.data.db.NoteDatabaseHelper
import com.tugcebulbul.notalmauygulamasi.data.model.Note
import com.tugcebulbul.notalmauygulamasi.databinding.ActivityAddEditNoteBinding
import com.tugcebulbul.notalmauygulamasi.util.SessionManager

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditNoteBinding
    private lateinit var dbHelper: NoteDatabaseHelper

    private var notId = 0
    private var kullaniciId = -1
    private var olusturulmaTarihi: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = NoteDatabaseHelper(this)
        kullaniciId = SessionManager.getUserId(this)

        if (kullaniciId == -1) {
            finish()
            return
        }

        notId = intent.getIntExtra("noteId", 0)

        // ✏️ Düzenleme modu
        if (notId != 0) {
            val not = dbHelper.aktifNotlariGetir(kullaniciId).find { it.id == notId }
            not?.let {
                binding.etTitle.setText(it.baslik)
                binding.etContent.setText(it.icerik)
                olusturulmaTarihi = it.olusturulmaTarihi
            }
        }

        binding.btnSaveNote.setOnClickListener {
            kaydet()
        }
    }

    private fun kaydet() {
        val baslik = binding.etTitle.text.toString().trim()
        val icerik = binding.etContent.text.toString().trim()
        val simdi = System.currentTimeMillis()

        if (baslik.isEmpty()) {
            Toast.makeText(this, getString(R.string.title_empty), Toast.LENGTH_SHORT).show()
            return
        }

        if (notId == 0) {
            //  Yeni not
            val yeniNot = Note(
                kullaniciId = kullaniciId,
                baslik = baslik,
                icerik = icerik,
                olusturulmaTarihi = simdi,
                guncellenmeTarihi = simdi
            )
            dbHelper.notEkle(yeniNot)
        } else {
            // ️ Güncelleme
            val guncellenenNot = Note(
                id = notId,
                kullaniciId = kullaniciId,
                baslik = baslik,
                icerik = icerik,
                olusturulmaTarihi = olusturulmaTarihi,
                guncellenmeTarihi = simdi
            )
            dbHelper.notGuncelle(guncellenenNot)
        }

        Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show()
        finish()
    }
}
