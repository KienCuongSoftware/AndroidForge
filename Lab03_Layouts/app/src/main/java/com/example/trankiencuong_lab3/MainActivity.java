package com.example.trankiencuong_lab3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButton(R.id.btn_bai1, R.layout.layout_bai1_linear, "Bai 1 - LinearLayout");
        setupButton(R.id.btn_bai2, R.layout.layout_bai2_relative, "Bai 2 - RelativeLayout");
        setupButton(R.id.btn_bai3, R.layout.layout_bai3_constraint, "Bai 3 - ConstraintLayout");
        setupButton(R.id.btn_bai4, R.layout.layout_bai4_table, "Bai 4 - TableLayout");
        setupButton(R.id.btn_bai5, R.layout.layout_bai5_grid, "Bai 5 - GridLayout");
        setupButton(R.id.btn_bai6, R.layout.layout_bai6_frame, "Bai 6 - FrameLayout");
    }

    private void setupButton(int buttonId, int layoutResId, String title) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
            intent.putExtra(PreviewActivity.EXTRA_LAYOUT_ID, layoutResId);
            intent.putExtra(PreviewActivity.EXTRA_TITLE, title);
            startActivity(intent);
        });
    }
}