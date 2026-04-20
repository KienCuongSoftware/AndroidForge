package com.example.lab4_bai2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ScoreFragment extends Fragment {

    private EditText editTextDiemToan;
    private EditText editTextDiemLy;
    private EditText editTextDiemHoa;
    private Button buttonTinhDiem;
    private String maSinhVien;
    private String hoTen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            maSinhVien = bundle.getString("maSinhVien");
            hoTen = bundle.getString("hoTen");
        }

        editTextDiemToan = view.findViewById(R.id.editTextDiemToan);
        editTextDiemLy = view.findViewById(R.id.editTextDiemLy);
        editTextDiemHoa = view.findViewById(R.id.editTextDiemHoa);
        buttonTinhDiem = view.findViewById(R.id.buttonTinhDiem);

        buttonTinhDiem.setOnClickListener(v -> xuLyTinhDiem());

        return view;
    }

    private void xuLyTinhDiem() {
        String diemToanStr = editTextDiemToan.getText().toString().trim();
        String diemLyStr = editTextDiemLy.getText().toString().trim();
        String diemHoaStr = editTextDiemHoa.getText().toString().trim();

        if (diemToanStr.isEmpty()) {
            Toast.makeText(requireContext(), "Chưa nhập điểm Toán", Toast.LENGTH_SHORT).show();
            editTextDiemToan.requestFocus();
            return;
        }
        if (diemLyStr.isEmpty()) {
            Toast.makeText(requireContext(), "Chưa nhập điểm Lý", Toast.LENGTH_SHORT).show();
            editTextDiemLy.requestFocus();
            return;
        }
        if (diemHoaStr.isEmpty()) {
            Toast.makeText(requireContext(), "Chưa nhập điểm Hóa", Toast.LENGTH_SHORT).show();
            editTextDiemHoa.requestFocus();
            return;
        }

        try {
            double diemToan = Double.parseDouble(diemToanStr);
            double diemLy = Double.parseDouble(diemLyStr);
            double diemHoa = Double.parseDouble(diemHoaStr);

            if (diemToan < 0 || diemToan > 10 || diemLy < 0 || diemLy > 10
                    || diemHoa < 0 || diemHoa > 10) {
                Toast.makeText(requireContext(), "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                return;
            }

            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.chuyenSangResultFragment(maSinhVien, hoTen, diemToan, diemLy, diemHoa);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
