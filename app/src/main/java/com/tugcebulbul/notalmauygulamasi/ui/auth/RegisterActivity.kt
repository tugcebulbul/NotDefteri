package com.tugcebulbul.notalmauygulamasi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tugcebulbul.notalmauygulamasi.R
import com.tugcebulbul.notalmauygulamasi.data.db.NoteDatabaseHelper
import com.tugcebulbul.notalmauygulamasi.databinding.ActivityRegisterBinding
import com.tugcebulbul.notalmauygulamasi.ui.MainActivity
import com.tugcebulbul.notalmauygulamasi.util.SessionManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: NoteDatabaseHelper

    private var sifreGorunur = false
    private var sifreTekrarGorunur = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = NoteDatabaseHelper(this)

        sifreGizlemeAyarla()
        registerClick()
        goLoginClick()
    }

    // Şifre gizleme / gösterme
    private fun sifreGizlemeAyarla() {

        // Şifre alanı
        binding.etRegisterPassword.setOnTouchListener { _, event ->
            val drawableEnd = 2
            if (event.rawX >= (binding.etRegisterPassword.right -
                        binding.etRegisterPassword.compoundDrawables[drawableEnd].bounds.width())
            ) {
                sifreGorunur = !sifreGorunur
                binding.etRegisterPassword.inputType =
                    if (sifreGorunur)
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    else
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                binding.etRegisterPassword.setSelection(binding.etRegisterPassword.text.length)
                binding.etRegisterPassword.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.lock,
                    0,
                    if (sifreGorunur) R.drawable.eye_on else R.drawable.eye_off,
                    0
                )
                true
            } else false
        }

        // Şifre tekrar alanı
        binding.etRegisterConfirmPassword.setOnTouchListener { _, event ->
            val drawableEnd = 2
            if (event.rawX >= (binding.etRegisterConfirmPassword.right -
                        binding.etRegisterConfirmPassword.compoundDrawables[drawableEnd].bounds.width())
            ) {
                sifreTekrarGorunur = !sifreTekrarGorunur
                binding.etRegisterConfirmPassword.inputType =
                    if (sifreTekrarGorunur)
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    else
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                binding.etRegisterConfirmPassword.setSelection(binding.etRegisterConfirmPassword.text.length)
                binding.etRegisterConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.lock,
                    0,
                    if (sifreTekrarGorunur) R.drawable.eye_on else R.drawable.eye_off,
                    0
                )
                true
            } else false
        }
    }

    // Kayıt işlemi
    private fun registerClick() {
        binding.btnRegister.setOnClickListener {

            val email = binding.etRegisterEmail.text.toString().trim()
            val sifre = binding.etRegisterPassword.text.toString().trim()
            val sifreTekrar = binding.etRegisterConfirmPassword.text.toString().trim()

            // Boş alan kontrolü
            if (email.isEmpty() || sifre.isEmpty() || sifreTekrar.isEmpty()) {
                Toast.makeText(this, getString(R.string.all_fields_required), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Şifre uzunluğu
            if (sifre.length < 6) {
                Toast.makeText(this, getString(R.string.password_min_length), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Şifre eşleşme
            if (sifre != sifreTekrar) {
                Toast.makeText(this, getString(R.string.passwords_not_match), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Email kontrolü
            if (dbHelper.isUserExists(email)) {
                Toast.makeText(this, getString(R.string.email_already_exists), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kullanıcı ekle
            val eklendiMi = dbHelper.insertUser(email, sifre)

            if (eklendiMi) {
                val userId = dbHelper.login(email, sifre)
                if (userId != null) {
                    SessionManager.saveUser(this, userId, email)
                    Toast.makeText(this, getString(R.string.register_successful), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Login ekranına geçiş
    private fun goLoginClick() {
        binding.tvGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
