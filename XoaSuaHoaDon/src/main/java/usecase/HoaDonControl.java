package usecase;

import repository.HoaDonRepository;
import boundary.OutputBoundary;

public abstract class HoaDonControl<T> {
    protected HoaDonRepository repo;
    protected OutputBoundary<T> outputBoundary;

    public HoaDonControl(HoaDonRepository repo, OutputBoundary<T> outputBoundary) {
        this.repo = repo;
        this.outputBoundary = outputBoundary;
    }
}
