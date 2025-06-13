package model;

public class Vouchers {
    public  int id;
    public String code;
    public String description;
    public float discount;
    public String start_date; // Tipe data sesuaikan nanti
    public String end_date; // Tipe data sesuaikan nanti

    public Vouchers() {}

    public Vouchers(int id, String code, String description, float discount, String start_date, String end_date) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discount = discount;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}
