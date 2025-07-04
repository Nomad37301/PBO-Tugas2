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
## `Endpoint : /customers`
# `GET`
   - `GET /customers` → Menampilkan semua pelanggan
     ![image](https://github.com/user-attachments/assets/33d8b8f1-0e62-4810-8b66-e4ae877f65c8)

   - `GET /customers/{id}` → Menampilkan Detail Satu Customer
     ![image](https://github.com/user-attachments/assets/b9882079-e399-42e0-b3b2-e07f31c8cfd1)
     
   - `GET /customers/{id}/bookings` Menampilkan Daftar Booking Milik Customer
     ![image](https://github.com/user-attachments/assets/55c8b00f-47dc-4217-b250-f4e3cb34fe91)

   - `GET /customers/{id}/reviews` Menampilkan Ulasan Customer
     ![image](https://github.com/user-attachments/assets/21282fd3-d023-455d-b099-a43b552e91aa)

   # `POST`
   - `POST /customers` → Menambahkan pelanggan baru
     ![image](https://github.com/user-attachments/assets/52ca86b2-3245-4f10-8c84-6fdddce51f6d)

   - `POST /customers/{id}/bookings` Customer Melakukan Pemesanan Villa
     ![image](https://github.com/user-attachments/assets/604ffcf4-a868-4bab-a39d-28df03425276)

   - `POST /customers/{id}/bookings/{id}/reviews`
     ![image](https://github.com/user-attachments/assets/248128e9-e2e8-4086-9e33-ed02871cb3c5)

   # PUT
   - `PUT /customers/{id}` → Update data pelanggan
     ![image](https://github.com/user-attachments/assets/93737ad8-a4cc-4c92-9fe2-c17091f84351)
     
   # DELETE
   - `DELETE /customers/{id}` → Hapus pelanggan
     ![image](https://github.com/user-attachments/assets/4f5a9054-4cb3-4ce0-aa41-9782c807b9cb)

## `Endpoint : /villas`
   - `GET /villas` → Menampilkan daftar villa
     ![image](https://github.com/user-attachments/assets/f333b8a2-bad5-46d8-86cd-90514f9368aa)

   - `GET /villas/{id}` Menampilkan Informasi detail suatu villa
     ![image](https://github.com/user-attachments/assets/c67f8bcf-2b6d-4204-a68a-0e188b091a9b)

   - `GET /villas/{id}/rooms` Menampilkan informasi detail suatu villa dengan lengkap dengan fasilitas dan harga
     ![image](https://github.com/user-attachments/assets/68457cbd-2d89-4b0b-96ae-036a154b18b0)

   - `GET /villas/{id}/bookings` Menampilkan Daftar Semua Booking Pada Suatu Villa
     
   - `GET /villas/{id}/reviews` Menampilkan Daftar Semua Reviews
   - `GET /villas?ci_date={checkin_date}&co_date={checkout_date}` Pencarian ketersediaan vila berdasarkan tanggal check-in dan checkout.
   - `POST /villas` Menambahkan Villa Baru
     ![image](https://github.com/user-attachments/assets/c61f8317-3a07-4ef4-8b33-6da880a8acad)

   - `POST /villas/{id}/rooms` Menambahkan Tipe Kamar Pada Villa
     ![image](https://github.com/user-attachments/assets/f2a7ece3-200a-4360-9e1d-8a57c9d21903)

   - `PUT /villas/{id}` Mengubah data suatu villa
   - 
   - `PUT /villas/{id}/rooms/{id}` Mengubah informasi kamar suatu villa
   - `DELETE /villas/{id}/rooms/{id}` Menghapus Kamar Suatu Villa
   - `DELETE /villas/{id}` Menghapus data suatu villa

## `Endpoint : /Vouchers`
   - `GET /vouchers` Menampilkan vouchers yang tersedia
     ![image](https://github.com/user-attachments/assets/bc6e4d93-42a1-4e2c-bf09-dc58fa916af6)

   - `GET /vouchers/{id}` Menampilkan informasi detail suatu vouchers
     ![image](https://github.com/user-attachments/assets/d1e75ddb-ca92-4110-a708-1c33df4eff5e)

   - `POST /vouchers` Membuat vouchers
     ![image](https://github.com/user-attachments/assets/e4345f7f-b009-42fc-be20-96c3428acf3f)
     
   - `PUT /vouchers/{id}` Mengubah data suatu vouchers
     ![image](https://github.com/user-attachments/assets/938da464-b034-409c-b6ff-90b76b2230bd)

   - `DELETE /vouchers/{id}` Menghapus vouchers
     ![image](https://github.com/user-attachments/assets/837a286a-eeb4-4b6e-b253-6cf22a165fb0)

5. Pastikan menggunakan format JSON untuk request dan response body.

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
