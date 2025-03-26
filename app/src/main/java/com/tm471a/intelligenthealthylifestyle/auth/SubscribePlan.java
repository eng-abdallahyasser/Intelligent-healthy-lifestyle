package com.tm471a.intelligenthealthylifestyle.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
                Toast.makeText(this, "Congratulations, you created account correctly", Toast.LENGTH_SHORT).show();
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