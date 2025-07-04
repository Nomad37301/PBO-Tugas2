# PBO Tugas 2 - Aplikasi Manajemen Villa

Proyek ini merupakan bagian dari tugas Pemrograman Berorientasi Objek (PBO), yang bertujuan untuk mengimplementasikan konsep OOP, exception handling, dan integrasi database dalam pengembangan aplikasi manajemen villa berbasis Java.

## 📌 Deskripsi Proyek
Aplikasi ini dirancang untuk mengelola proses reservasi villa secara digital. Pelanggan dapat mendaftar, melihat tipe villa, melakukan pemesanan, mengklaim voucher diskon, dan memberikan ulasan setelah menginap. Backend API ini ditujukan untuk dipakai oleh antarmuka client seperti web atau mobile apps.

## 🧠 Teknologi yang Digunakan
- **Java**: Bahasa pemrograman utama.
- **IntelliJ IDEA**: IDE yang digunakan untuk pengembangan dan debugging.
- **SQLite**: Database lokal untuk menyimpan data.
- **Postman**: Digunakan untuk menguji endpoint API aplikasi.
- **Jackson Library**: Untuk parsing JSON.
- **SLF4J**: Logging sistem.
- **JDBC**: Untuk koneksi database.

## 🧩 Fitur Utama
- **Manajemen Customer**: Tambah, ubah, dan hapus data pelanggan.
- **Manajemen Villa & Booking**: Kelola jenis kamar dan pemesanan villa.
- **Manajemen Voucher**: Tambahkan dan kelola voucher diskon.
- **Review & Rating**: Pelanggan dapat memberikan ulasan terhadap pengalaman menginap.
- **Validasi dan Exception Handling**: Penanganan error dengan exception khusus.
- **Database**: Menggunakan SQLite untuk penyimpanan data secara lokal.

## 📁 Struktur Folder
```
PBO-Tugas2-main/
│
├── README.md                  <- Dokumentasi proyek
├── src/
│   ├── app/                   <- Main class untuk menjalankan aplikasi
│   ├── controller/            <- Logika bisnis dan pengontrol fitur
│   ├── model/                 <- Representasi data (POJO)
│   ├── exception/             <- Kelas untuk penanganan error khusus
│   ├── database/              <- Koneksi dan setup database
│   ├── lib/                   <- Library eksternal (Jackson, SQLite JDBC, SLF4J)
```

## 🚀 Cara Menjalankan
1. Buka proyek ini menggunakan **IntelliJ IDEA**.
2. Pastikan sudah menginstal Java JDK 11 atau lebih baru.
3. Build project agar semua dependensi dikenali.
4. Jalankan class `Main.java` yang ada di folder `src/app`.

## 🔧 Setup Environment di IntelliJ IDEA
1. Buka IntelliJ > File > Open > Pilih folder `PBO-Tugas2-main`
2. Pastikan JDK 11 atau lebih tinggi sudah terkonfigurasi
3. Masuk ke File > Project Structure > Libraries dan tambahkan semua JAR dari folder `src/lib`
4. Klik kanan `Main.java` di `src/app` lalu klik Run

## 🚀 Cara Menjalankan dari Terminal
```
cd src
javac -cp "lib/*" app/Main.java
java -cp ".:lib/*" app.Main
```
Untuk Windows, ganti `:` menjadi `;` dalam classpath

## 🔌 Dokumentasi API Singkat

| Method | Endpoint     | Deskripsi                         |
|--------|--------------|------------------------------------|
| GET    | /customers   | Mendapatkan daftar pelanggan       |
| POST   | /customers   | Menambahkan pelanggan baru         |
| GET    | /villas      | Mendapatkan daftar villa           |
| POST   | /bookings    | Melakukan pemesanan villa          |
| POST   | /reviews     | Memberikan ulasan dari pelanggan   |

Contoh JSON POST /customers:
```json
{
  "name": "Made Surya",
  "email": "surya@example.com"
}
```

Response:
```json
{
  "message": "Customer berhasil ditambahkan"
}
```

## 🧪 Pengujian API
1. Jalankan aplikasi dari IntelliJ.
2. Buka **Postman**.
3. Gunakan endpoint-endpoint berikut untuk menguji:
   - `GET /customers` → Menampilkan semua pelanggan
   - `POST /customers` → Menambahkan pelanggan baru
   - `GET /villas` → Menampilkan daftar villa
   - `POST /bookings` → Melakukan booking
   - `POST /reviews` → Mengirim ulasan
4. Pastikan menggunakan format JSON untuk request dan response body.

> Endpoint lengkap dan dokumentasi API bisa dilihat dalam file controller terkait.

## 🛠️ Dependensi
- **Jackson**: Untuk serialisasi dan deserialisasi JSON.
- **SLF4J**: Logging sederhana.
- **SQLite JDBC**: Untuk koneksi database SQLite.O

## 💾 Struktur Database Ringkas
![Erd](https://github.com/user-attachments/assets/a6ff4652-8f8f-488a-9019-783b12825625)
- Customers (id, name, email)
- Bookings (id, customer_id, room_type_id, checkin_date, checkout_date)
- Reviews (id, customer_id, rating, comment)
- RoomType (id, name, price)
- Vouchers (id, code, discount)
