package interfaceadapters.xoa;

import boundary.OutputBoundary;
import usecase.xoa.XoaHoaDonOutputData;
import interfaceadapters.xoa.XoaHoaDonViewModel;

public class XoaHoaDonPresenter implements OutputBoundary<XoaHoaDonOutputData> {
    private final XoaHoaDonViewModel vm;

    public XoaHoaDonPresenter(XoaHoaDonViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void present(XoaHoaDonOutputData outputData) {
        vm.success = outputData.success;
        vm.message = outputData.message;
    }
}
