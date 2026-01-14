package frameworks.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import entities.*;
import interfaceadapters.sua.*;
import usecase.sua.SuaHoaDonInputData;
import repository.HoaDonRepository;

public class SuaHoaDonForm extends JDialog {
    public HoaDonRepository repo;
    public SuaHoaDonController controller;
    public SuaHoaDonViewModel vm;

    JTextField txtMa;
	public JTextField txtTen;
	public JTextField txtNgay;
	public JTextField txtSoLuong;
	public JTextField txtDonGia;
	public JTextField txtDinhMuc;
	public JTextField txtQuocTich;
	public JComboBox<String> cmbLoai, cmbDoiTuong;
	public JButton btnSave, btnCancel;

    public SuaHoaDonForm(Frame owner, HoaDonRepository repo) {
        super(owner, "Sửa Hóa Đơn", true);
        this.repo = repo;
        this.vm = new SuaHoaDonViewModel();
        this.controller = new SuaHoaDonController(new usecase.sua.SuaHoaDonUseCaseControl(repo, new SuaHoaDonPresenter(vm)));

        initComponents();
        setSize(420, 420);
        setLocationRelativeTo(owner);
    }

    public void initComponents() {
        setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(0,2,6,6));
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        p.add(new JLabel("Mã KH:"));
        txtMa = new JTextField();
        p.add(txtMa);

        p.add(new JLabel("Họ tên:"));
        txtTen = new JTextField();
        p.add(txtTen);

        p.add(new JLabel("Ngày (YYYY-MM-DD):"));
        txtNgay = new JTextField();
        p.add(txtNgay);

        p.add(new JLabel("Loại:"));
        cmbLoai = new JComboBox<>(new String[] {"VN","NN"});
        p.add(cmbLoai);

        p.add(new JLabel("Đối tượng (VN):"));
        cmbDoiTuong = new JComboBox<>(new String[] {"SINH_HOAT","KINH_DOANH","SAN_XUAT"});
        p.add(cmbDoiTuong);

        p.add(new JLabel("Số lượng (KW):"));
        txtSoLuong = new JTextField();
        p.add(txtSoLuong);

        p.add(new JLabel("Đơn giá:"));
        txtDonGia = new JTextField();
        p.add(txtDonGia);

        p.add(new JLabel("Định mức (chỉ VN):"));
        txtDinhMuc = new JTextField();
        p.add(txtDinhMuc);

        p.add(new JLabel("Quốc tịch (chỉ NN):"));
        txtQuocTich = new JTextField();
        p.add(txtQuocTich);

        add(p, BorderLayout.CENTER);

        JPanel btnP = new JPanel();
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        btnP.add(btnSave);
        btnP.add(btnCancel);
        add(btnP, BorderLayout.SOUTH);

        cmbLoai.addActionListener(e -> updateFieldsByLoai());
        btnCancel.addActionListener(e -> setVisible(false));
        btnSave.addActionListener(e -> onSave());
    }

    public void updateFieldsByLoai() {
        boolean isVN = "VN".equals(cmbLoai.getSelectedItem());
        cmbDoiTuong.setEnabled(isVN);
        txtDinhMuc.setEnabled(isVN);
        txtQuocTich.setEnabled(!isVN);
    }

    public void setHoaDonToEdit(entities.HoaDonEntity hoaDon) {
        if (hoaDon == null) {
            txtMa.setText("");
            txtMa.setEditable(true);
            txtTen.setText("");
            txtNgay.setText(LocalDate.now().toString());
            cmbLoai.setSelectedIndex(0);
            cmbDoiTuong.setSelectedIndex(0);
            txtSoLuong.setText("");
            txtDonGia.setText("");
            txtDinhMuc.setText("");
            txtQuocTich.setText("");
        } else {
            txtMa.setText(hoaDon.getMaKH());
            txtMa.setEditable(false);
            txtTen.setText(hoaDon.getHoTen());
            txtNgay.setText(hoaDon.getNgayRaHoaDon().toString());
            txtSoLuong.setText(String.valueOf(hoaDon.getSoLuong()));
            txtDonGia.setText(String.valueOf(hoaDon.getDonGia()));
            if (hoaDon instanceof HoaDonVietNam) {
                cmbLoai.setSelectedItem("VN");
                HoaDonVietNam vn = (HoaDonVietNam) hoaDon;
                cmbDoiTuong.setSelectedItem(vn.getDoiTuong().name());
                txtDinhMuc.setText(String.valueOf(vn.getDinhMuc()));
                txtQuocTich.setText("");
            } else if (hoaDon instanceof HoaDonNuocNgoai) {
                cmbLoai.setSelectedItem("NN");
                HoaDonNuocNgoai nn = (HoaDonNuocNgoai) hoaDon;
                txtQuocTich.setText(nn.getQuocTich());
                txtDinhMuc.setText("");
            }
            updateFieldsByLoai();
        }
    }

    public void onSave() {
        SuaHoaDonInputData in = new SuaHoaDonInputData();
        in.maKH = txtMa.getText().trim();
        in.hoTen = txtTen.getText().trim();
        try {
            in.ngayRaHoaDon = LocalDate.parse(txtNgay.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ. Dùng định dạng YYYY-MM-DD", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        in.loai = (String)cmbLoai.getSelectedItem();
        in.doiTuong = (String)cmbDoiTuong.getSelectedItem();
        try { in.soLuong = Double.parseDouble(txtSoLuong.getText().trim()); } catch (Exception e) { in.soLuong = 0.0; }
        try { in.donGia = Double.parseDouble(txtDonGia.getText().trim()); } catch (Exception e) { in.donGia = 0.0; }
        try { in.dinhMuc = txtDinhMuc.isEnabled() && !txtDinhMuc.getText().isBlank() ? Double.parseDouble(txtDinhMuc.getText().trim()) : 0.0; } catch (Exception e) { in.dinhMuc = 0.0; }
        in.quocTich = txtQuocTich.getText().trim();

        controller.sua(in);
        // presenter updated vm
        if (vm.success) {
            JOptionPane.showMessageDialog(this, vm.message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, vm.message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
