package interfaceadapters.sua;

import usecase.sua.SuaHoaDonInputData;
import boundary.InputBoundary;

public class SuaHoaDonController {
    private final InputBoundary<SuaHoaDonInputData> useCase;

    public SuaHoaDonController(InputBoundary<SuaHoaDonInputData> useCase) {
        this.useCase = useCase;
    }

    public void sua(SuaHoaDonInputData data) {
        useCase.handle(data);
    }
}
