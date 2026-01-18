package com.tugcebulbul.notalmauygulamasi

import android.app.Application
import com.tugcebulbul.notalmauygulamasi.util.LocaleHelper

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Uygulama açıldığında kaydedilmiş dili uygula
        LocaleHelper.setLocale(this, LocaleHelper.getLocale(this))
    }
}
