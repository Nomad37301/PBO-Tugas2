package controller;

import model.Vouchers;
import util.JsonUtil;

import java.util.*;

public class VoucherController {
    private static final List<Vouchers> vouchers = new ArrayList<>();
    private static int idCounter = 1;

    public static String getAllVouchers() {
        return JsonUtil.toJson(vouchers);
    }

    public static Vouchers getVoucherById(int id) {
        for (Vouchers v : vouchers) {
            if (v.id == id) {
                return v;
            }
        }
        return null;
    }

    public static String createVoucher(String body) {
        Map<String, String> params = parseParams(body);
        Vouchers newVoucher = new Vouchers();
        newVoucher.id = idCounter++;
        newVoucher.name = params.get("name");
        newVoucher.discount = Integer.parseInt(params.get("discount"));
        vouchers.add(newVoucher);
        return "Voucher berhasil ditambahkan";
    }

    public static String updateVoucher(int id, String body) {
        Map<String, String> params = parseParams(body);
        for (Vouchers v : vouchers) {
            if (v.id == id) {
                v.name = params.get("name");
                v.discount = Integer.parseInt(params.get("discount"));
                return "Voucher berhasil diupdate";
            }
        }
        return "Voucher tidak ditemukan";
    }

    public static String deleteVoucher(int id) {
        Iterator<Vouchers> it = vouchers.iterator();
        while (it.hasNext()) {
            Vouchers v = it.next();
            if (v.id == id) {
                it.remove();
                return "Voucher berhasil dihapus";
            }
        }
        return "Voucher tidak ditemukan";
    }

    private static Map<String, String> parseParams(String body) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }
}
