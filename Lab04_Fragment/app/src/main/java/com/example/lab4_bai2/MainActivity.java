package com.example.lab4_bai2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

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

        if (savedInstanceState == null) {
            hienThiFragment(new StudentInfoFragment());
        }
    }

    public void hienThiFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    public void chuyenSangScoreFragment(String maSinhVien, String hoTen) {
        ScoreFragment scoreFragment = new ScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("maSinhVien", maSinhVien);
        bundle.putString("hoTen", hoTen);
        scoreFragment.setArguments(bundle);
        hienThiFragment(scoreFragment);
    }

    public void chuyenSangResultFragment(String maSinhVien, String hoTen,
                                         double diemToan, double diemLy, double diemHoa) {
        ResultFragment resultFragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("maSinhVien", maSinhVien);
        bundle.putString("hoTen", hoTen);
        bundle.putDouble("diemToan", diemToan);
        bundle.putDouble("diemLy", diemLy);
        bundle.putDouble("diemHoa", diemHoa);
        resultFragment.setArguments(bundle);
        hienThiFragment(resultFragment);
    }

    public void quayLaiStudentInfoFragment() {
        hienThiFragment(new StudentInfoFragment());
    }
}
