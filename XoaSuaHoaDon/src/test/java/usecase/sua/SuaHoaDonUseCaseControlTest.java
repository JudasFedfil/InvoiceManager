package usecase.sua;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import boundary.OutputBoundary;
import repository.HoaDonRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class SuaHoaDonUseCaseControlTest {

    private class TestableSuaHoaDonUseCaseControl extends SuaHoaDonUseCaseControl {
        

        public TestableSuaHoaDonUseCaseControl(HoaDonRepository repo, OutputBoundary<SuaHoaDonOutputData> output, SuaHoaDonEntity mockEntity) {
             super(repo, output);

        }
    }
    @Mock
    private SuaHoaDonEntity mockUseCaseEntity; 
    
    @Mock
    private HoaDonRepository mockRepo;
    
    @Mock
    private OutputBoundary<SuaHoaDonOutputData> mockOutputBoundary;

    private SuaHoaDonUseCaseControl useCaseControl;

    private SuaHoaDonInputData dummyInput;
    private SuaHoaDonOutputData expectedOutput;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        useCaseControl = new SuaHoaDonUseCaseControl(mockRepo, mockOutputBoundary);
        
        dummyInput = new SuaHoaDonInputData();
        
        expectedOutput = new SuaHoaDonOutputData(); 
        expectedOutput.success = true;
        expectedOutput.message = "Test OK";
    }
    
    
    @Test
    void handle_ShouldExecuteUseCaseAndPresentOutput() throws Exception {

        try {
            java.lang.reflect.Field field = SuaHoaDonUseCaseControl.class.getDeclaredField("useCase");
            field.setAccessible(true);
            field.set(useCaseControl, mockUseCaseEntity); 
        } catch (Exception e) {
        }
        
        when(mockUseCaseEntity.execute(dummyInput)).thenReturn(expectedOutput);

        useCaseControl.handle(dummyInput);

        verify(mockUseCaseEntity, times(1)).execute(dummyInput);
        
        verify(mockOutputBoundary, times(1)).present(expectedOutput);
    }
}