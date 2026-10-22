package com.hotel.model;

public class Room {

    public String roomNumber, type, status, customerName, customerPhone;
    public double pricePerNight;
    public int numberOfNights;

    public Room(String roomNumber, String type, double pricePerNight) {
        this(roomNumber, type, pricePerNight, "Available", "", "", 0);
    }

    public Room(String roomNumber, String type, double pricePerNight,
                String status, String customerName, String customerPhone, int numberOfNights) {
        this.roomNumber     = roomNumber;
        this.type           = type;
        this.pricePerNight  = pricePerNight;
        this.status         = status;
        this.customerName   = customerName;
        this.customerPhone  = customerPhone;
        this.numberOfNights = numberOfNights;
    }

    public String toCSV() {
        return roomNumber + "," + type + "," + pricePerNight + "," +
               status + "," + customerName + "," + customerPhone + "," + numberOfNights;
    }

    public String toJSON() {
        return "{"
            + "\"roomNumber\":\""    + roomNumber    + "\","
            + "\"type\":\""         + type          + "\","
            + "\"pricePerNight\":"  + pricePerNight  + ","
            + "\"status\":\""       + status        + "\","
            + "\"customerName\":\"" + customerName  + "\","
            + "\"customerPhone\":\"" + customerPhone + "\","
            + "\"numberOfNights\":" + numberOfNights + ","
            + "\"totalBill\":"      + (pricePerNight * numberOfNights)
            + "}";
    }
}
