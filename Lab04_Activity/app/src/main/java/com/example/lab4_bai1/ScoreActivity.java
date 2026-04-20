package com.example.lab4_bai1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewThongTinSinhVien;
    private EditText editTextDiemToan;
    private EditText editTextDiemLy;
    private EditText editTextDiemHoa;
    private Button buttonXemKetQua;

    private String maSinhVien;
    private String hoTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewThongTinSinhVien = findViewById(R.id.textViewThongTinSinhVien);
        editTextDiemToan = findViewById(R.id.editTextDiemToan);
        editTextDiemLy = findViewById(R.id.editTextDiemLy);
        editTextDiemHoa = findViewById(R.id.editTextDiemHoa);
        buttonXemKetQua = findViewById(R.id.buttonXemKetQua);

        nhanDuLieuTuMainActivity();
        buttonXemKetQua.setOnClickListener(v -> chuyenSangResultActivity());
    }

    private void nhanDuLieuTuMainActivity() {
        Intent intent = getIntent();
        maSinhVien = intent.getStringExtra("MASV");
        hoTen = intent.getStringExtra("HOTEN");
        if (maSinhVien == null) {
            maSinhVien = "";
        }
        if (hoTen == null) {
            hoTen = "";
        }
        String thongTin = "Mã SV: " + maSinhVien + "\nHọ tên: " + hoTen;
        textViewThongTinSinhVien.setText(thongTin);
    }

    private void chuyenSangResultActivity() {
        String diemToanStr = editTextDiemToan.getText().toString().trim();
        String diemLyStr = editTextDiemLy.getText().toString().trim();
        String diemHoaStr = editTextDiemHoa.getText().toString().trim();

        if (diemToanStr.isEmpty() || diemLyStr.isEmpty() || diemHoaStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ điểm!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double diemToan = Double.parseDouble(diemToanStr);
            double diemLy = Double.parseDouble(diemLyStr);
            double diemHoa = Double.parseDouble(diemHoaStr);

            if (diemToan < 0 || diemToan > 10
                    || diemLy < 0 || diemLy > 10
                    || diemHoa < 0 || diemHoa > 10) {
                Toast.makeText(this, "Điểm phải từ 0 đến 10!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ScoreActivity.this, ResultActivity.class);
            intent.putExtra("MASV", maSinhVien);
            intent.putExtra("HOTEN", hoTen);
            intent.putExtra("DIEMTOAN", diemToan);
            intent.putExtra("DIEMLY", diemLy);
            intent.putExtra("DIEMHOA", diemHoa);
            startActivity(intent);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Điểm phải là số!", Toast.LENGTH_SHORT).show();
        }
    }
}
