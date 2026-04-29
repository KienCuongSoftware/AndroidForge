package com.example.lab08_firebase.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab08_firebase.R;
import com.example.lab08_firebase.model.User;
import com.example.lab08_firebase.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private LoginViewModel loginViewModel;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        buttonLogin.setOnClickListener(v -> handleLogin());
        textViewForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, R.string.forgot_password_dev, Toast.LENGTH_SHORT).show());
    }

    private void handleLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Log.d(TAG, "Input Email: " + email + ", Input Password: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_fill_all, Toast.LENGTH_SHORT).show();
            return;
        }

        loginViewModel.login(email).observe(this, user -> {
            if (user != null) {
                if (password.equals(user.getPassword())) {
                    navigateToRoleBasedActivity(user);
                } else {
                    Toast.makeText(this, R.string.error_wrong_password, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.error_user_not_found, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToRoleBasedActivity(User user) {
        if ("admin".equals(user.getRole())) {
            startActivity(new Intent(this, AdminActivity.class));
            Toast.makeText(this, R.string.welcome_admin, Toast.LENGTH_SHORT).show();
        } else if ("customer".equals(user.getRole())) {
            startActivity(new Intent(this, CustomerActivity.class));
            Toast.makeText(this, R.string.welcome_customer, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.error_invalid_role, Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }
}
