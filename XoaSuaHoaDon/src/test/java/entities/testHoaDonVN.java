package entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class testHoaDonVN {
	@Test
    void tinhThanhTien_ShouldCalculateCorrectly() {
        String maKH = "NN001";
        String hoTen = "John Doe";
        LocalDate ngay = LocalDate.of(2023, 11, 12);
        double soLuong = 100.0;
        double donGia = 15.5;
        String quocTich = "USA";

        HoaDonNuocNgoai hoaDon = new HoaDonNuocNgoai(
            maKH, hoTen, ngay, soLuong, donGia, quocTich
        );

        double actualThanhTien = hoaDon.tinhThanhTien();


        double expectedThanhTien = 1550.0;
        double delta = 0.0001; 

        assertEquals(expectedThanhTien, actualThanhTien, delta,
            "Thành tiền của hóa đơn nước ngoài phải là SoLuong * DonGia"
        );
    }
	

	
	private HoaDonNuocNgoai createHoaDon(double soLuong, double donGia) {
        return new HoaDonNuocNgoai(
            "NN002", "Jane Smith", LocalDate.of(2023, 11, 13),
            soLuong, donGia, "CAN"
        );
    }
	@Test
    void tinhThanhTienSoLuong0() {
        //SoLuong = 0, DonGia = 10.0
        HoaDonNuocNgoai hoaDon = createHoaDon(0.0, 10.0);
        double delta = 0.0001;

        double expected = 0.0; // 0.0 * 10.0 = 0.0
        assertEquals(expected, hoaDon.tinhThanhTien(), delta,
            "Thành tiền phải bằng 0 khi số lượng bằng 0."
        );
    }
	
	
	//Đơn giá 0
	@Test
    void tinhThanhTien_WhenDonGiaIsZero_ShouldReturnZero() {
        // SoLuong = 50.0, DonGia = 0.0
        HoaDonNuocNgoai hoaDon = createHoaDon(50.0, 0.0);
        double delta = 0.0001;

        double expected = 0.0; // 50.0 * 0.0 = 0.0
        assertEquals(expected, hoaDon.tinhThanhTien(), delta,
            "Thành tiền phải bằng 0 khi đơn giá bằng 0."
        );
    }
}
