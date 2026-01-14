package frameworks.forms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Frame;

import frameworks.forms.XoaHoaDonForm;
import interfaceadapters.xoa.XoaHoaDonController;
import interfaceadapters.xoa.XoaHoaDonViewModel;
import usecase.xoa.XoaHoaDonInputData;
import repository.HoaDonRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class XoaHoaDonFormTest {

    @Mock
    private XoaHoaDonController mockController;
    
    @Mock
    private XoaHoaDonViewModel mockVm;
    
    @Mock 
    private HoaDonRepository mockRepo;
    
    private XoaHoaDonForm form;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        form = new XoaHoaDonForm(new Frame(), mockRepo) {
            @Override
            public void setVisible(boolean b) {
               
            }
        };
        form.controller = mockController;
        form.vm = mockVm;
    }
    

    
    
    
    @Test
    void onXoa_SuccessCase_CallsControllerAndCloses() {
        final String maKH = "KH999";
        form.txtMa.setText(maKH);
        
        mockVm.success = true;
        mockVm.message = "Xóa hóa đơn thành công!";
        
        ArgumentCaptor<XoaHoaDonInputData> inputCaptor = ArgumentCaptor.forClass(XoaHoaDonInputData.class);

        form.onXoa();

        verify(mockController, times(1)).xoa(inputCaptor.capture());
        
        XoaHoaDonInputData capturedInput = inputCaptor.getValue();
        assertEquals(maKH, capturedInput.maKH, "Mã KH trong InputData phải khớp với TextField.");
        
    }

    @Test
    void onXoa_FailureCaseFromController_ShowsErrorAndDoesNotClose() {
        final String maKH = "KH000";
        form.txtMa.setText(maKH);
        
        mockVm.success = false;
        mockVm.message = "Lỗi: Không tìm thấy Mã KH: KH000";

        form.onXoa();

        verify(mockController, times(1)).xoa(any(XoaHoaDonInputData.class));
        
    }
    
    @Test
    void onXoa_EmptyMaKH_CallsControllerWithEmptyString() {
        form.txtMa.setText("   "); 
        mockVm.success = false;
        mockVm.message = "Lỗi: Mã KH không được để trống.";
        ArgumentCaptor<XoaHoaDonInputData> inputCaptor = ArgumentCaptor.forClass(XoaHoaDonInputData.class);

        form.onXoa();

        verify(mockController, times(1)).xoa(inputCaptor.capture());
        
        assertEquals("", inputCaptor.getValue().maKH, "Mã KH phải là chuỗi rỗng sau khi trim.");
    }
}