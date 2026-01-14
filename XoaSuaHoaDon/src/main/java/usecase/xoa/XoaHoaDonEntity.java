package usecase.xoa;

import repository.HoaDonRepository;
import entities.HoaDonEntity; 

public class XoaHoaDonEntity { 

    private final HoaDonRepository repo; 

    public XoaHoaDonEntity(HoaDonRepository repo) { 
        this.repo = repo; 
    }

    public XoaHoaDonOutputData execute(XoaHoaDonInputData input) {
        XoaHoaDonOutputData out = new XoaHoaDonOutputData();

        if (input.maKH == null || input.maKH.isBlank()) {
            out.success = false;
            out.message = "Mã khách hàng không hợp lệ.";
            return out;
        }

        HoaDonEntity found = repo.findByMaKH(input.maKH); 

        if (found == null) {
            out.success = false;
            out.message = "Không tìm thấy hóa đơn với mã: " + input.maKH;
        } else {
            repo.delete(input.maKH); 
            out.success = true;
            out.message = "Xóa hóa đơn thành công.";
        }
        
        return out;
    }
}