package usecase.sua;

import usecase.HoaDonControl;
import repository.HoaDonRepository;
import boundary.OutputBoundary;
import boundary.InputBoundary;

public class SuaHoaDonUseCaseControl extends HoaDonControl implements InputBoundary<SuaHoaDonInputData> {

        private final SuaHoaDonEntity useCase;

    public SuaHoaDonUseCaseControl(HoaDonRepository repo, OutputBoundary output) {
        super(repo, output);
        this.useCase = new SuaHoaDonEntity(repo); 
    }

    @Override
    public void handle(SuaHoaDonInputData input) {
        SuaHoaDonOutputData out = useCase.execute(input);

        outputBoundary.present(out);
    }
}