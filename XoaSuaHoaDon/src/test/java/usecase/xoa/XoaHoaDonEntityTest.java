package usecase.xoa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import repository.HoaDonRepository;
import entities.HoaDonEntity; 
import entities.HoaDonVietNam;
import entities.HoaDonVietNam.DoiTuong;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class XoaHoaDonEntityTest {

    @Mock
    private HoaDonRepository mockRepo;

    private XoaHoaDonEntity xoaHoaDonEntity;

    private final String EXISTING_MA_KH = "KH007";
    private final String NON_EXISTING_MA_KH = "KH404";
    private HoaDonEntity dummyHoaDon;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        xoaHoaDonEntity = new XoaHoaDonEntity(mockRepo);
        
        dummyHoaDon = new HoaDonVietNam(
            EXISTING_MA_KH, 
            "James Bond", 
            LocalDate.now(), 
            100, 2000, 
            DoiTuong.KINH_DOANH, 
            50
        );
    }
    
    // Input không hợp lệ
    @Test
    void execute_ShouldReturnFailureIfMaKHIsNull() {
        XoaHoaDonInputData input = new XoaHoaDonInputData(); // maKH mặc định là null

        XoaHoaDonOutputData output = xoaHoaDonEntity.execute(input);

        assertFalse(output.success, "Phải thất bại khi Mã KH là null.");
        assertEquals("Mã khách hàng không hợp lệ.", output.message);
        
        verifyNoInteractions(mockRepo); 
    }

    
    @Test
    void execute_ShouldReturnFailureIfMaKHIsBlank() {
        XoaHoaDonInputData input = new XoaHoaDonInputData();
        input.maKH = "   "; // Chỉ chứa khoảng trắng

        XoaHoaDonOutputData output = xoaHoaDonEntity.execute(input);

        assertFalse(output.success, "Phải thất bại khi Mã KH chỉ chứa khoảng trắng.");
        assertEquals("Mã khách hàng không hợp lệ.", output.message);
        verifyNoInteractions(mockRepo);
    }

    //Xóa thành công

    @Test
    void execute_ShouldReturnSuccessAndDeleteHoaDonWhenFound() {
        XoaHoaDonInputData input = new XoaHoaDonInputData();
        input.maKH = EXISTING_MA_KH;
        
        when(mockRepo.findByMaKH(EXISTING_MA_KH)).thenReturn(dummyHoaDon);

        XoaHoaDonOutputData output = xoaHoaDonEntity.execute(input);

        assertTrue(output.success, "Xóa phải thành công.");
        assertEquals("Xóa hóa đơn thành công.", output.message);
        
        verify(mockRepo, times(1)).findByMaKH(EXISTING_MA_KH);
        
        verify(mockRepo, times(1)).delete(EXISTING_MA_KH);
    }

    //Xóa thất bại (Không tìm thấy)

    @Test
    void execute_ShouldReturnFailureAndNotDeleteWhenNotFound() {
        XoaHoaDonInputData input = new XoaHoaDonInputData();
        input.maKH = NON_EXISTING_MA_KH;
        
        when(mockRepo.findByMaKH(NON_EXISTING_MA_KH)).thenReturn(null);

        XoaHoaDonOutputData output = xoaHoaDonEntity.execute(input);

        assertFalse(output.success, "Phải thất bại vì không tìm thấy hóa đơn.");
        assertTrue(output.message.contains(NON_EXISTING_MA_KH), "Thông báo phải chứa Mã KH không tìm thấy.");
        
        verify(mockRepo, times(1)).findByMaKH(NON_EXISTING_MA_KH);
        
        verify(mockRepo, never()).delete(anyString());
    }
}