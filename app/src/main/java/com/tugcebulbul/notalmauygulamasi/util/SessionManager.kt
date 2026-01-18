package com.tugcebulbul.notalmauygulamasi.util

import android.content.Context
import android.content.Intent
import com.tugcebulbul.notalmauygulamasi.ui.auth.LoginActivity

object SessionManager {

    private const val PREF_NAME = "user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "user_email"

    //  Kullanıcı giriş bilgilerini kaydet
    fun saveUser(context: Context, userId: Int, email: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    //  Kullanıcı ID al
    fun getUserId(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    //  Kullanıcı email al
    fun getUserEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, null)
    }

    //  Kullanıcı giriş yapmış mı?
    fun isLoggedIn(context: Context): Boolean {
        return getUserId(context) != -1
    }

    // Çıkış yap (SADECE manuel logout için)
    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
