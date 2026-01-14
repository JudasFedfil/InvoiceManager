package usecase.sua;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import repository.HoaDonRepository;
import entities.HoaDonEntity;
import entities.HoaDonVietNam;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuaHoaDonEntityTest {

    @Mock
    private HoaDonRepository mockRepo;

    private SuaHoaDonEntity suaHoaDonEntity;

    @BeforeEach
    void setUp() {
        suaHoaDonEntity = new SuaHoaDonEntity(mockRepo);
    }

    // TEST CASE 1: Mã KH không hợp lệ
    @Test
    void execute_WhenMaKHIsNull_ShouldReturnFailure() {
        SuaHoaDonInputData input = new SuaHoaDonInputData();
        input.maKH = null;

        SuaHoaDonOutputData output = suaHoaDonEntity.execute(input);

        assertFalse(output.success, "Output success phải là false");
        assertEquals("Mã khách hàng không hợp lệ.", output.message, "Thông báo lỗi phải chính xác");

        verify(mockRepo, never()).update(any(HoaDonEntity.class));
    }

    // TEST CASE 2: Sửa Hóa Đơn thành công
    @Test
    void execute_WhenValidVietNamInput_ShouldUpdateAndReturnSuccess() {
        LocalDate ngayHoaDon = LocalDate.of(2025, 1, 15);
        SuaHoaDonInputData input = new SuaHoaDonInputData();
        input.maKH = "KH001";
        input.hoTen = "Nguyễn Văn A";
        input.loai = "VN";
        input.doiTuong = "KINH_DOANH"; // Test ánh xạ đúng enum
        input.dinhMuc = 150.0;
        input.soLuong = 100.0;
        input.donGia = 2500.0;
        input.ngayRaHoaDon = ngayHoaDon;
        

        SuaHoaDonOutputData output = suaHoaDonEntity.execute(input);

        assertTrue(output.success, "Output success phải là true");
        assertEquals("Sửa hóa đơn thành công.", output.message, "Thông báo thành công phải chính xác");
        
        ArgumentCaptor<HoaDonEntity> entityCaptor = ArgumentCaptor.forClass(HoaDonEntity.class);
        verify(mockRepo, times(1)).update(entityCaptor.capture());
        
        HoaDonEntity capturedEntity = entityCaptor.getValue();
        
        assertTrue(capturedEntity instanceof HoaDonVietNam, "Phải tạo ra đối tượng HoaDonVietNam");
        HoaDonVietNam hdvN = (HoaDonVietNam) capturedEntity;
        
        assertEquals(input.maKH, hdvN.getMaKH(), "Mã KH phải khớp");
        assertEquals(input.hoTen, hdvN.getHoTen(), "Họ tên phải khớp");
        assertEquals(ngayHoaDon, hdvN.getNgayRaHoaDon(), "Ngày ra hóa đơn phải khớp");
        assertEquals(HoaDonVietNam.DoiTuong.KINH_DOANH, hdvN.getDoiTuong(), "Đối tượng phải là KINH_DOANH");
        assertEquals(150.0, hdvN.getDinhMuc(), 0.001, "Định mức phải khớp");
    }

    // TEST CASE 3: Xử lý ngoại lệ từ Repository
    @Test
    void execute_WhenRepoThrowsException_ShouldReturnFailureWithMessage() {
        SuaHoaDonInputData input = new SuaHoaDonInputData();
        input.maKH = "KH002";
        input.loai = "NN";
        input.quocTich = "Pháp";
        
        String errorMessage = "Lỗi kết nối cơ sở dữ liệu!";

        doThrow(new RuntimeException(errorMessage)).when(mockRepo).update(any(HoaDonEntity.class));

        SuaHoaDonOutputData output = suaHoaDonEntity.execute(input);

        assertFalse(output.success, "Output success phải là false");
        assertTrue(output.message.startsWith("Lỗi nghiệp vụ:"), "Thông báo phải là lỗi nghiệp vụ");
        assertTrue(output.message.contains(errorMessage), "Thông báo phải chứa message lỗi gốc");
        verify(mockRepo, times(1)).update(any(HoaDonEntity.class));
    }
}