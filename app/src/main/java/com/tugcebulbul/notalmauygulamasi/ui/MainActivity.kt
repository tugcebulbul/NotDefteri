package com.tugcebulbul.notalmauygulamasi.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tugcebulbul.notalmauygulamasi.databinding.ActivityMainBinding
import com.tugcebulbul.notalmauygulamasi.ui.auth.LoginActivity
import com.tugcebulbul.notalmauygulamasi.ui.notes.NotesActivity
import com.tugcebulbul.notalmauygulamasi.util.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Notlar ekranına git
        binding.btnMyNotes.setOnClickListener {
            startActivity(Intent(this, NotesActivity::class.java))
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            SessionManager.logout(this)
        }
    }

    override fun onStart() {
        super.onStart()

        //  Session kontrolü BURADA yapılmalı
        if (!SessionManager.isLoggedIn(this)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        //  Kullanıcı emailini göster
        val email = SessionManager.getUserEmail(this)
        binding.tvUserEmail.text = email ?: "Unknown user"
    }
}
