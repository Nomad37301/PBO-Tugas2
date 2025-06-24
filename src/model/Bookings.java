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

    public Bookings(int id, int customer, int room_type, String checkin_date, String checkout_date) {
        this.id = id;
        this.customer = customer;
        this.room_type = room_type;
        this.checkin_date = checkin_date;
        this.checkout_date = checkout_date;
    }

}
