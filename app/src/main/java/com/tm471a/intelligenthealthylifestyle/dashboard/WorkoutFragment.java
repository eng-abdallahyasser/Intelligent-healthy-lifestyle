package com.tm471a.intelligenthealthylifestyle.dashboard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentWorkoutBinding;


public class WorkoutFragment extends Fragment {

    private FragmentWorkoutBinding binding;
    private WorkoutViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        setupObservers();
        setupClickListeners();

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.getWorkoutPlans().observe(getViewLifecycleOwner(), plans -> {
            if (plans != null && !plans.isEmpty()) {
                WorkoutAdapter adapter = new WorkoutAdapter(plans);
                binding.rvWorkouts.setAdapter(adapter);
            } else {
                // Show empty state
            }
        });
    }

    private void setupClickListeners() {
        binding.fabLogWorkout.setOnClickListener(v -> {
            // Navigate to workout logging
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}