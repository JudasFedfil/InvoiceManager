package usecase.xoa;

import usecase.HoaDonControl;
import boundary.OutputBoundary;
import boundary.InputBoundary;
import repository.HoaDonRepository;

public class XoaHoaDonUseCaseControl 
    extends HoaDonControl<XoaHoaDonOutputData> 
    implements InputBoundary<XoaHoaDonInputData> 
{

    private final XoaHoaDonEntity useCase;

    public XoaHoaDonUseCaseControl(HoaDonRepository repo, OutputBoundary<XoaHoaDonOutputData> output) {
        super(repo, output); 
        this.useCase = new XoaHoaDonEntity(repo); 
    }

    @Override
    public void handle(XoaHoaDonInputData input) {
        XoaHoaDonOutputData out = useCase.execute(input); 

        outputBoundary.present(out);
    }
}