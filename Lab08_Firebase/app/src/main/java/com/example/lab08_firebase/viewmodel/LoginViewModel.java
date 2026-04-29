package com.example.lab08_firebase.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab08_firebase.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";
    private final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");

    public LiveData<User> login(String email) {
        MutableLiveData<User> resultLiveData = new MutableLiveData<>();
        userReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "So luong ban ghi du lieu: " + snapshot.getChildrenCount());
                User foundUser = null;

                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null && email.equals(user.getEmail())) {
                            foundUser = user;
                            Log.d(TAG, "Tim thay nguoi dung phu hop: " + user.getEmail());
                            break;
                        }
                    }
                } else {
                    Log.d(TAG, "Khong tim thay nguoi dung voi email: " + email);
                }

                resultLiveData.setValue(foundUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Loi co so du lieu: " + error.getMessage(), error.toException());
                resultLiveData.setValue(null);
            }
        });

        return resultLiveData;
    }
}
