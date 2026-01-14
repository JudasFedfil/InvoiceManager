package interfaceadapters.sua;

import boundary.OutputBoundary;
import usecase.sua.SuaHoaDonOutputData;
import interfaceadapters.sua.SuaHoaDonViewModel;

public class SuaHoaDonPresenter implements OutputBoundary<SuaHoaDonOutputData> {
    private final SuaHoaDonViewModel vm;

    public SuaHoaDonPresenter(SuaHoaDonViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void present(SuaHoaDonOutputData outputData) {
        vm.success = outputData.success;
        vm.message = outputData.message;
    }
}
