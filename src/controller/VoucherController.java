package controller;

import database.DB;
import exception.ApiException;
import exception.BadRequestException;
import model.Vouchers;
import server.Request;
import server.Response;
import util.JsonUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherController {

    public static void getAllVouchers(Response res) {
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM vouchers")) {

            List<Vouchers> voucherList = new ArrayList<>();
            while (rs.next()) {
                Vouchers v = new Vouchers();
                v.id = rs.getInt("id");
                v.name = rs.getString("name");
                v.code = rs.getString("code");
                v.description = rs.getString("description");
                v.discount = rs.getFloat("discount");
                v.start_date = rs.getString("start_date");
                v.end_date = rs.getString("end_date");
                voucherList.add(v);
            }

            res.setStatus(200);
            res.setBody(JsonUtil.toJson(voucherList));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }

    public static void getVoucherById(Response res, int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM vouchers WHERE id = ?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) throw new BadRequestException("Voucher tidak ditemukan");

            Vouchers v = new Vouchers();
            v.id = rs.getInt("id");
            v.name = rs.getString("name");
            v.code = rs.getString("code");
            v.description = rs.getString("description");
            v.discount = rs.getFloat("discount");
            v.start_date = rs.getString("start_date");
            v.end_date = rs.getString("end_date");

            res.setStatus(200);
            res.setBody(JsonUtil.toJson(v));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }

    public static void createVoucher(Request req, Response res) {
        Vouchers v = JsonUtil.fromJson(req.getBody(), Vouchers.class);
        if (v == null || v.code == null || v.name == null) {
            throw new BadRequestException("Data voucher tidak lengkap.");
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO vouchers (name, code, description, discount, start_date, end_date)
                VALUES (?, ?, ?, ?, ?, ?)
            """)) {

            stmt.setString(1, v.name);
            stmt.setString(2, v.code);
            stmt.setString(3, v.description);
            stmt.setFloat(4, v.discount);
            stmt.setString(5, v.start_date);
            stmt.setString(6, v.end_date);
            stmt.executeUpdate();

            res.setStatus(201);
            res.setBody("{\"message\":\"Voucher berhasil ditambahkan\"}");
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }

    public static void updateVoucher(Request req, Response res, int id) {
        Vouchers v = JsonUtil.fromJson(req.getBody(), Vouchers.class);
        if (v == null || v.code == null || v.name == null) {
            throw new BadRequestException("Data voucher tidak lengkap.");
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
                UPDATE vouchers SET name=?, code=?, description=?, discount=?, start_date=?, end_date=? WHERE id=?
            """)) {

            stmt.setString(1, v.name);
            stmt.setString(2, v.code);
            stmt.setString(3, v.description);
            stmt.setFloat(4, v.discount);
            stmt.setString(5, v.start_date);
            stmt.setString(6, v.end_date);
            stmt.setInt(7, id);
            int updated = stmt.executeUpdate();

            if (updated == 0) throw new BadRequestException("Voucher tidak ditemukan");

            res.setStatus(200);
            res.setBody("{\"message\":\"Voucher berhasil diupdate\"}");
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }

    public static void deleteVoucher(Response res, int id) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM vouchers WHERE id = ?")) {

            stmt.setInt(1, id);
            int deleted = stmt.executeUpdate();

            if (deleted == 0) throw new BadRequestException("Voucher tidak ditemukan");

            res.setStatus(200);
            res.setBody("{\"message\":\"Voucher berhasil dihapus\"}");
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }
}
