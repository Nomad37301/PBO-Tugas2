package controller;

import exception.ApiException;
import exception.BadRequestException;
import model.Reviews;
import server.Request;
import database.DB;
import server.Response;
import util.JsonUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReviewController {
    // POST /customers/{id}/bookings/{id}/reviews
    public static void createReview(Request req, Response res, int customerId, int bookingId) {
        Reviews review = JsonUtil.fromJson(req.getBody(), Reviews.class);

        if (review == null || review.star < 1 || review.star > 5 ||
                review.title == null || review.content == null) {
            throw new BadRequestException("Data review tidak lengkap atau invalid.");
        }

        try (Connection conn = DB.getConnection()) {
            // Sebelum insert review
            String checkSql = "SELECT * FROM bookings WHERE id = ? AND customer = ?";
            try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
                stmt.setInt(1, bookingId);
                stmt.setInt(2, customerId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    throw new BadRequestException("Booking tidak ditemukan.");
                }
            }

            // Paksa update booking jadi checkout
            String updateSql = "UPDATE bookings SET has_checkin = 1, has_checkout = 1 WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                stmt.setInt(1, bookingId);
                stmt.executeUpdate();
            }

            // Insert review
            String insertSql = """
                INSERT INTO reviews (booking, star, title, content)
                VALUES (?, ?, ?, ?)
            """;
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                stmt.setInt(1, bookingId);
                stmt.setInt(2, review.star);
                stmt.setString(3, review.title);
                stmt.setString(4, review.content);
                stmt.executeUpdate();
            }

            res.setStatus(201);
            res.setBody("{\"message\":\"Review berhasil ditambahkan.\"}");
            res.send();

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }
}
