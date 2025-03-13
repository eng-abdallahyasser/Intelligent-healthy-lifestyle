package com.tm471a.intelligenthealthylifestyle.auth;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.features.MainActivity;
import com.tm471a.intelligenthealthylifestyle.databinding.ActivitySignupBinding;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private AuthViewModel authViewModel;
    String[] fitnessLevels = new String[]{"Beginner", "Intermediate", "Advanced"};
    String selectedFitnessLevel="Beginner";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, fitnessLevels);
        binding.etCurrentFitnessLevel.setAdapter(adapter);

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
        binding.btnGender.setOnClickListener(view->{
            if(binding.btnGender.getText().equals("Male")){
                binding.btnGender.setText("Female");
            }else{
                binding.btnGender.setText("Male");
            }
        });
        binding.etCurrentFitnessLevel.setOnItemClickListener((parent, view, position, id) -> {
            selectedFitnessLevel = fitnessLevels[position];
        });
    }

    private void attemptSignUp() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String heightStr = binding.etHeight.getText().toString().trim();
        String weightStr = binding.etWeight.getText().toString().trim();
        String ageStr = binding.etAge.getText().toString().trim();
        String gender = binding.btnGender.getText().toString().trim();
        String medicalConditionsOrInjuries = binding.etMedicalConditionsOrInjuries.getText().toString().trim();

        if (validateInputs(name, email, password, heightStr, weightStr)) {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            int age = Integer.parseInt(ageStr);
            authViewModel.signUp(name, email, password, height, weight,age, gender, medicalConditionsOrInjuries, selectedFitnessLevel, getSelectedFitnessGoals(), getSelectedDietaryPreferences());
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
    // Helper method to get fitness goals
    private List<String> getSelectedFitnessGoals() {
        List<String> goals = new ArrayList<>();
        if (binding.cbMuscleGain.isChecked()) goals.add("Muscle Gain");
        if (binding.cbWeightLoss.isChecked()) goals.add("Weight Loss");
        if (binding.cbEndurance.isChecked()) goals.add("Endurance");
        return goals;
    }

    // Helper method to get dietary preferences
    private List<String> getSelectedDietaryPreferences() {
        List<String> preferences = new ArrayList<>();
        if (binding.cbVegetarian.isChecked()) preferences.add("Vegetarian");
        if (binding.cbVegan.isChecked()) preferences.add("Vegan");
        if (binding.cbGlutenFree.isChecked()) preferences.add("Gluten Free");
        return preferences;
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