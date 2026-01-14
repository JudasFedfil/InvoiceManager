package frameworks.forms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import frameworks.forms.SuaHoaDonForm;
import interfaceadapters.sua.SuaHoaDonController;
import interfaceadapters.sua.SuaHoaDonViewModel;
import usecase.sua.SuaHoaDonInputData;
import repository.HoaDonRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SuaHoaDonFormTest {

    @Mock
    private SuaHoaDonController mockController;
    
    @Mock
    private SuaHoaDonViewModel mockVm;
    
    @Mock 
    private HoaDonRepository mockRepo;
    
    private SuaHoaDonForm form;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        form = new SuaHoaDonForm(new Frame(), mockRepo) {
            @Override
            public void setVisible(boolean b) {

            }
        };
        form.controller = mockController;
        form.vm = mockVm;
        
        form.txtMa.setText("KH001");
        form.txtTen.setText("Nguyen Van A");
        form.txtNgay.setText("2023-11-10");
        form.cmbLoai.setSelectedItem("VN");
        form.cmbDoiTuong.setSelectedItem("SINH_HOAT");
        form.txtSoLuong.setText("150.5");
        form.txtDonGia.setText("1000.5");

        form.txtDinhMuc.setText("50.0");
        form.txtDinhMuc.setEnabled(true);
        form.txtQuocTich.setText("");
        form.txtQuocTich.setEnabled(false);
    }
    
    private void mockJOptionPane() {
        try {
            UIManager.put("OptionPane.messageMnemonic", "M"); 
        } catch (Exception e) {
           
        }
    }
    
    
    

    @Test
    void onSave_SuccessCase_CallsControllerAndCloses() {

        mockVm.success = true;
        mockVm.message = "Cập nhật thành công!";
        
        ArgumentCaptor<SuaHoaDonInputData> inputCaptor = ArgumentCaptor.forClass(SuaHoaDonInputData.class);

        form.onSave();

        verify(mockController, times(1)).sua(inputCaptor.capture());
        
        SuaHoaDonInputData capturedInput = inputCaptor.getValue();
        assertEquals("KH001", capturedInput.maKH);
        assertEquals("Nguyen Van A", capturedInput.hoTen);
        assertEquals(LocalDate.parse("2023-11-10"), capturedInput.ngayRaHoaDon);
        assertEquals("VN", capturedInput.loai);
        assertEquals("SINH_HOAT", capturedInput.doiTuong);
        assertEquals(150.5, capturedInput.soLuong, 0.001);
        assertEquals(1000.5, capturedInput.donGia, 0.001);
        assertEquals(50.0, capturedInput.dinhMuc, 0.001);
        assertEquals("", capturedInput.quocTich);
        
    }

    @Test
    void onSave_ValidationFailed_InvalidDate_ShowsErrorAndDoesNotCallController() {
        form.txtNgay.setText("2023/11/10"); // Ngày không hợp lệ (sai format)
        mockJOptionPane();
        
        form.onSave();

        verify(mockController, never()).sua(any(SuaHoaDonInputData.class));
        
    }
    
    @Test
    void onSave_FailureCaseFromController_ShowsErrorAndDoesNotClose() {
        mockVm.success = false;
        mockVm.message = "Lỗi nghiệp vụ: Mã khách hàng đã tồn tại.";
        mockJOptionPane(); 

        form.onSave();

        verify(mockController, times(1)).sua(any(SuaHoaDonInputData.class));
        
    }
    
    @Test
    void onSave_ParseErrorForNumericFields_DefaultsToZero() {
        form.txtSoLuong.setText("abc"); 
        form.txtDonGia.setText("xyz");
        form.txtDinhMuc.setText("def"); 
        mockVm.success = true; 
        ArgumentCaptor<SuaHoaDonInputData> inputCaptor = ArgumentCaptor.forClass(SuaHoaDonInputData.class);

        form.onSave();

        verify(mockController, times(1)).sua(inputCaptor.capture());
        SuaHoaDonInputData capturedInput = inputCaptor.getValue();
        
        assertEquals(0.0, capturedInput.soLuong, "Số lượng nên mặc định là 0.0");
        assertEquals(0.0, capturedInput.donGia, "Đơn giá nên mặc định là 0.0");
        assertEquals(0.0, capturedInput.dinhMuc, "Định mức nên mặc định là 0.0");
    }
    
    @Test
    void onSave_NuocNgoaiFieldsCheck() {
        form.cmbLoai.setSelectedItem("NN");
        form.txtDinhMuc.setEnabled(false);
        form.txtQuocTich.setText("USA");
        form.txtQuocTich.setEnabled(true);
        form.txtDinhMuc.setText("100.0"); 
        mockVm.success = true;
        ArgumentCaptor<SuaHoaDonInputData> inputCaptor = ArgumentCaptor.forClass(SuaHoaDonInputData.class);
        
        form.onSave();
        
        verify(mockController, times(1)).sua(inputCaptor.capture());
        SuaHoaDonInputData capturedInput = inputCaptor.getValue();
        
        assertEquals("NN", capturedInput.loai);
        assertEquals("USA", capturedInput.quocTich);

        assertEquals(0.0, capturedInput.dinhMuc, "Định mức phải là 0.0 cho KH Nước Ngoài");
    }
}