package entities;

import java.time.LocalDate;

public class HoaDonVietNam extends HoaDonEntity {
    public enum DoiTuong { SINH_HOAT, KINH_DOANH, SAN_XUAT }

    private DoiTuong doiTuong;
    private double dinhMuc;

    public HoaDonVietNam(String maKH, String hoTen, LocalDate ngay, double soLuong, double donGia, DoiTuong doiTuong, double dinhMuc) {
        super(maKH, hoTen, ngay, soLuong, donGia);
        this.doiTuong = doiTuong;
        this.dinhMuc = dinhMuc;
    }

    public DoiTuong getDoiTuong() { return doiTuong; }
    public double getDinhMuc() { return dinhMuc; }
    public void setDoiTuong(DoiTuong d) { this.doiTuong = d; }
    public void setDinhMuc(double dm) { this.dinhMuc = dm; }

    @Override
    public double tinhThanhTien() {
        double sl = getSoLuong();
        double dg = getDonGia();
        if (sl <= dinhMuc) {
            return sl * dg;
        } else {
            double vuot = sl - dinhMuc;
            return (dinhMuc * dg) + (vuot * dg * 2.5);
        }
    }

    @Override
    public String getLoai() { return "VN"; }
}
