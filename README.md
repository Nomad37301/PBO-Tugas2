# PBO Tugas 2 - Aplikasi Manajemen Villa

Proyek ini merupakan bagian dari tugas Pemrograman Berorientasi Objek (PBO), yang bertujuan untuk mengimplementasikan konsep OOP, exception handling, dan integrasi database dalam pengembangan aplikasi manajemen villa berbasis Java.

## Identitas Kami
1. Jihan Alya Qanita (2405551120)
2. I Ketut Bagus Nugraha (2405551139)
3. ANAK AGUNG GDE PUTRA PURNAMA (2405551172)
4. I Kadek Bayu Mahardika Suputra (2405551117)

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
## `Endpoint : /villas`
# `GET`
  - `GET /villas` → Menampilkan semua villa
  ![Screenshot 2025-07-04 231546](https://github.com/user-attachments/assets/3d4ba8fe-1abc-4140-aae4-0ce6fc0c0a33)
  Endpoint ini digunakan untuk mengambil seluruh data villa yang ada dari database. Permintaan dikirim menggunakan method GET tanpa parameter tambahan. Pada tangkapan layar di atas, terlihat bahwa response berhasil dikembalikan oleh server dengan status 200 OK, dan data villa yang sudah di buat akan ditampilkan dalam format JSON.

- `GET /villas/{id}` → Menampilkan villa berdasarkan id spesifik
![Screenshot 2025-07-04 231558](https://github.com/user-attachments/assets/e32b1791-efb8-4b6a-8f22-aeb92054bdd9)
Endpoint ini digunakan untuk menampilkan informasi detail dari satu villa berdasarkan ID-nya. Permintaan dilakukan menggunakan method GET, dengan parameter ID yang disisipkan langsung pada URL. Pada contoh tangkapan layar di atas, ID villa yang diambil adalah 1, dan server merespons dengan status 200 OK, menampilkan data lengkap villa berupa id, name, descripton, dan address dalam format JSON.

- `GET /villas/{id}/rooms` → Menampilkan kamar villa berdasarkan id villa
![Screenshot 2025-07-04 231624](https://github.com/user-attachments/assets/6ba920eb-88fa-4277-8724-d3fcfe25144e)
Endpoint ini digunakan untuk menampilkan informasi detail dari kamar yang terdapat pada satu villa berdasarkan ID-nya. Permintaan dilakukan menggunakan method GET, dengan parameter ID yang disisipkan langsung pada URL. Pada contoh tangkapan layar di atas, ID villa yang diambil adalah 1, dan server merespons dengan status 200 OK, menampilkan data lengkap dari kamar yang terdapat pada villa tersebut.

- `GET /villas/{id}/bookings` → Menampilkan kamar villa berdasarkan data booking
![Screenshot 2025-07-04 231940](https://github.com/user-attachments/assets/f8bacf7a-706d-423e-90cc-c2f44d0876eb)
Endpoint ini digunakan untuk menampilkan informasi yang mendetail dari booking yang telah dilakukan pada suatu villa berdasarkan ID-nya. Permintaan dilakukan menggunakan method GET, dengan parameter ID yang disisipkan langsung pada URL. Pada contoh tangkapan layar di atas, ID villa yang diambil adalah 1, dan server merespons dengan status 200 OK, menampilkan data lengkap dari booking yang telah terjadi di villa tersebut, seperti ID pelanggan, tanggal check-in dan check-out, dll.

- `GET /villas/{id}/reviews` → Menampilkan kamar villa berdasarkan data review yang dilakukan oleh pelanggan
![Screenshot 2025-07-04 232225](https://github.com/user-attachments/assets/fd69851b-9959-421c-a9b2-c318daa3206f)
Endpoint ini digunakan untuk menampilkan informasi yang mendetail dari review yang telah dilakukan oleh pelanggan yang sudah melakukan booking pada suatu villa berdasarkan ID-nya. Permintaan dilakukan menggunakan method GET, dengan parameter ID yang disisipkan langsung pada URL. Pada contoh tangkapan layar di atas, ID villa yang diambil adalah 1, dan server merespons dengan status 200 OK, menampilkan data dari booking yang telah terjadi di villa tersebut, seperti ID review, ratingnya, dan isi dari reviewnya tersebut.

- `GET /villas?ci_date={checkin_date}&co_date={checkout_date} ` → Pencarian ketersediaan vila berdasarkan tanggal check-in dan checkout
![Screenshot 2025-07-04 232327](https://github.com/user-attachments/assets/35fcec49-ba30-480b-9926-18430d84a2d9)
Endpoint ini digunakan untuk menampilkan informasi dari ketersediaan vila berdasarkan tanggal check-in dan check-out yang telah dilakukan oleh pelanggan yang sudah melakukan booking. Permintaan dilakukan menggunakan method GET, dengan parameter date dari check-in dan check-out yang disisipkan langsung pada URL. Pada contoh tangkapan layar di atas, date yang digunakan dan hasil yang sudah tertera.

- `POST /villas ` → Menambahkan data vila
![Screenshot 2025-07-04 231300](https://github.com/user-attachments/assets/4dbbc6bf-3839-47e4-b9aa-d6072a775311)
Endpoint ini digunakan untuk menambahkan data vila yang ingin di tambahkan pada database.

- `POST /villas/{id}/rooms ` → Menambahkan tipe kamar pada vila
![Screenshot 2025-07-04 231456](https://github.com/user-attachments/assets/7b2f7c39-1b90-4b94-96c7-788cda78fb38)
Endpoint ini digunakan untuk menambahkan data kamar dari vila yang sudah ada pada database.

- `PUT /villas/{id} ` → Mengubah data suatu vila
![Screenshot 2025-07-04 232516](https://github.com/user-attachments/assets/4cd5ae86-82ea-4628-8fb5-8e63efa437ba)
Endpoint ini digunakan untuk mengubah data dari vila yang sudah ada pada database. Endpoint ini dapat digunakan bila kita ingin melakukan update data semisalnya serperti nama pada vila yang sudah ada, alamat maupun deskripsi.

- `PUT /villas/{id}/rooms/{id}  ` → Mengubah informasi kamar suatu vila
![Screenshot 2025-07-04 232627](https://github.com/user-attachments/assets/7629d6f1-c162-4274-9078-9ef4791b4f04)
Endpoint ini digunakan untuk mengubah data kamar dari vila yang sudah ada pada database. Endpoint ini dapat digunakan bila kita ingin melakukan update data semisalnya serperti status dari beberapa service yang terdapat pada kamar pada vila yang sudah ada, bed size maupun harga.

- `DELETE /villas/{id}/rooms/{id}   ` → Menghapus kamar suatu vila
![Screenshot 2025-07-04 232659](https://github.com/user-attachments/assets/8fca15fa-ada0-42ed-a698-2f7c136db841)
Endpoint ini digunakan untuk menghapus data kamar yang terdapat pada vila yang sudah ada pada database.

- `DELETE /villas/{id}   ` → Menghapus data suatu vila
![Screenshot 2025-07-04 232727](https://github.com/user-attachments/assets/c656e239-3772-4b0f-bb91-84700c9f9c5b)
Endpoint ini digunakan untuk menghapus data dari vila yang sudah ada pada database sebelumnya.

## `Endpoint : /customers`
# `GET`
   - `GET /customers` → Menampilkan semua pelanggan
     ![image](https://github.com/user-attachments/assets/33d8b8f1-0e62-4810-8b66-e4ae877f65c8)
     Endpoint ini digunakan untuk mengambil seluruh data pelanggan dari database. Permintaan dikirim menggunakan method GET tanpa parameter tambahan. Pada tangkapan layar di atas, terlihat bahwa response berhasil dikembalikan oleh server dengan status 200 OK, dan data pelanggan ditampilkan dalam format JSON lengkap, mencakup ID, nama, email, dan nomor telepon.

   - `GET /customers/{id}` → Menampilkan Detail Satu Customer
     ![image](https://github.com/user-attachments/assets/b9882079-e399-42e0-b3b2-e07f31c8cfd1)
     Endpoint ini digunakan untuk menampilkan informasi detail dari satu pelanggan berdasarkan ID-nya. Permintaan dilakukan menggunakan method GET, dengan parameter ID yang disisipkan langsung pada URL. Pada contoh tangkapan layar di atas, ID pelanggan yang diambil adalah 4, dan server merespons dengan status 200 OK, menampilkan data lengkap pelanggan berupa id, name, email, dan phone dalam format JSON.
     
   - `GET /customers/{id}/bookings` Menampilkan Daftar Booking Milik Customer
     ![image](https://github.com/user-attachments/assets/55c8b00f-47dc-4217-b250-f4e3cb34fe91)
     Endpoint ini berfungsi untuk menampilkan seluruh data booking yang dimiliki oleh seorang customer tertentu, berdasarkan ID-nya. Permintaan dikirim menggunakan method GET dengan format URL /customers/{id}/bookings. Pada contoh di atas, sistem mengembalikan daftar pemesanan milik customer dengan ID 4, berisi informasi seperti id, room_type, checkin_date, checkout_date, price, dan status booking. Respons berhasil dikembalikan dengan status 200 OK, menandakan permintaan berhasil diproses oleh server.
     
   - `GET /customers/{id}/reviews` Menampilkan Ulasan Customer
     ![image](https://github.com/user-attachments/assets/21282fd3-d023-455d-b099-a43b552e91aa)
     Endpoint ini digunakan untuk menampilkan semua ulasan atau review yang telah diberikan oleh seorang customer tertentu. Permintaan ini menggunakan method GET dan ditujukan ke URL /customers/{id}/reviews, dengan {id} adalah ID dari customer yang dimaksud. Pada contoh tangkapan layar di atas, data ulasan yang ditampilkan mencakup booking, star, title, dan content. Respons dikembalikan dengan status 200 OK, yang menandakan bahwa permintaan berhasil dan ulasan berhasil ditampilkan.

   # `POST`
   - `POST /customers` → Menambahkan pelanggan baru
     ![image](https://github.com/user-attachments/assets/52ca86b2-3245-4f10-8c84-6fdddce51f6d)
     Endpoint ini berfungsi untuk menambahkan data pelanggan baru ke dalam sistem. Permintaan ini dikirim menggunakan method POST dengan body berisi informasi pelanggan seperti name, email, dan phone. Setelah data dikirim, server akan memproses permintaan dan memberikan respons berupa data pelanggan yang telah dibuat beserta ID-nya. Respons yang diterima berupa JSON dan disertai status 201 Created, menandakan bahwa pelanggan berhasil ditambahkan ke dalam database.
     
   - `POST /customers/{id}/bookings` Customer Melakukan Pemesanan Villa
     ![image](https://github.com/user-attachments/assets/604ffcf4-a868-4bab-a39d-28df03425276)
     Endpoint ini digunakan ketika seorang customer ingin melakukan pemesanan villa. Permintaan dikirim dengan method POST dan menyertakan ID customer pada path URL. Di dalam body permintaan, data yang dikirim meliputi informasi pemesanan seperti room_type, checkin_date, checkout_date, dan dapat juga menyertakan voucher jika ada. Setelah permintaan dikirim, server akan mencatat pemesanan dan mengembalikan detail booking yang telah dibuat dalam format JSON, termasuk harga akhir dan status pembayaran. Status respons 201 Created menandakan bahwa pemesanan berhasil dilakukan.

   - `POST /customers/{id}/bookings/{id}/reviews`
     ![image](https://github.com/user-attachments/assets/248128e9-e2e8-4086-9e33-ed02871cb3c5)
     Endpoint ini memungkinkan customer memberikan ulasan terhadap villa yang telah mereka pesan. Dengan menggunakan method POST, client mengirim permintaan ke endpoint yang menyertakan ID customer dan ID booking. Body permintaan harus berisi data ulasan seperti star (1–5), title, dan content. Sebelum review disimpan, server akan memverifikasi bahwa pemesanan tersebut memang dimiliki oleh customer terkait. Jika valid, status has_checkin dan has_checkout akan diperbarui menjadi 1, lalu ulasan disimpan. Respons 201 Created menandakan ulasan berhasil ditambahkan ke dalam sistem.
     
   # PUT
   - `PUT /customers/{id}` → Update data pelanggan
     ![image](https://github.com/user-attachments/assets/93737ad8-a4cc-4c92-9fe2-c17091f84351)
     Endpoint ini digunakan untuk memperbarui informasi dari pelanggan tertentu. Permintaan dikirim menggunakan method PUT ke endpoint dengan menyertakan ID pelanggan. Di dalam body permintaan, client mengisi data baru seperti name, email, dan phone. Server akan memverifikasi apakah pelanggan dengan ID tersebut ada, lalu memperbarui datanya sesuai dengan input yang diberikan. Jika berhasil, server merespons dengan status 200 OK beserta data pelanggan yang telah diperbarui.
     
   # DELETE
   - `DELETE /customers/{id}` → Hapus pelanggan
     ![image](https://github.com/user-attachments/assets/4f5a9054-4cb3-4ce0-aa41-9782c807b9cb)
     Endpoint ini digunakan untuk menghapus data pelanggan berdasarkan ID yang diberikan pada URL. Permintaan dikirim dengan method DELETE. Setelah server memverifikasi keberadaan pelanggan, data pelanggan tersebut akan dihapus dari database. Jika berhasil, server mengembalikan respons dengan status 200 OK dan pesan konfirmasi bahwa pelanggan telah berhasil dihapus.

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
     Endpoint ini digunakan untuk mengambil daftar semua voucher yang tersedia. Permintaan dikirim menggunakan method GET tanpa perlu mengisi body request. Server akan membalas dengan data berupa array JSON yang berisi informasi setiap voucher, seperti id, name, code, description, discount, start_date, dan end_date. Respons berhasil ditandai dengan status 200 OK.

   - `GET /vouchers/{id}` Menampilkan informasi detail suatu vouchers
     ![image](https://github.com/user-attachments/assets/d1e75ddb-ca92-4110-a708-1c33df4eff5e)
     Endpoint ini digunakan untuk menampilkan detail dari satu voucher berdasarkan ID-nya. Permintaan dilakukan menggunakan method GET dengan menambahkan id voucher di bagian akhir URL. Server akan merespons dengan data voucher yang sesuai dalam format JSON. Informasi yang ditampilkan meliputi nama, kode, deskripsi, besar diskon, tanggal mulai, dan tanggal berakhir. Jika ID tidak ditemukan, server akan mengembalikan pesan error.

   - `POST /vouchers` Membuat vouchers
     ![image](https://github.com/user-attachments/assets/e4345f7f-b009-42fc-be20-96c3428acf3f)
     Endpoint ini digunakan untuk menambahkan data voucher baru ke sistem. Permintaan dikirim dengan method POST dan berisi data voucher seperti name, code, description, discount, start_date, dan end_date dalam format form URL-encoded. Setelah berhasil, server akan merespons dengan pesan konfirmasi bahwa voucher berhasil ditambahkan. Endpoint ini sangat berguna untuk keperluan promosi atau potongan harga pada sistem pemesanan.
     
   - `PUT /vouchers/{id}` Mengubah data suatu vouchers
     ![image](https://github.com/user-attachments/assets/938da464-b034-409c-b6ff-90b76b2230bd)
     Endpoint ini digunakan untuk memperbarui informasi voucher yang sudah ada berdasarkan ID-nya. Permintaan dikirim menggunakan method PUT dan berisi data yang ingin diperbarui, seperti name, code, description, discount, start_date, dan end_date. Data dikirim dalam format form URL-encoded. Jika ID voucher ditemukan dan data valid, maka server akan memberikan respons berupa pesan bahwa voucher berhasil diupdate. Fitur ini penting ketika admin ingin mengubah detail promo tanpa harus menghapus dan membuat ulang.

   - `DELETE /vouchers/{id}` Menghapus vouchers
     ![image](https://github.com/user-attachments/assets/837a286a-eeb4-4b6e-b253-6cf22a165fb0)
     Endpoint ini digunakan untuk menghapus voucher tertentu berdasarkan ID-nya. Permintaan dikirim menggunakan method DELETE. Jika ID yang diberikan sesuai dengan voucher yang ada, maka sistem akan menghapus data tersebut dari daftar voucher dan memberikan respons berupa pesan konfirmasi bahwa voucher berhasil dihapus. Fitur ini berguna untuk mengelola promo yang sudah tidak berlaku atau ingin dihapus dari sistem.
     
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
