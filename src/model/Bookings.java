package model;

public class Bookings {
    public int id;
    public int customer;
    public int room_type;
    public String checkin_date; // sesuaikan tipe datanya yaw
    public String checkout_date; // sesuaikan tipe datanya yaw
    public int price;
    public int final_price;
    public String payment_status;
    public int has_checkin = 0;
    public int has_checkout = 0;

    public Bookings() {}
}
