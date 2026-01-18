#📝 Not Defteri Uygulaması (Android)

Bu proje, Android platformu için geliştirilmiş bir Not Defteri Uygulamasıdır.
Uygulamada kullanıcılar notlarını ekleyebilir, düzenleyebilir, silebilir ve çöp kutusu üzerinden yönetebilir.
Veriler yerel olarak SQLite veritabanı kullanılarak saklanmaktadır.

##🚀 Özellikler

📌 Not ekleme

✏️ Not düzenleme

🗑️ Not silme

♻️ Çöp kutusuna taşıma ve geri yükleme

👤 Kullanıcı kayıt sistemi

💾 Yerel veritabanı (SQLite) kullanımı

🌍 Çoklu dil desteği (Türkçe / İngilizce)

##🛠️ Kullanılan Teknolojiler

Programlama Dili: Kotlin

Platform: Android

Veritabanı: SQLite

IDE: Android Studio

##📂 Veritabanı Yapısı

Uygulamada tüm veriler NoteDatabaseHelper sınıfı üzerinden yönetilmektedir.

Bu sınıf sayesinde:

Not ekleme

Not silme

Not güncelleme

Notları çöp kutusuna taşıma

Çöp kutusundan geri yükleme

işlemleri SQLite veritabanı üzerinden gerçekleştirilmektedir.

##👤 Kullanıcı Sistemi

RegisterActivity ekranı ile kullanıcı kayıt işlemleri yapılır

Kullanıcı e-posta ve şifre bilgileri ile sisteme kayıt olabilir

Notlar uygulama içerisinde yerel olarak saklanır

##📱 Ekranlar

Kayıt Ekranı (Register)

Ana Sayfa (Not Listesi)

Not Ekle / Düzenle

Çöp Kutusu
