package com.tm471a.intelligenthealthylifestyle.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tm471a.intelligenthealthylifestyle.databinding.ActivitySubscribePlanBinding;
import com.tm471a.intelligenthealthylifestyle.features.MainActivity;
import com.tm471a.intelligenthealthylifestyle.features.workout.WorkoutViewModel;

public class SubscribePlan extends AppCompatActivity {

    private ActivitySubscribePlanBinding binding;
    private WorkoutViewModel sharedWorkoutViewModel;

    private AuthViewModel authViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscribePlanBinding.inflate(getLayoutInflater());
        authViewModel = new ViewModelProvider(
                this,
                new AuthViewModelFactory(getApplication())
        ).get(AuthViewModel.class);
        setContentView(binding.getRoot());
        sharedWorkoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        sharedWorkoutViewModel.getStatusMessage().observe(this, message -> {
            if (message.equals("workout plan subscribed...")) {
                navigateToDashboard();
            }
        });
    }
    private void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}