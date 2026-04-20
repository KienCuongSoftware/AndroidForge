package com.example.lab4_bai1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    private TextView textViewMaSinhVien;
    private TextView textViewHoTen;
    private TextView textViewDiemToan;
    private TextView textViewDiemLy;
    private TextView textViewDiemHoa;
    private TextView textViewDiemTrungBinh;
    private TextView textViewXepLoai;
    private Button buttonQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewMaSinhVien = findViewById(R.id.textViewMaSinhVien);
        textViewHoTen = findViewById(R.id.textViewHoTen);
        textViewDiemToan = findViewById(R.id.textViewDiemToan);
        textViewDiemLy = findViewById(R.id.textViewDiemLy);
        textViewDiemHoa = findViewById(R.id.textViewDiemHoa);
        textViewDiemTrungBinh = findViewById(R.id.textViewDiemTrungBinh);
        textViewXepLoai = findViewById(R.id.textViewXepLoai);
        buttonQuayLai = findViewById(R.id.buttonQuayLai);

        nhanVaHienThiKetQua();
        buttonQuayLai.setOnClickListener(v -> quayLaiMainActivity());
    }

    private void nhanVaHienThiKetQua() {
        Intent intent = getIntent();
        String maSinhVien = intent.getStringExtra("MASV");
        String hoTen = intent.getStringExtra("HOTEN");
        if (maSinhVien == null) {
            maSinhVien = "";
        }
        if (hoTen == null) {
            hoTen = "";
        }

        double diemToan = intent.getDoubleExtra("DIEMTOAN", 0.0);
        double diemLy = intent.getDoubleExtra("DIEMLY", 0.0);
        double diemHoa = intent.getDoubleExtra("DIEMHOA", 0.0);

        double diemTrungBinh = (diemToan + diemLy + diemHoa) / 3;
        String xepLoai = xepLoaiHocLuc(diemTrungBinh);

        textViewMaSinhVien.setText("Mã SV: " + maSinhVien);
        textViewHoTen.setText("Họ tên: " + hoTen);
        textViewDiemToan.setText("Điểm Toán: " + String.format("%.2f", diemToan));
        textViewDiemLy.setText("Điểm Lý: " + String.format("%.2f", diemLy));
        textViewDiemHoa.setText("Điểm Hóa: " + String.format("%.2f", diemHoa));
        textViewDiemTrungBinh.setText("Điểm TB: " + String.format("%.2f", diemTrungBinh));
        textViewXepLoai.setText("Xếp loại: " + xepLoai);
    }

    private String xepLoaiHocLuc(double diemTrungBinh) {
        if (diemTrungBinh >= 9.0) {
            return "Xuất sắc";
        } else if (diemTrungBinh >= 8.0) {
            return "Giỏi";
        } else if (diemTrungBinh >= 6.5) {
            return "Khá";
        } else if (diemTrungBinh >= 5.0) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }

    private void quayLaiMainActivity() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
