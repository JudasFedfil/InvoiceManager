package repository;

import java.util.List;
import entities.HoaDonEntity;

public interface HoaDonRepository {
    List<HoaDonEntity> getAll();
    HoaDonEntity findByMaKH(String maKH);
    void update(HoaDonEntity hoaDon);
    void delete(String maKH);
    void add(HoaDonEntity hoaDon);
}
