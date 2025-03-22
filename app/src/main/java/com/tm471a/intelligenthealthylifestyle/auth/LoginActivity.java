package com.tm471a.intelligenthealthylifestyle.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.features.MainActivity;
import com.tm471a.intelligenthealthylifestyle.databinding.ActivityLoginBinding;

// LoginActivity.java
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        setupObservers();
        setupClickListeners();
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
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.tvSignup.setOnClickListener(v -> navigateToSignUp());
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (validateInputs(email, password)) {
            authViewModel.login(email, password);
        }
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            showError("Please enter email");
            return false;
        }
        if (password.isEmpty()) {
            showError("Please enter password");
            return false;
        }
        return true;
    }

    private void showLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!isLoading);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // In LoginActivity.java
    private void navigateToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
        // Optional animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void handleError(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}