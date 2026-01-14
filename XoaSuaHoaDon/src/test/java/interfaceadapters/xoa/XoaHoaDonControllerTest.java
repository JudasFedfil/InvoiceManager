package interfaceadapters.xoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import boundary.InputBoundary;
import usecase.xoa.XoaHoaDonInputData;
import interfaceadapters.xoa.XoaHoaDonController;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class XoaHoaDonControllerTest {

    @Mock
    private InputBoundary<XoaHoaDonInputData> mockUseCase;

    private XoaHoaDonController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new XoaHoaDonController(mockUseCase);
    }

    @Test
    void xoa_ShouldCallUseCaseHandleMethodWithCorrectData() {
        XoaHoaDonInputData testData = new XoaHoaDonInputData();
        testData.maKH = "KH12345";
        
        controller.xoa(testData);
        
        verify(mockUseCase, times(1)).handle(testData);
        
        verifyNoMoreInteractions(mockUseCase);
    }
}