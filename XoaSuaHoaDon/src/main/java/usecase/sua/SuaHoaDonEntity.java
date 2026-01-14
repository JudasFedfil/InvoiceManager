package usecase.sua;

import java.time.LocalDate;
import repository.HoaDonRepository;
import entities.*;

public class SuaHoaDonEntity {

    private final HoaDonRepository repo;

    public SuaHoaDonEntity(HoaDonRepository repo) {
        this.repo = repo;
    }

    public SuaHoaDonOutputData execute(SuaHoaDonInputData input) {
        SuaHoaDonOutputData out = new SuaHoaDonOutputData();

        if (input.maKH == null || input.maKH.isBlank()) {
            out.success = false;
            out.message = "Mã khách hàng không hợp lệ.";
            return out;
        }

        entities.HoaDonEntity entity;
        try {
            if ("VN".equalsIgnoreCase(input.loai)) {
                double dm = input.dinhMuc != null ? input.dinhMuc : 0;
                HoaDonVietNam.DoiTuong dt = HoaDonVietNam.DoiTuong.SINH_HOAT;
                if ("KINH_DOANH".equalsIgnoreCase(input.doiTuong)) dt = HoaDonVietNam.DoiTuong.KINH_DOANH;
                else if ("SAN_XUAT".equalsIgnoreCase(input.doiTuong)) dt = HoaDonVietNam.DoiTuong.SAN_XUAT;

                entity = new HoaDonVietNam(
                    input.maKH, 
                    input.hoTen, 
                    input.ngayRaHoaDon != null ? input.ngayRaHoaDon : LocalDate.now(),
                    input.soLuong != null ? input.soLuong : 0, 
                    input.donGia != null ? input.donGia : 0, 
                    dt, dm
                );
            } else {
                entity = new HoaDonNuocNgoai(
                    input.maKH, 
                    input.hoTen, 
                    input.ngayRaHoaDon != null ? input.ngayRaHoaDon : LocalDate.now(),
                    input.soLuong != null ? input.soLuong : 0, 
                    input.donGia != null ? input.donGia : 0, 
                    input.quocTich != null ? input.quocTich : ""
                );
            }
            
            repo.update(entity);
            out.success = true;
            out.message = "Sửa hóa đơn thành công.";

        } catch (Exception ex) {
            out.success = false;
            out.message = "Lỗi nghiệp vụ: " + ex.getMessage();
        }
        
        return out;
    }
}