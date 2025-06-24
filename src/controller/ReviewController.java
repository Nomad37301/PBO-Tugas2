package controller;

import database.DB;
import exception.ApiException;
import exception.BadRequestException;
import exception.NotFoundException;
import model.Reviews;
import model.Bookings;
import server.Request;
import server.Response;
import util.JsonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewController {
    private static int getVillaIdFromRoomType(Connection conn, int roomTypeId) throws Exception {
        String query = "SELECT villa_id FROM room_types WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomTypeId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) throw new NotFoundException("Room type tidak ditemukan.");
            return rs.getInt("villa_id");
        }
    }

    public static void getByVillaId(Response res, int villaId) {
        List<Reviews> reviews = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
            SELECT booking_id AS bookings, rating AS star, review_text AS content, '' AS title
            FROM reviews WHERE villa_id = ?
        """)) {

            stmt.setInt(1, villaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reviews r = new Reviews();
                r.bookings = rs.getInt("bookings");
                r.star = rs.getInt("star");
                r.content = rs.getString("content");
                r.title = rs.getString("title"); // kolom dummy tetap di-handle
                reviews.add(r);
            }

            res.setStatus(200);
            res.setBody(JsonUtil.toJson(reviews));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }

    public static void getByCustomerId(Response res, int customerId) {
        List<Reviews> reviews = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("""
            SELECT booking AS bookings, star, title, content
            FROM reviews WHERE villa_id = ?
        """)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reviews r = new Reviews();
                r.bookings = rs.getInt("bookings");
                r.star = rs.getInt("star");
                r.content = rs.getString("content");
                r.title = rs.getString("title"); // kolom dummy, aman
                reviews.add(r);
            }

            res.setStatus(200);
            res.setBody(JsonUtil.toJson(reviews));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }

    public static void createReview(Request req, Response res, int customerId, int bookingId) {
        Reviews review = JsonUtil.fromJson(req.getBody(), Reviews.class);

        if (review == null || review.star == 0 || review.title == null || review.content == null) {
            throw new BadRequestException("Data review tidak lengkap.");
        }

        try (Connection conn = DB.getConnection()) {
            // Cek apakah booking valid & milik customer
            String checkQuery = "SELECT * FROM bookings WHERE id = ? AND customer = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, bookingId);
                checkStmt.setInt(2, customerId);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    throw new NotFoundException("Booking tidak ditemukan atau tidak milik customer ini.");
                }

                // Ambil villa_id dari tabel bookings → lewat room_type
                int roomTypeId = rs.getInt("room_type");
                int villaId = getVillaIdFromRoomType(conn, roomTypeId);

                // INSERT sesuai struktur tabel
                String insertQuery = "INSERT INTO reviews (booking, villa_id, star, title, content) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, bookingId);
                    insertStmt.setInt(2, villaId);
                    insertStmt.setInt(3, review.star);
                    insertStmt.setString(4, review.title);
                    insertStmt.setString(5, review.content);
                    insertStmt.executeUpdate();
                }
            }

            res.setStatus(201);
            res.setBody("{\"message\": \"Review berhasil ditambahkan.\"}");
            res.send();

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }
}
