package repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import entities.HoaDonEntity;
import entities.HoaDonVietNam;
import entities.HoaDonVietNam.DoiTuong;
import entities.HoaDonNuocNgoai;

public class InMemoryHoaDonRepository implements HoaDonRepository {
    private final List<HoaDonEntity> db = new ArrayList<>();

    public InMemoryHoaDonRepository() {
        // dữ liệu mẫu: 2 VN, 2 NN
        db.add(new HoaDonVietNam("VN001", "Nguyen Van A", LocalDate.of(2025, 1, 12), 120, 2000, DoiTuong.SINH_HOAT, 100));
        db.add(new HoaDonVietNam("VN002", "Tran Thi B", LocalDate.of(2025, 2, 20), 80, 1500, DoiTuong.KINH_DOANH, 100));
        db.add(new HoaDonNuocNgoai("NN001", "John Doe", LocalDate.of(2025, 3, 5), 200, 3000, "USA"));
        db.add(new HoaDonNuocNgoai("NN002", "Anna Smith", LocalDate.of(2025, 4, 10), 50, 2500, "UK"));
    }

    @Override
    public List<HoaDonEntity> getAll() {
        return new ArrayList<>(db);
    }

    @Override
    public HoaDonEntity findByMaKH(String maKH) {
        return db.stream().filter(h -> h.getMaKH().equals(maKH)).findFirst().orElse(null);
    }

    @Override
    public void update(HoaDonEntity hoaDon) {
        for (int i = 0; i < db.size(); i++) {
            if (db.get(i).getMaKH().equals(hoaDon.getMaKH())) {
                db.set(i, hoaDon);
                return;
            }
        }
        // nếu không tồn tại, thêm mới
        db.add(hoaDon);
    }

    @Override
    public void delete(String maKH) {
        db.removeIf(h -> h.getMaKH().equals(maKH));
    }

    @Override
    public void add(HoaDonEntity hoaDon) {
        db.add(hoaDon);
    }
}
