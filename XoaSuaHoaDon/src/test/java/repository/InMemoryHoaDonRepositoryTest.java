package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import entities.HoaDonEntity;
import entities.HoaDonVietNam;
import entities.HoaDonVietNam.DoiTuong;
import entities.HoaDonNuocNgoai;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHoaDonRepositoryTest {

    private InMemoryHoaDonRepository repository;
    private final int INITIAL_SIZE = 4; 

    @BeforeEach
    void setUp() {
        repository = new InMemoryHoaDonRepository();
    }


    @Test
    void getAll_ShouldReturnInitialSampleData() {
        List<HoaDonEntity> all = repository.getAll();
        
        assertEquals(INITIAL_SIZE, all.size(), "Repository phải có 4 hóa đơn ban đầu.");
        

        all.add(new HoaDonVietNam("TEST001", "Temp", LocalDate.now(), 10, 10, DoiTuong.SINH_HOAT, 1));
        assertEquals(INITIAL_SIZE, repository.getAll().size(), "List trả về phải là bản sao, không ảnh hưởng đến nội bộ.");
    }
    

    @Test
    void findByMaKH_ShouldReturnExistingHoaDon() {
        String maKH = "VN002";
        HoaDonEntity found = repository.findByMaKH(maKH);
        
        assertNotNull(found, "Phải tìm thấy hóa đơn VN002.");
        assertEquals("Tran Thi B", found.getHoTen());
    }

    @Test
    void findByMaKH_ShouldReturnNullForNonExistingMaKH() {
        HoaDonEntity found = repository.findByMaKH("KHOI_TON_TAI");
        assertNull(found, "Phải trả về null nếu Mã KH không tồn tại.");
    }



    @Test
    void delete_ShouldRemoveExistingHoaDon() {
        String maKHToDelete = "NN001";
        
        repository.delete(maKHToDelete);
        
        assertEquals(INITIAL_SIZE - 1, repository.getAll().size(), "Số lượng phải giảm đi 1.");
        
        assertNull(repository.findByMaKH(maKHToDelete), "Hóa đơn NN001 phải bị xóa.");
    }

    @Test
    void delete_ShouldDoNothingIfMaKHDoesNotExist() {
        repository.delete("NON_EXISTENT");
        
        assertEquals(INITIAL_SIZE, repository.getAll().size(), "Xóa Mã KH không tồn tại không làm thay đổi số lượng.");
    }
    

    
    
    @Test
    void update_ShouldModifyExistingHoaDon() {
        String maKHToUpdate = "VN001";
        HoaDonEntity original = repository.findByMaKH(maKHToUpdate);
        
        HoaDonVietNam updatedHD = new HoaDonVietNam(
            original.getMaKH(), 
            "Nguyen Van Z", // Tên mới
            original.getNgayRaHoaDon(), 
            original.getSoLuong(), 
            original.getDonGia(), 
            DoiTuong.SAN_XUAT, // Đối tượng mới
            150 // Định mức mới
        );
        
        repository.update(updatedHD);
        
        HoaDonVietNam found = (HoaDonVietNam) repository.findByMaKH(maKHToUpdate);
        
        assertEquals(INITIAL_SIZE, repository.getAll().size(), "Update không được thay đổi tổng số lượng.");
        
        assertEquals("Nguyen Van Z", found.getHoTen(), "Tên phải được cập nhật.");
        assertEquals(DoiTuong.SAN_XUAT, found.getDoiTuong(), "Đối tượng phải được cập nhật.");
        assertEquals(150, found.getDinhMuc(), 0.001, "Định mức phải được cập nhật.");
    }

    @Test
    void update_ShouldAddNewHoaDonIfMaKHNotFound() {
        String newMaKH = "NEW_ADD";
        HoaDonEntity newHD = new HoaDonNuocNgoai(newMaKH, "New Add via Update", LocalDate.now(), 1, 1, "FRA");
        
        repository.update(newHD); // update sẽ thêm mới nếu không tìm thấy
        
        assertEquals(INITIAL_SIZE + 1, repository.getAll().size(), "Update không tìm thấy phải thêm mới.");
        
        assertNotNull(repository.findByMaKH(newMaKH), "Hóa đơn mới phải tồn tại.");
    }
}