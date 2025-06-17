package model;

public class Bookings {
    public int id;
    public int customer;
    public int room_type;
    public String checkin_date; // sesuaikan tipe datanya / ni tipe data masih belum tentu
    public String checkout_date; // sesuaikan tipe datanya yaw / sama aja kek di atas
    public int price;
    public int voucher;
    public int final_price;
    public String payment_status;
    public boolean has_checkin;
    public boolean has_checkout;

    public Bookings() {}
}
