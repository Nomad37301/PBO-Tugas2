package server;

import app.Main;
import controller.VillaController;
import controller.CustomerController;
import controller.VoucherController;
import controller.ReviewController;
import exception.ApiException;
import exception.NotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import util.JsonUtil;

public class Server {
    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (Exception e) {
                        System.err.println("Error handling client: " + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) throws Exception {
        Request req = new Request(socket.getInputStream());
        Response res = new Response(socket.getOutputStream());

        System.out.println(req.getMethod() + " " + req.getPath());

        try {
            // Middleware: API key validation
            String apiKey = req.getHeader("x-api-key");
            if (!Main.API_KEY.equals(apiKey)) {
                res.setStatus(401);
                res.setHeader("Content-Type", "application/json");
                res.setBody("{\"error\": \"Unauthorized\"}");
                res.send();
                return;
            }

            // Route the request
            route(req, res);

        } catch (ApiException e) {
            res.setStatus(e.getStatus());
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(new ErrorResponse(e.getMessage())));
            res.send();

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(new ErrorResponse("Internal Server Error")));
            res.send();
        }
    }

    private void route(Request req, Response res) {
        String method = req.getMethod();
        String path = req.getPath();

        // --- rute villa ---
        if (method.equals("GET") && path.equals("/villas")) {
            VillaController.getAll(res);
        } else if (method.equals("GET") && path.matches("^/villas/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            VillaController.getDetail(res, id);
        } else if (method.equals("POST") && path.equals("/villas")) {
            VillaController.createVilla(req, res);
        } else if (method.equals("PUT") && path.matches("^/villas/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            VillaController.updateVilla(req, res, id);
        } else if (method.equals("DELETE") && path.matches("^/villas/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            VillaController.deleteVilla(res, id);
        }

        // --- rute roomtype ---
        else if (method.equals("GET") && path.matches("^/villas/\\d+/rooms$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            VillaController.getRooms(res, id);
        } else if (method.equals("POST") && path.matches("^/villas/\\d+/rooms$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            VillaController.createRoom(req, res, id);
        } else if (method.equals("PUT") && path.matches("^/villas/\\d+/rooms/\\d+$")) {
            String[] parts = path.split("/");
            int villaId = Integer.parseInt(parts[2]);
            int roomId = Integer.parseInt(parts[4]);
            VillaController.updateRoom(req, res, villaId, roomId);
        } else if (method.equals("DELETE") && path.matches("^/villas/\\d+/rooms/\\d+$")) {
            String[] parts = path.split("/");
            int villaId = Integer.parseInt(parts[2]);
            int roomId = Integer.parseInt(parts[4]);
            VillaController.deleteRoom(res, villaId, roomId);
        }

        // --- rute customer ---
        else if (method.equals("GET") && path.equals("/customers")) {
            CustomerController.getAll(res);
        }
        else if (method.equals("GET") && path.matches("^/customers/\\d+/reviews$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.getReviews(res, id);
        } else if (method.equals("GET") && path.matches("^/customers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.getDetail(res, id);
        } else if (method.equals("POST") && path.equals("/customers")) {
            CustomerController.createCustomer(req, res);
        } else if (method.equals("PUT") && path.matches("^/customers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.updateCustomer(req, res, id);
        } else if (method.equals("DELETE") && path.matches("^/customers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.deleteCustomer(res, id);
        }
            
        // --- rute voucher ---
        else if (method.equals("GET") && path.equals("/vouchers")) {
            res.setHeader("Content-Type", "application/json");
            res.setBody(VoucherController.getAllVouchers());
            res.send();
        } else if (method.equals("GET") && path.matches("^/vouchers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(VoucherController.getVoucherById(id)));
            res.send();
        } else if (method.equals("POST") && path.equals("/vouchers")) {
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(VoucherController.createVoucher(req.getBody())));
            res.send();
        } else if (method.equals("PUT") && path.matches("^/vouchers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(VoucherController.updateVoucher(id, req.getBody())));
            res.send();
        } else if (method.equals("DELETE") && path.matches("^/vouchers/\\d+$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            res.setHeader("Content-Type", "application/json");
            res.setBody(JsonUtil.toJson(VoucherController.deleteVoucher(id)));
            res.send();
        }

        // --- GET /villas/{id}/bookings ---
        else if (method.equals("GET") && path.matches("^/villas/\\d+/bookings$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            VillaController.getBookings(res, id);
        }

        // --- GET /villas?ci_date=...&co_date=... ---
        else if (method.equals("GET") && path.startsWith("/villas") &&
                JsonUtil.getQueryParam(path, "ci_date") != null &&
                JsonUtil.getQueryParam(path, "co_date") != null) {
            VillaController.checkAvailability(req, res);
        }

        // --- GET /customers/{id}/bookings ---
        else if (method.equals("GET") && path.matches("^/customers/\\d+/bookings$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.getBookings(res, id);
        }

        // --- POST /customers/{id}/bookings ---
        else if (method.equals("POST") && path.matches("^/customers/\\d+/bookings$")) {
            int id = Integer.parseInt(path.split("/")[2]);
            CustomerController.createBooking(req, res, id);
        }

        // Rute review controller
        else if (method.equals("POST") && path.matches("^/customers/\\d+/bookings/\\d+/reviews$")) {
            String[] parts = path.split("/");
            int customerId = Integer.parseInt(parts[2]);
            int bookingId = Integer.parseInt(parts[4]);
            ReviewController.createReview(req, res, customerId, bookingId);
        }


        // --- rute Unknown  ---
        else {
            throw new NotFoundException("Endpoint not found: " + method + " " + path);
        }
    }

    // Simple error wrapper class
    static class ErrorResponse {
        public String error;

        public ErrorResponse(String message) {
            this.error = message;
        }
    }
}
