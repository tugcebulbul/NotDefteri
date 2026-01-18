package com.tugcebulbul.notalmauygulamasi.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tugcebulbul.notalmauygulamasi.data.model.Note

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context.applicationContext, "app.db", null, 5) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL("""
            CREATE TABLE kullanicilar (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE,
                sifre TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE notlar (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                kullaniciId INTEGER,
                baslik TEXT,
                icerik TEXT,
                olusturulmaTarihi INTEGER,
                guncellenmeTarihi INTEGER,
                silindi INTEGER DEFAULT 0
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS kullanicilar")
        db.execSQL("DROP TABLE IF EXISTS notlar")
        onCreate(db)
    }

    // ================== USER ==================

    fun insertUser(email: String, sifre: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("sifre", sifre)
        }
        val result = db.insert("kullanicilar", null, values)
        db.close()
        return result != -1L
    }

    fun isUserExists(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM kullanicilar WHERE email=?",
            arrayOf(email)
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    fun login(email: String, sifre: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM kullanicilar WHERE email=? AND sifre=?",
            arrayOf(email, sifre)
        )

        val userId = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        } else null

        cursor.close()
        db.close()
        return userId
    }

    // ================== NOTE ==================

    fun notEkle(note: Note): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("kullaniciId", note.kullaniciId)
            put("baslik", note.baslik)
            put("icerik", note.icerik)
            put("olusturulmaTarihi", note.olusturulmaTarihi)
            put("guncellenmeTarihi", note.guncellenmeTarihi)
            put("silindi", 0)
        }
        val result = db.insert("notlar", null, values)
        db.close()
        return result != -1L
    }

    fun notGuncelle(note: Note): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("baslik", note.baslik)
            put("icerik", note.icerik)
            put("guncellenmeTarihi", note.guncellenmeTarihi)
        }
        val result = db.update(
            "notlar",
            values,
            "id=?",
            arrayOf(note.id.toString())
        )
        db.close()
        return result > 0
    }

    // NotesActivity'de kullanılan isim
    fun deleteNote(noteId: Int): Boolean {
        return notCopeAt(noteId)
    }

    //  Çöp kutusuna taşı
    fun notCopeAt(noteId: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("silindi", 1)
        }
        val result = db.update("notlar", values, "id=?", arrayOf(noteId.toString()))
        db.close()
        return result > 0
    }

    // Çöp kutusundan geri al
    fun notGeriAl(noteId: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("silindi", 0)
        }
        val result = db.update("notlar", values, "id=?", arrayOf(noteId.toString()))
        db.close()
        return result > 0
    }

    //  Kalıcı sil
    fun notKaliciSil(noteId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("notlar", "id=?", arrayOf(noteId.toString()))
        db.close()
        return result > 0
    }

    fun aktifNotlariGetir(kullaniciId: Int): List<Note> {
        val db = readableDatabase
        val list = mutableListOf<Note>()

        val cursor = db.rawQuery(
            "SELECT * FROM notlar WHERE kullaniciId=? AND silindi=0 ORDER BY guncellenmeTarihi DESC",
            arrayOf(kullaniciId.toString())
        )

        while (cursor.moveToNext()) {
            list.add(cursorToNote(cursor))
        }

        cursor.close()
        db.close()
        return list
    }

    fun copKutusuNotlariGetir(kullaniciId: Int): List<Note> {
        val db = readableDatabase
        val list = mutableListOf<Note>()

        val cursor = db.rawQuery(
            "SELECT * FROM notlar WHERE kullaniciId=? AND silindi=1 ORDER BY guncellenmeTarihi DESC",
            arrayOf(kullaniciId.toString())
        )

        while (cursor.moveToNext()) {
            list.add(cursorToNote(cursor))
        }

        cursor.close()
        db.close()
        return list
    }

    private fun cursorToNote(cursor: Cursor): Note {
        return Note(
            id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
            kullaniciId = cursor.getInt(cursor.getColumnIndexOrThrow("kullaniciId")),
            baslik = cursor.getString(cursor.getColumnIndexOrThrow("baslik")),
            icerik = cursor.getString(cursor.getColumnIndexOrThrow("icerik")),
            olusturulmaTarihi = cursor.getLong(cursor.getColumnIndexOrThrow("olusturulmaTarihi")),
            guncellenmeTarihi = cursor.getLong(cursor.getColumnIndexOrThrow("guncellenmeTarihi")),
            silindi = cursor.getInt(cursor.getColumnIndexOrThrow("silindi"))
        )
    }
}
