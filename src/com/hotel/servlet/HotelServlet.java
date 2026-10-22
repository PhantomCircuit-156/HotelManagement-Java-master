package com.hotel.servlet;

import com.hotel.model.Room;
import com.hotel.util.DBManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/*")
public class HotelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if ("/rooms".equals(req.getPathInfo())) {
            try {
                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                for (Room r : DBManager.loadRooms()) {
                    if (!first) json.append(",");
                    json.append(r.toJSON());
                    first = false;
                }
                send(resp, 200, json.append("]").toString());
            } catch (SQLException e) {
                send(resp, 500, err("Database error: " + e.getMessage()));
            }
        } else {
            send(resp, 404, err("Unknown endpoint"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String> p = params(body(req));
        switch (req.getPathInfo() == null ? "" : req.getPathInfo()) {
            case "/add":      add(p, resp);      break;
            case "/book":     book(p, resp);     break;
            case "/checkout": checkout(p, resp); break;
            default:          send(resp, 404, err("Unknown endpoint"));
        }
    }

    private void add(Map<String, String> p, HttpServletResponse resp) throws IOException {
        String num      = p.getOrDefault("roomNumber", "").trim();
        String type     = p.getOrDefault("type", "Single").trim();
        String priceStr = p.getOrDefault("price", "0").trim();

        if (num.isEmpty()) { send(resp, 400, err("Room number is required")); return; }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price <= 0) { send(resp, 400, err("Price must be greater than 0")); return; }
        } catch (NumberFormatException e) { send(resp, 400, err("Invalid price value")); return; }

        try {
            if (DBManager.roomExists(num)) {
                send(resp, 400, err("Room " + num + " already exists")); return;
            }
            DBManager.addRoom(new Room(num, type, price));
            send(resp, 200, ok("Room " + num + " added successfully"));
        } catch (SQLException e) {
            send(resp, 500, err("Database error: " + e.getMessage()));
        }
    }

    private void book(Map<String, String> p, HttpServletResponse resp) throws IOException {
        String num   = p.getOrDefault("roomNumber", "").trim();
        String name  = p.getOrDefault("customerName", "").trim();
        String phone = p.getOrDefault("customerPhone", "").trim();

        if (num.isEmpty() || name.isEmpty()) {
            send(resp, 400, err("Room number and customer name are required")); return;
        }

        int nights = 1;
        try { nights = Math.max(1, Integer.parseInt(p.getOrDefault("nights", "1").trim())); }
        catch (NumberFormatException ignored) {}

        try {
            Room r = DBManager.findRoom(num);
            if (r == null)                  { send(resp, 404, err("Room " + num + " not found")); return; }
            if (r.status.equals("Booked"))  { send(resp, 400, err("Room " + num + " is already booked")); return; }

            DBManager.bookRoom(num, name, phone, nights);
            send(resp, 200, ok("Room " + num + " booked for " + name));
        } catch (SQLException e) {
            send(resp, 500, err("Database error: " + e.getMessage()));
        }
    }

    private void checkout(Map<String, String> p, HttpServletResponse resp) throws IOException {
        String num = p.getOrDefault("roomNumber", "").trim();
        if (num.isEmpty()) { send(resp, 400, err("Room number is required")); return; }

        try {
            Room r = DBManager.findRoom(num);
            if (r == null)                      { send(resp, 404, err("Room " + num + " not found")); return; }
            if (r.status.equals("Available"))   { send(resp, 400, err("Room " + num + " is not currently booked")); return; }

            double total = r.pricePerNight * r.numberOfNights;
            String bill  = "{\"success\":\"Checkout successful\","
                + "\"customerName\":\"" + r.customerName   + "\","
                + "\"roomNumber\":\""   + r.roomNumber     + "\","
                + "\"nights\":"         + r.numberOfNights + ","
                + "\"pricePerNight\":"  + r.pricePerNight  + ","
                + "\"totalBill\":"      + total + "}";

            DBManager.checkoutRoom(num);
            send(resp, 200, bill);
        } catch (SQLException e) {
            send(resp, 500, err("Database error: " + e.getMessage()));
        }
    }

    private static void send(HttpServletResponse resp, int status, String json) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.getWriter().write(json);
    }

    private static String body(HttpServletRequest req) throws IOException {
        return new String(req.getInputStream().readAllBytes(), "UTF-8");
    }

    private static Map<String, String> params(String data) {
        Map<String, String> map = new LinkedHashMap<>();
        if (data == null || data.isBlank()) return map;
        for (String pair : data.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                try { map.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8")); }
                catch (Exception e) { map.put(kv[0], kv[1]); }
            }
        }
        return map;
    }

    private static String err(String msg) { return "{\"error\":\""   + msg.replace("\"", "'") + "\"}"; }
    private static String ok(String msg)  { return "{\"success\":\"" + msg.replace("\"", "'") + "\"}"; }
}
