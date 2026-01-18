package com.tugcebulbul.notalmauygulamasi.data.model

data class Note(
    val id: Int = 0,
    val kullaniciId: Int,
    val baslik: String,
    val icerik: String,
    val olusturulmaTarihi: Long,
    val guncellenmeTarihi: Long,
    val silindi: Int = 0 // 0 = aktif, 1 = çöp kutusunda
)
