package usecase.xoa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import boundary.OutputBoundary;
import repository.HoaDonRepository;
import usecase.HoaDonControl; 
import usecase.xoa.XoaHoaDonInputData;
import usecase.xoa.XoaHoaDonOutputData;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class XoaHoaDonUseCaseControlTest {

    @Mock
    private HoaDonRepository mockRepo;
    
    @Mock
    private OutputBoundary<XoaHoaDonOutputData> mockOutputBoundary;
    
    @Mock
    private XoaHoaDonEntity mockUseCaseEntity; 
    
    private XoaHoaDonUseCaseControl useCaseControl;

    private XoaHoaDonInputData dummyInput;
    private XoaHoaDonOutputData expectedOutput;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        useCaseControl = new XoaHoaDonUseCaseControl(mockRepo, mockOutputBoundary);
        
        try {
            java.lang.reflect.Field field = XoaHoaDonUseCaseControl.class.getDeclaredField("useCase");
            field.setAccessible(true);
            field.set(useCaseControl, mockUseCaseEntity); // Inject mock
        } catch (Exception e) {

        }
        
        dummyInput = new XoaHoaDonInputData();
        dummyInput.maKH = "KH001";
        
        expectedOutput = new XoaHoaDonOutputData();
        expectedOutput.success = true;
        expectedOutput.message = "Test Xóa OK";
    }
    
    @Test
    void handle_ShouldExecuteUseCaseAndPresentOutput() {
        when(mockUseCaseEntity.execute(dummyInput)).thenReturn(expectedOutput);
        
        useCaseControl.handle(dummyInput);

        verify(mockUseCaseEntity, times(1)).execute(dummyInput);
        
        verify(mockOutputBoundary, times(1)).present(expectedOutput);
        
        verifyNoMoreInteractions(mockOutputBoundary);
        verifyNoMoreInteractions(mockUseCaseEntity);
    }
}