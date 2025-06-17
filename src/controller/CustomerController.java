package controller;

import database.DB;
import exception.ApiException;
import exception.BadRequestException;
import exception.NotFoundException;
import model.Customers; // Menggunakan model Customers sesuai keinginanmu
// import model.Booking; // Impor ini jika/ketika kamu mengimplementasikan endpoint bookings
// import model.Review;  // Impor ini jika/ketika kamu mengimplementasikan endpoint reviews
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
}