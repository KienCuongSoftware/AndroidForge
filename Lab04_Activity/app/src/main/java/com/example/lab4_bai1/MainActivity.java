package com.example.lab4_bai1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextMaSinhVien;
    private EditText editTextHoTen;
    private Button buttonTiepTheo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextMaSinhVien = findViewById(R.id.editTextMaSinhVien);
        editTextHoTen = findViewById(R.id.editTextHoTen);
        buttonTiepTheo = findViewById(R.id.buttonTiepTheo);

        buttonTiepTheo.setOnClickListener(v -> chuyenSangScoreActivity());
    }

    private void chuyenSangScoreActivity() {
        String maSinhVien = editTextMaSinhVien.getText().toString().trim();
        String hoTen = editTextHoTen.getText().toString().trim();

        if (maSinhVien.isEmpty() || hoTen.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
        intent.putExtra("MASV", maSinhVien);
        intent.putExtra("HOTEN", hoTen);
        startActivity(intent);
    }
}
