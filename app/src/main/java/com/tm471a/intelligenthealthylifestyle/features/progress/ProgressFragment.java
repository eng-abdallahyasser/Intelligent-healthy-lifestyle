package com.tm471a.intelligenthealthylifestyle.features.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tm471a.intelligenthealthylifestyle.databinding.FragmentProgressBinding;

public class ProgressFragment extends Fragment {

    private FragmentProgressBinding binding;
    private ProgressViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgressBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        setupInputHandlers();
        setupCharts();
        setupObservers();

        return binding.getRoot();
    }

    private void setupInputHandlers() {
        binding.btnLogWeight.setOnClickListener(v -> {
            String weightStr = binding.etWeight.getText().toString();
            if (!weightStr.isEmpty()) {
                float weight = Float.parseFloat(weightStr);
                viewModel.saveWeight(weight);
                binding.etWeight.setText("");
                showToast("Weight logged successfully!");
            } else {
                showToast("Please enter weight");
            }
        });

        binding.btnLogWorkout.setOnClickListener(v -> {
            String countStr = binding.etWorkoutCount.getText().toString();
            if (!countStr.isEmpty()) {
                int count = Integer.parseInt(countStr);
                viewModel.saveWorkout(count);
                binding.etWorkoutCount.setText("");
                showToast("Workout logged successfully!");
            } else {
                showToast("Please enter workout count");
            }
        });

        binding.btnLogMeasurements.setOnClickListener(v -> {
            String chestStr = binding.etChest.getText().toString();
            String waistStr = binding.etWaist.getText().toString();
            String hipsStr = binding.etHips.getText().toString();

            if (!chestStr.isEmpty() && !waistStr.isEmpty() && !hipsStr.isEmpty()) {
                float chest = Float.parseFloat(chestStr);
                float waist = Float.parseFloat(waistStr);
                float hips = Float.parseFloat(hipsStr);

                viewModel.saveBodyMeasurement(chest, waist, hips);
                clearMeasurementFields();
                showToast("Measurements logged successfully!");
            } else {
                showToast("Please fill all measurement fields");
            }
        });
    }

    private void clearMeasurementFields() {
        binding.etChest.setText("");
        binding.etWaist.setText("");
        binding.etHips.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void setupCharts() {

    }

    private void setupObservers() {

    }
}