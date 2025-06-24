package controller;

import database.DB;
import exception.ApiException;
import exception.BadRequestException;
import exception.NotFoundException;
import model.Customers;
import model.Bookings;
import model.Reviews;
import server.Request;
import server.Response;
import util.JsonUtil;
import util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    // GET /customers
    // Daftar semua customer
    public static void getAll(Response res) {
        List<Customers> customers = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {

            while (rs.next()) {
                Customers c = new Customers(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                customers.add(c);
            }

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(customers));
            res.send();

        } catch (Exception e) {
            System.err.println("Error getting all customers: " + e.getMessage());
            throw new ApiException(500, "Database Error: " + e.getMessage());
        }
    }

    // GET /customers/{id}
    // Informasi detail seorang customer
    public static void getDetail(Response res, int customerId) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE id = ?")) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new NotFoundException("Customer with ID " + customerId + " not found.");
            }

            Customers customer = new Customers(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
            );

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(customer));
            res.send();

        } catch (Exception e) {
            System.err.println("Error getting customer detail (ID: " + customerId + "): " + e.getMessage());
            throw new ApiException(500, "Error: " + e.getMessage());
        }
    }

    // POST /customers
    // Menambahkan customer baru (registrasi customer)
    public static void createCustomer(Request req, Response res) {
        Customers customer = JsonUtil.fromJson(req.getBody(), Customers.class);

        if (customer == null ||
                Validator.isEmpty(customer.name) ||
                Validator.isEmpty(customer.email)) {
            throw new BadRequestException("Invalid customer data. 'name' and 'email' are required.");
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)")) { // <--- HAPUS Statement.RETURN_GENERATED_KEYS DI SINI

            stmt.setString(1, customer.name);
            stmt.setString(2, customer.email);
            stmt.setString(3, customer.phone);
            stmt.executeUpdate();

            // --- Ganti bagian ini untuk mendapatkan ID yang dihasilkan ---
            try (Statement idStmt = conn.createStatement();
                 ResultSet keys = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                if (keys.next()) {
                    customer.id = keys.getInt(1);
                }
            }
            // --- Akhir bagian pengganti ---

            res.setStatus(201); // 201 Created
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(customer));
            res.send();

        } catch (Exception e) {
            System.err.println("Error creating customer: " + e.getMessage());
            throw new ApiException(500, "Database insert error: " + e.getMessage());
        }
    }

    // PUT /customers/{id}
    // Mengubah data seorang customer
    public static void updateCustomer(Request req, Response res, int customerId) {
        Customers customer = JsonUtil.fromJson(req.getBody(), Customers.class);

        // Validasi data: name dan email adalah wajib (menggunakan isEmpty saja)
        if (customer == null ||
                Validator.isEmpty(customer.name) ||
                Validator.isEmpty(customer.email)) {
            throw new BadRequestException("Invalid customer data for update. 'name' and 'email' are required.");
        }

        try (Connection conn = DB.getConnection()) {
            // Cek apakah customer dengan ID ini ada
            PreparedStatement check = conn.prepareStatement("SELECT id FROM customers WHERE id = ?");
            check.setInt(1, customerId);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                throw new NotFoundException("Customer with ID " + customerId + " not found.");
            }

            PreparedStatement update = conn.prepareStatement(
                    "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?");
            update.setString(1, customer.name);
            update.setString(2, customer.email);
            update.setString(3, customer.phone);
            update.setInt(4, customerId);
            update.executeUpdate();

            customer.id = customerId; // Set ID pada objek customer yang dikembalikan
            res.setStatus(200); // 200 OK
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(customer));
            res.send();

        } catch (Exception e) {
            System.err.println("Error updating customer (ID: " + customerId + "): " + e.getMessage());
            throw new ApiException(500, "Update error: " + e.getMessage());
        }
    }

    // DELETE /customers/{id}
    // Menghapus data seorang customer (Endpoint ini tidak ada di tugas PDF, tapi umumnya ada untuk CRUD lengkap)
    public static void deleteCustomer(Response res, int customerId) {
        try (Connection conn = DB.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT id FROM customers WHERE id = ?")) {
            check.setInt(1, customerId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) throw new NotFoundException("Customer with ID " + customerId + " not found.");

            PreparedStatement del = conn.prepareStatement("DELETE FROM customers WHERE id = ?");
            del.setInt(1, customerId);
            del.executeUpdate();

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody("{\"message\": \"Customer deleted successfully\"}");
            res.send();

        } catch (Exception e) {
            System.err.println("Delete error: " + e.getMessage());
            throw new ApiException(500, "Delete error: " + e.getMessage());
        }
    }

    public static void getBookings(Response res, int customerId) {
        List<Bookings> bookings = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE customer = ?")) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bookings b = new Bookings();
                b.id = rs.getInt("id");
                b.customer = rs.getInt("customer");
                b.room_type = rs.getInt("room_type");
                b.checkin_date = rs.getString("checkin_date");
                b.checkout_date = rs.getString("checkout_date");
                b.price = rs.getInt("price");
                b.voucher = rs.getInt("voucher");
                b.final_price = rs.getInt("final_price");
                b.payment_status = rs.getString("payment_status");
                b.has_checkin = rs.getBoolean("has_checkin");
                b.has_checkout = rs.getBoolean("has_checkout");
                bookings.add(b);
            }

            res.setStatus(200);
            res.setBody(JsonUtil.toJson(bookings));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }

    public static void createBooking(Request req, Response res, int customerId) {
        Bookings b = JsonUtil.fromJson(req.getBody(), Bookings.class);

        if (b == null || b.room_type == 0 || b.checkin_date == null || b.checkout_date == null) {
            throw new BadRequestException("Data booking tidak lengkap.");
        }

        try (Connection conn = DB.getConnection()) {
            // Validasi ketersediaan kamar
            String check = """
            SELECT * FROM bookings
            WHERE room_type = ? AND NOT (
                checkout_date <= ? OR checkin_date >= ?
            )
        """;

            try (PreparedStatement stmt = conn.prepareStatement(check)) {
                stmt.setInt(1, b.room_type);
                stmt.setString(2, b.checkin_date);
                stmt.setString(3, b.checkout_date);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    throw new BadRequestException("Kamar tidak tersedia pada tanggal tersebut.");
                }
            }

            // Insert booking
            String insert = """
            INSERT INTO bookings (customer, room_type, checkin_date, checkout_date, price, voucher, final_price, payment_status, has_checkin, has_checkout)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, b.room_type);
                stmt.setString(3, b.checkin_date);
                stmt.setString(4, b.checkout_date);
                stmt.setInt(5, b.price);
                stmt.setInt(6, b.voucher);
                stmt.setInt(7, b.final_price);
                stmt.setString(8, b.payment_status != null ? b.payment_status : "pending");
                stmt.setBoolean(9, false);
                stmt.setBoolean(10, false);
                stmt.executeUpdate();
            }

            res.setStatus(201);
            res.setBody("{\"message\":\"Booking berhasil dibuat.\"}");
            res.send();

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }
}