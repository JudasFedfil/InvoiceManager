package interfaceadapters.xoa;

import boundary.InputBoundary;
import usecase.xoa.XoaHoaDonInputData;

public class XoaHoaDonController {
    private final InputBoundary<XoaHoaDonInputData> useCase;

    public XoaHoaDonController(InputBoundary<XoaHoaDonInputData> useCase) {
        this.useCase = useCase;
    }

    public void xoa(XoaHoaDonInputData data) {
        useCase.handle(data);
    }
}
