package com.example.lab07_sqlite.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lab07_sqlite.R;
import com.example.lab07_sqlite.model.Student;
import com.example.lab07_sqlite.model.StudentRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentViewModel extends AndroidViewModel {
    private final StudentRepository repository;
    private final MutableLiveData<List<Student>> studentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new StudentRepository(application);
        loadAllStudents();
    }

    public LiveData<List<Student>> getStudentsLiveData() {
        return studentsLiveData;
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void loadAllStudents() {
        executorService.execute(() -> studentsLiveData.postValue(repository.getAllStudents()));
    }

    public void insertStudent(Student student) {
        executorService.execute(() -> {
            long result = repository.insertStudent(student);
            if (result > 0) {
                messageLiveData.postValue(getApplication().getString(R.string.message_add_success));
                loadAllStudents();
            } else {
                messageLiveData.postValue(getApplication().getString(R.string.message_add_failed));
            }
        });
    }

    public void updateStudent(Student student) {
        executorService.execute(() -> {
            int result = repository.updateStudent(student);
            if (result > 0) {
                messageLiveData.postValue(getApplication().getString(R.string.message_update_success));
                loadAllStudents();
            } else {
                messageLiveData.postValue(getApplication().getString(R.string.message_update_failed));
            }
        });
    }

    public void deleteStudent(int studentId) {
        executorService.execute(() -> {
            int result = repository.deleteStudent(studentId);
            if (result > 0) {
                messageLiveData.postValue(getApplication().getString(R.string.message_delete_success));
                loadAllStudents();
            } else {
                messageLiveData.postValue(getApplication().getString(R.string.message_delete_failed));
            }
        });
    }

    public void searchStudentByName(String keyword) {
        executorService.execute(() -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                studentsLiveData.postValue(repository.getAllStudents());
            } else {
                studentsLiveData.postValue(repository.searchStudentByName(keyword.trim()));
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
