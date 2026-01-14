package entities;

import java.time.LocalDate;

public abstract class HoaDonEntity {
    protected String maKH;
    protected String hoTen;
    protected LocalDate ngayRaHoaDon;
    protected double soLuong;
    protected double donGia;

    public HoaDonEntity(String maKH, String hoTen, LocalDate ngayRaHoaDon, double soLuong, double donGia) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.ngayRaHoaDon = ngayRaHoaDon;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaKH() { return maKH; }
    public String getHoTen() { return hoTen; }
    public java.time.LocalDate getNgayRaHoaDon() { return ngayRaHoaDon; }
    public double getSoLuong() { return soLuong; }
    public double getDonGia() { return donGia; }

    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setNgayRaHoaDon(LocalDate ngay) { this.ngayRaHoaDon = ngay; }
    public void setSoLuong(double sl) { this.soLuong = sl; }
    public void setDonGia(double dg) { this.donGia = dg; }

    public abstract double tinhThanhTien();

    public abstract String getLoai(); // "VN" or "NN"

    @Override
    public String toString() {
        return String.format("%s - %s - %s", maKH, hoTen, ngayRaHoaDon);
    }
}
