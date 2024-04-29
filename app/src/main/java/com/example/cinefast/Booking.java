package com.example.cinefast;

public class Booking {
    private String bookingId;
    private String userId;
    private String movieName;
    private String seats;
    private double totalPrice;
    private String dateTime;
    private long timestamp;

    public Booking() {
        // Default constructor for Firebase
    }

    public Booking(String bookingId, String userId, String movieName, String seats, double totalPrice, String dateTime, long timestamp) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieName = movieName;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.timestamp = timestamp;
    }

    // Getters
    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getMovieName() { return movieName; }
    public String getSeats() { return seats; }
    public double getTotalPrice() { return totalPrice; }
    public String getDateTime() { return dateTime; }
    public long getTimestamp() { return timestamp; }

    // Setters (Required for Firebase to populate data)
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public void setSeats(String seats) { this.seats = seats; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
