package model;

public class Bookings {
    public int id;
    public int customer;
    public int room_type;
    public String checkin_date; // sesuaikan tipe datanya / ni tipe data masih belum tentu
    public String checkout_date; // sesuaikan tipe datanya yaw / sama aja kek di atas
    public int price;
    public int final_price;
    public String payment_status;
    public int has_checkin = 0;
    public int has_checkout = 0;

    public Bookings() {}
}
