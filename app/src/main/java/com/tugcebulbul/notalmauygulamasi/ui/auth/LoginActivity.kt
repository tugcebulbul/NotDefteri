package com.tugcebulbul.notalmauygulamasi.ui.auth

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tugcebulbul.notalmauygulamasi.R
import com.tugcebulbul.notalmauygulamasi.data.db.NoteDatabaseHelper
import com.tugcebulbul.notalmauygulamasi.databinding.ActivityLoginBinding
import com.tugcebulbul.notalmauygulamasi.ui.MainActivity
import com.tugcebulbul.notalmauygulamasi.util.LocaleHelper
import com.tugcebulbul.notalmauygulamasi.util.SessionManager
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: NoteDatabaseHelper
    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {

        // Uygulama açılır açılmaz kaydedilmiş dili uygula
        LocaleHelper.setLocale(this, LocaleHelper.getLocale(this))

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = NoteDatabaseHelper(this)

        setupPasswordToggle()
        setupLoginClick()
        setupRegisterClick()
        setupLanguageChangeClick()
    }

    // Şifre göster / gizle
    private fun setupPasswordToggle() {
        binding.etPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2
                val drawable = binding.etPassword.compoundDrawables[drawableEnd]

                if (drawable != null &&
                    event.rawX >= (binding.etPassword.right - drawable.bounds.width())
                ) {
                    passwordVisible = !passwordVisible

                    binding.etPassword.inputType =
                        if (passwordVisible)
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        else
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                    binding.etPassword.setSelection(binding.etPassword.text.length)

                    binding.etPassword.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.lock,
                        0,
                        if (passwordVisible) R.drawable.eye_on else R.drawable.eye_off,
                        0
                    )
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    //  Giriş işlemi
    private fun setupLoginClick() {
        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.email_password_required),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val userId = dbHelper.login(email, password)

            if (userId != null) {
                SessionManager.saveUser(this, userId, email)

                Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.invalid_email_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Kayıt ol ekranına geçiş
    private fun setupRegisterClick() {
        binding.btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    // Dil değiştirme butonu
    private fun setupLanguageChangeClick() {
        binding.btnChangeLanguage.setOnClickListener {
            val languages = arrayOf("English", "Türkçe")
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_language))
                .setItems(languages) { _, which ->
                    when (which) {
                        0 -> LocaleHelper.setLocale(this, "en")
                        1 -> LocaleHelper.setLocale(this, "tr")
                    }
                    recreate() // Aktiviteyi yeniden başlat
                }
                .show()
        }
    }
}
