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

public class StudentInfoFragment extends Fragment {

    private EditText editTextMaSinhVien;
    private EditText editTextHoTen;
    private Button buttonTiep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_info, container, false);

        editTextMaSinhVien = view.findViewById(R.id.editTextMaSinhVien);
        editTextHoTen = view.findViewById(R.id.editTextHoTen);
        buttonTiep = view.findViewById(R.id.buttonTiep);

        buttonTiep.setOnClickListener(v -> xuLyChuyenFragment());

        return view;
    }

    private void xuLyChuyenFragment() {
        String maSinhVien = editTextMaSinhVien.getText().toString().trim();
        String hoTen = editTextHoTen.getText().toString().trim();

        if (maSinhVien.isEmpty()) {
            Toast.makeText(requireContext(), "Chưa nhập mã sinh viên", Toast.LENGTH_SHORT).show();
            editTextMaSinhVien.requestFocus();
            return;
        }
        if (hoTen.isEmpty()) {
            Toast.makeText(requireContext(), "Chưa nhập họ và tên", Toast.LENGTH_SHORT).show();
            editTextHoTen.requestFocus();
            return;
        }

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.chuyenSangScoreFragment(maSinhVien, hoTen);
        }
    }
}
