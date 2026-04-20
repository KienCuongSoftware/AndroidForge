package com.example.trankiencuong_lab3;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {

    public static final String EXTRA_LAYOUT_ID = "extra_layout_id";
    public static final String EXTRA_TITLE = "extra_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId = getIntent().getIntExtra(EXTRA_LAYOUT_ID, R.layout.layout_bai1_linear);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null) {
            setTitle(title);
        }

        setContentView(layoutId);
    }
}
