package com.example.lab07_sqlite.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab07_sqlite.R;
import com.example.lab07_sqlite.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private final List<Student> studentList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Student student);

        void onDeleteClick(Student student);

        void onItemClick(Student student);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setStudentList(List<Student> students) {
        studentList.clear();
        if (students != null) {
            studentList.addAll(students);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(studentList.get(position));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStudentCode;
        private final TextView tvFullName;
        private final TextView tvDateOfBirth;
        private final TextView tvMajor;
        private final TextView tvGpa;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentCode = itemView.findViewById(R.id.tvStudentCode);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvDateOfBirth = itemView.findViewById(R.id.tvDateOfBirth);
            tvMajor = itemView.findViewById(R.id.tvMajor);
            tvGpa = itemView.findViewById(R.id.tvGpa);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(Student student) {
            tvStudentCode.setText(itemView.getContext().getString(R.string.student_code_value, student.getStudentCode()));
            tvFullName.setText(student.getFullName());
            tvDateOfBirth.setText(itemView.getContext().getString(R.string.date_of_birth_value, student.getDateOfBirth()));
            tvMajor.setText(itemView.getContext().getString(R.string.major_value, student.getMajor()));
            tvGpa.setText(itemView.getContext().getString(R.string.gpa_value, String.format(Locale.US, "%.2f", student.getGpa())));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(student);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(student);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(student);
                }
            });
        }
    }
}
