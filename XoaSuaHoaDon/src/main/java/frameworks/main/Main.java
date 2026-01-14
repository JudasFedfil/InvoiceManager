package frameworks.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import repository.InMemoryHoaDonRepository;
import repository.HoaDonRepository;
import entities.HoaDonEntity;
import frameworks.forms.SuaHoaDonForm;
import frameworks.forms.XoaHoaDonForm;

public class Main {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private final HoaDonRepository repo = new InMemoryHoaDonRepository();

    public Main() {
        initUI();
        loadData();
    }

    private void initUI() {
        frame = new JFrame("Quản lý Hóa Đơn - SuaXoaHoaDon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[] {"Mã KH","Họ tên","Ngày","Loại","Số lượng","Đơn giá","Định mức/Quốc tịch","Thành tiền"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnP = new JPanel();
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        btnP.add(btnSua);
        btnP.add(btnXoa);
        frame.add(btnP, BorderLayout.SOUTH);

        btnSua.addActionListener(e -> onSua());
        btnXoa.addActionListener(e -> onXoa());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<HoaDonEntity> ds = repo.getAll();
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        for (HoaDonEntity h : ds) {
            String extra = "";
            if ("VN".equals(h.getLoai())) {
                extra = String.valueOf(((entities.HoaDonVietNam)h).getDinhMuc());
            } else {
                extra = ((entities.HoaDonNuocNgoai)h).getQuocTich();
            }
            tableModel.addRow(new Object[] {
                h.getMaKH(), h.getHoTen(), h.getNgayRaHoaDon().format(fmt), h.getLoai(),
                h.getSoLuong(), h.getDonGia(), extra, h.tinhThanhTien()
            });
        }
    }

    private void onSua() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(frame, "Chọn một hàng để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String ma = (String)tableModel.getValueAt(r, 0);
        HoaDonEntity h = repo.findByMaKH(ma);
        SuaHoaDonForm form = new SuaHoaDonForm(frame, repo);
        form.setHoaDonToEdit(h);
        form.setVisible(true);
        loadData();
    }

    private void onXoa() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(frame, "Chọn một hàng để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String ma = (String)tableModel.getValueAt(r, 0);

        XoaHoaDonForm xform = new XoaHoaDonForm(frame, repo);
        xform.txtMa.setText(ma);
        xform.setVisible(true);

        loadData();
    }


    public static void main(String[] args) {
               SwingUtilities.invokeLater(() -> new Main());
    }
}
