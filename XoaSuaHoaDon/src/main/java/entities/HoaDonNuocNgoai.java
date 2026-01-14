package entities;

import java.time.LocalDate;

public class HoaDonNuocNgoai extends HoaDonEntity {
    private String quocTich;

    public HoaDonNuocNgoai(String maKH, String hoTen, LocalDate ngay, double soLuong, double donGia, String quocTich) {
        super(maKH, hoTen, ngay, soLuong, donGia);
        this.quocTich = quocTich;
    }

    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String q) { this.quocTich = q; }

    @Override
    public double tinhThanhTien() {
        return getSoLuong() * getDonGia();
    }

    @Override
    public String getLoai() { return "NN"; }
}
