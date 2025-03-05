package com.tm471a.intelligenthealthylifestyle.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.dashboard.MainActivity;
import com.tm471a.intelligenthealthylifestyle.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupObservers();
        setupClickListeners();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setupObservers() {
        authViewModel.getUserLiveData().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    showLoading(true);
                    break;
                case SUCCESS:
                    showLoading(false);
                    navigateToDashboard();
                    break;
                case ERROR:
                    showLoading(false);
                    handleError(resource.message);
                    break;
            }
        });
    }

    private void setupClickListeners() {
        binding.btnSignup.setOnClickListener(v -> attemptSignUp());
        binding.tvLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptSignUp() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String heightStr = binding.etHeight.getText().toString().trim();
        String weightStr = binding.etWeight.getText().toString().trim();

        if (validateInputs(name, email, password, heightStr, weightStr)) {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            authViewModel.signUp(name, email, password, height, weight);
        }
    }

    private boolean validateInputs(String name, String email, String password, String height, String weight) {
        if (name.isEmpty()) {
            showError("Please enter your name");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Please enter valid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }
        if (height.isEmpty() || Double.parseDouble(height) <= 0) {
            showError("Please enter valid height");
            return false;
        }
        if (weight.isEmpty() || Double.parseDouble(weight) <= 0) {
            showError("Please enter valid weight");
            return false;
        }
        return true;
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnSignup.setEnabled(!isLoading);
    }

    // In SignUpActivity.java
    private void navigateToLogin() {
        finish(); // Simply close current activity to return to Login
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void handleError(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}