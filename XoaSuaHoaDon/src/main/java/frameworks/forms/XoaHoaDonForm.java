package frameworks.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import interfaceadapters.xoa.*;
import usecase.xoa.XoaHoaDonInputData;
import repository.HoaDonRepository;

public class XoaHoaDonForm extends JDialog {
    public HoaDonRepository repo;
    public XoaHoaDonController controller;
    public XoaHoaDonViewModel vm;

    public JTextField txtMa;
    public JButton btnXoa, btnCancel;

    public XoaHoaDonForm(Frame owner, HoaDonRepository repo) {
        super(owner, "Xóa Hóa Đơn", true);
        this.repo = repo;
        this.vm = new XoaHoaDonViewModel();
        this.controller = new XoaHoaDonController(new usecase.xoa.XoaHoaDonUseCaseControl(repo, new XoaHoaDonPresenter(vm)));

        initComponents();
        setSize(320,150);
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(0,2,6,6));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(new JLabel("Mã KH cần xóa:"));
        txtMa = new JTextField();
        p.add(txtMa);
        add(p, BorderLayout.CENTER);

        JPanel btnP = new JPanel();
        btnXoa = new JButton("Xóa");
        btnCancel = new JButton("Hủy");
        btnP.add(btnXoa);
        btnP.add(btnCancel);
        add(btnP, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> setVisible(false));
        btnXoa.addActionListener(e -> onXoa());
    }

    public void onXoa() {
        XoaHoaDonInputData in = new XoaHoaDonInputData();
        in.maKH = txtMa.getText().trim();
        controller.xoa(in);
        if (vm.success) {
            JOptionPane.showMessageDialog(this, vm.message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, vm.message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
