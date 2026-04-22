package com.example.lab07_sqlite;

import android.text.Editable;
import android.text.TextWatcher;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab07_sqlite.model.Student;
import com.example.lab07_sqlite.view.StudentAdapter;
import com.example.lab07_sqlite.viewmodel.StudentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private StudentViewModel viewModel;
    private StudentAdapter adapter;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        setupRecyclerView();
        setupSearch();
        setupAddButton();
        observeViewModel();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Student student) {
                showAddOrEditDialog(student);
            }

            @Override
            public void onDeleteClick(Student student) {
                showDeleteConfirmDialog(student);
            }

            @Override
            public void onItemClick(Student student) {
                showStudentDetailDialog(student);
            }
        });
    }

    private void setupSearch() {
        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.searchStudentByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupAddButton() {
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddOrEditDialog(null));
    }

    private void observeViewModel() {
        viewModel.getStudentsLiveData().observe(this, students -> adapter.setStudentList(students));
        viewModel.getMessageLiveData().observe(this, message -> {
            if (message != null && !message.trim().isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddOrEditDialog(Student editingStudent) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_student, null);
        EditText etStudentCode = dialogView.findViewById(R.id.etStudentCode);
        EditText etFullName = dialogView.findViewById(R.id.etFullName);
        EditText etDateOfBirth = dialogView.findViewById(R.id.etDateOfBirth);
        EditText etMajor = dialogView.findViewById(R.id.etMajor);
        EditText etGpa = dialogView.findViewById(R.id.etGpa);

        boolean isEditMode = editingStudent != null;
        if (isEditMode) {
            etStudentCode.setText(editingStudent.getStudentCode());
            etFullName.setText(editingStudent.getFullName());
            etDateOfBirth.setText(editingStudent.getDateOfBirth());
            etMajor.setText(editingStudent.getMajor());
            etGpa.setText(String.format(Locale.US, "%.2f", editingStudent.getGpa()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEditMode ? R.string.edit_student : R.string.add_student);
        builder.setView(dialogView);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(isEditMode ? R.string.update : R.string.save, null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String studentCode = etStudentCode.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            String dateOfBirth = etDateOfBirth.getText().toString().trim();
            String major = etMajor.getText().toString().trim();
            String gpaStr = etGpa.getText().toString().trim();

            if (!validateInputs(etStudentCode, etFullName, etDateOfBirth, etMajor, etGpa, studentCode, fullName, dateOfBirth, major, gpaStr)) {
                return;
            }

            double gpa = Double.parseDouble(gpaStr);
            if (isEditMode) {
                editingStudent.setStudentCode(studentCode);
                editingStudent.setFullName(fullName);
                editingStudent.setDateOfBirth(dateOfBirth);
                editingStudent.setMajor(major);
                editingStudent.setGpa(gpa);
                viewModel.updateStudent(editingStudent);
            } else {
                viewModel.insertStudent(new Student(studentCode, fullName, dateOfBirth, major, gpa));
            }
            dialog.dismiss();
        }));
        dialog.show();
    }

    private boolean validateInputs(EditText etStudentCode, EditText etFullName, EditText etDateOfBirth,
                                   EditText etMajor, EditText etGpa, String studentCode, String fullName,
                                   String dateOfBirth, String major, String gpaStr) {
        if (studentCode.isEmpty()) {
            etStudentCode.setError(getString(R.string.error_student_code_required));
            return false;
        }
        if (fullName.isEmpty()) {
            etFullName.setError(getString(R.string.error_full_name_required));
            return false;
        }
        if (dateOfBirth.isEmpty()) {
            etDateOfBirth.setError(getString(R.string.error_date_required));
            return false;
        }
        if (major.isEmpty()) {
            etMajor.setError(getString(R.string.error_major_required));
            return false;
        }
        if (gpaStr.isEmpty()) {
            etGpa.setError(getString(R.string.error_gpa_required));
            return false;
        }
        try {
            double gpa = Double.parseDouble(gpaStr);
            if (gpa < 0.0 || gpa > 4.0) {
                etGpa.setError(getString(R.string.error_gpa_range));
                return false;
            }
        } catch (NumberFormatException e) {
            etGpa.setError(getString(R.string.error_gpa_invalid));
            return false;
        }
        return true;
    }

    private void showDeleteConfirmDialog(Student student) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage(getString(R.string.confirm_delete_message, student.getFullName()))
                .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.deleteStudent(student.getId()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showStudentDetailDialog(Student student) {
        String message = getString(
                R.string.student_detail_message,
                student.getStudentCode(),
                student.getFullName(),
                student.getDateOfBirth(),
                student.getMajor(),
                String.format(Locale.US, "%.2f", student.getGpa())
        );

        new AlertDialog.Builder(this)
                .setTitle(R.string.student_details)
                .setMessage(message)
                .setPositiveButton(R.string.close, null)
                .show();
    }
}