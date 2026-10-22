package com.hotel.util;

import com.hotel.model.Room;
import java.sql.*;
import java.util.ArrayList;

public class DBManager {

    private static final String DB_SERVER_URL = "jdbc:mysql://127.0.0.1:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hotel_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String DB_PASS = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "root123";

    private static boolean initialized = false;

    public static synchronized void ensureDatabaseExists() {
        if (initialized)
            return;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DB_SERVER_URL, DB_USER, DB_PASS);
                    Statement stmt = con.createStatement()) {
                stmt.executeUpdate(
                        "CREATE DATABASE IF NOT EXISTS hotel_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            }

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                    Statement stmt = con.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS rooms ("
                        + "  room_number     VARCHAR(20)    NOT NULL PRIMARY KEY,"
                        + "  type            VARCHAR(20)    NOT NULL DEFAULT 'Single',"
                        + "  price_per_night DECIMAL(10,2)  NOT NULL,"
                        + "  status          VARCHAR(20)    NOT NULL DEFAULT 'Available',"
                        + "  customer_name   VARCHAR(100)   DEFAULT '',"
                        + "  customer_phone  VARCHAR(20)    DEFAULT '',"
                        + "  number_of_nights INT           DEFAULT 0"
                        + ")";
                stmt.executeUpdate(sql);
            }
            initialized = true;
        } catch (Exception e) {
            System.err.println("[DBManager] Database auto-init warning: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        ensureDatabaseExists();
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static ArrayList<Room> loadRooms() throws SQLException {
        ArrayList<Room> rooms = new ArrayList<>();
        String sql = "SELECT room_number, type, price_per_night, status, customer_name, customer_phone, number_of_nights FROM rooms";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price_per_night"),
                        rs.getString("status"),
                        rs.getString("customer_name"),
                        rs.getString("customer_phone"),
                        rs.getInt("number_of_nights")));
            }
        }
        return rooms;
    }

    public static void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, type, price_per_night, status, customer_name, customer_phone, number_of_nights) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, room.roomNumber);
            ps.setString(2, room.type);
            ps.setDouble(3, room.pricePerNight);
            ps.setString(4, room.status);
            ps.setString(5, room.customerName);
            ps.setString(6, room.customerPhone);
            ps.setInt(7, room.numberOfNights);
            ps.executeUpdate();
        }
    }

    public static void bookRoom(String roomNumber, String customerName, String customerPhone, int nights)
            throws SQLException {
        String sql = "UPDATE rooms SET status='Booked', customer_name=?, customer_phone=?, number_of_nights=? WHERE room_number=?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customerName);
            ps.setString(2, customerPhone);
            ps.setInt(3, nights);
            ps.setString(4, roomNumber);
            ps.executeUpdate();
        }
    }

    public static void checkoutRoom(String roomNumber) throws SQLException {
        String sql = "UPDATE rooms SET status='Available', customer_name='', customer_phone='', number_of_nights=0 WHERE room_number=?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ps.executeUpdate();
        }
    }

    public static Room findRoom(String roomNumber) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE room_number=?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Room(
                            rs.getString("room_number"),
                            rs.getString("type"),
                            rs.getDouble("price_per_night"),
                            rs.getString("status"),
                            rs.getString("customer_name"),
                            rs.getString("customer_phone"),
                            rs.getInt("number_of_nights"));
                }
            }
        }
        return null;
    }

    public static boolean roomExists(String roomNumber) throws SQLException {
        return findRoom(roomNumber) != null;
    }
}
