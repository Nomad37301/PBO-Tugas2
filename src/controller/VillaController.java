package controller;

import database.DB;
import exception.ApiException;
import exception.BadRequestException;
import exception.NotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.RoomType;
import model.Villa;
import server.Request;
import server.Response;
import util.JsonUtil;
import util.Validator;


public class VillaController {
    public static void getAll(Response res) {
        List<Villa> villas = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM villas")) {

            while (rs.next()) {
                Villa v = new Villa(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("address")
                );
                villas.add(v);
            }

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(villas));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database Error: " + e.getMessage());
        }
    }

    public static void getDetail(Response res, int villaId) {
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM villas WHERE id = " + villaId)) {

            if (!rs.next()) {
                throw new NotFoundException("Villa with ID " + villaId + " not found.");
            }

            Villa villa = new Villa(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("address")
            );

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(villa));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Error: " + e.getMessage());
        }
    }

    public static void createVilla(Request req, Response res) {
        model.Villa villa = JsonUtil.fromJson(req.getBody(), model.Villa.class);

        if (villa == null ||
                Validator.isEmpty(villa.name) ||
                Validator.isEmpty(villa.description) ||
                Validator.isEmpty(villa.address)) {
            throw new BadRequestException("Invalid villa data. 'name', 'description', and 'address' are required.");
        }

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO villas (name, description, address) VALUES (?, ?, ?)");
            stmt.setString(1, villa.name);
            stmt.setString(2, villa.description);
            stmt.setString(3, villa.address);
            stmt.executeUpdate();

            // Ambil ID terbaru
            Statement getIdStmt = conn.createStatement();
            ResultSet rs = getIdStmt.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                villa.id = rs.getInt(1);
            }

            res.setStatus(201);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(villa));
            res.send();
        } catch (Exception e) {
            throw new ApiException(500, "Database insert error: " + e.getMessage());
        }
    }

        public static void updateVilla(Request req, Response res, int villaId) {
        model.Villa villa = JsonUtil.fromJson(req.getBody(), model.Villa.class);

        if (villa == null ||
                Validator.isEmpty(villa.name) ||
                Validator.isEmpty(villa.description) ||
                Validator.isEmpty(villa.address)) {
            throw new BadRequestException("Invalid data. 'name', 'description', and 'address' are required.");
        }

        try (Connection conn = DB.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT id FROM villas WHERE id = ?")) {
            check.setInt(1, villaId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) throw new NotFoundException("Villa with ID " + villaId + " not found.");

            PreparedStatement update = conn.prepareStatement(
                    "UPDATE villas SET name = ?, description = ?, address = ? WHERE id = ?");
            update.setString(1, villa.name);
            update.setString(2, villa.description);
            update.setString(3, villa.address);
            update.setInt(4, villaId);
            update.executeUpdate();

            villa.id = villaId;
            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(villa));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Update error: " + e.getMessage());
        }
    }

    public static void deleteVilla(Response res, int villaId) {
        try (Connection conn = DB.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT id FROM villas WHERE id = ?")) {
            check.setInt(1, villaId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) throw new NotFoundException("Villa with ID " + villaId + " not found.");

            PreparedStatement del = conn.prepareStatement("DELETE FROM villas WHERE id = ?");
            del.setInt(1, villaId);
            del.executeUpdate();

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody("{\"message\":\"Villa deleted successfully\"}");
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Delete error: " + e.getMessage());
        }
    }

    public static void getRooms(Response res, int villaId) {
        List<RoomType> rooms = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM room_types WHERE villa = " + villaId)) {

            while (rs.next()) {
                RoomType room = new RoomType();
                room.id = rs.getInt("id");
                room.villa = rs.getInt("villa");
                room.name = rs.getString("name");
                room.quantity = rs.getInt("quantity");
                room.capacity = rs.getInt("capacity");
                room.price = rs.getInt("price");
                room.bed_size = rs.getString("bed_size");
                room.has_desk = rs.getInt("has_desk") == 1;
                room.has_ac = rs.getInt("has_ac") == 1;
                room.has_tv = rs.getInt("has_tv") == 1;
                room.has_wifi = rs.getInt("has_wifi") == 1;
                room.has_shower = rs.getInt("has_shower") == 1;
                room.has_hotwater = rs.getInt("has_hotwater") == 1;
                room.has_fridge = rs.getInt("has_fridge") == 1;

                rooms.add(room);
            }

            if (rooms.isEmpty()) {
                throw new NotFoundException("No rooms found for villa ID " + villaId);
            }

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(rooms));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Database error: " + e.getMessage());
        }
    }


    public static void createRoom(Request req, Response res, int villaId) {
        RoomType room = JsonUtil.fromJson(req.getBody(), RoomType.class);

        if (room == null ||
                Validator.isEmpty(room.name) ||
                room.price <= 0 ||
                Validator.isEmpty(room.bed_size) ||
                (!room.bed_size.equals("double") && !room.bed_size.equals("queen") && !room.bed_size.equals("king"))) {
            throw new BadRequestException("Invalid room data. Required: name, price > 0, bed_size: double/queen/king");
        }

        try (Connection conn = DB.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO room_types (villa, name, bed_size, price) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, villaId);
            stmt.setString(2, room.name);
            stmt.setString(3, room.bed_size);
            stmt.setInt(4, room.price);
            stmt.executeUpdate();

            // Ambil ID terakhir
            Statement getIdStmt = conn.createStatement();
            ResultSet rs = getIdStmt.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                room.id = rs.getInt(1);
            }

            room.villa = villaId;

            res.setStatus(201);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(room));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Insert error: " + e.getMessage());
        }
    }

        public static void updateRoom(Request req, Response res, int villaId, int roomId) {
        RoomType room = JsonUtil.fromJson(req.getBody(), RoomType.class);

        if (room == null ||
                Validator.isEmpty(room.name) ||
                room.price <= 0 ||
                Validator.isEmpty(room.bed_size)) {
            throw new BadRequestException("Invalid data. Required fields missing or invalid.");
        }

        try (Connection conn = DB.getConnection()) {
            PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM room_types WHERE id = ? AND villa = ?");
            check.setInt(1, roomId);
            check.setInt(2, villaId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) throw new NotFoundException("Room not found for this villa.");

            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE room_types SET name = ?, quantity = ?, capacity = ?, price = ?, bed_size = ?, " +
                            "has_desk = ?, has_ac = ?, has_tv = ?, has_wifi = ?, has_shower = ?, has_hotwater = ?, has_fridge = ? " +
                            "WHERE id = ?");

            stmt.setString(1, room.name);
            stmt.setInt(2, room.quantity);
            stmt.setInt(3, room.capacity);
            stmt.setInt(4, room.price);
            stmt.setString(5, room.bed_size);
            stmt.setInt(6, room.has_desk ? 1 : 0);
            stmt.setInt(7, room.has_ac ? 1 : 0);
            stmt.setInt(8, room.has_tv ? 1 : 0);
            stmt.setInt(9, room.has_wifi ? 1 : 0);
            stmt.setInt(10, room.has_shower ? 1 : 0);
            stmt.setInt(11, room.has_hotwater ? 1 : 0);
            stmt.setInt(12, room.has_fridge ? 1 : 0);
            stmt.setInt(13, roomId);

            stmt.executeUpdate();

            room.id = roomId;
            room.villa = villaId;

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(room));
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Update error: " + e.getMessage());
        }
    }

    public static void deleteRoom(Response res, int villaId, int roomId) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM room_types WHERE id = ? AND villa = ?");
            check.setInt(1, roomId);
            check.setInt(2, villaId);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) throw new NotFoundException("Room not found.");

            PreparedStatement del = conn.prepareStatement("DELETE FROM room_types WHERE id = ?");
            del.setInt(1, roomId);
            del.executeUpdate();

            res.setStatus(200);
            res.setHeader("Content-Type", "application/json");
            res.setBody("{\"message\": \"Room deleted successfully\"}");
            res.send();

        } catch (Exception e) {
            throw new ApiException(500, "Delete error: " + e.getMessage());
        }
    }
}

