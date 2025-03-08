package com.tm471a.intelligenthealthylifestyle.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupForm();
        setupObservers();

        return binding.getRoot();
    }

    private void setupForm() {
        binding.btnSave.setOnClickListener(v -> {
            // Get current user ID
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Validate and collect all fields
            try {
                User updatedUser = new User(
                        userId,
                        binding.etName.getText().toString().trim(),
                        binding.etEmail.getText().toString().trim(),
                        Double.parseDouble(binding.etHeight.getText().toString()),
                        Double.parseDouble(binding.etWeight.getText().toString()),
                        getSelectedFitnessGoals(),
                        getSelectedDietaryPreferences(),
                        Integer.parseInt(binding.etAge.getText().toString())
                );

                viewModel.updateProfile(updatedUser);

            } catch (NumberFormatException e) {
                showMsg("Please enter valid numerical values for height and weight");
            } catch (Exception e) {
                showMsg("Please fill all required fields");
            }
        });
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

    private void showMsg(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    private void setupObservers() {
        viewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null && getContext() != null) {
                binding.etName.setText(user.getName());
                binding.etEmail.setText(user.getEmail());
                binding.etHeight.setText(String.valueOf(user.getHeight()));
                binding.etWeight.setText(String.valueOf(user.getWeight()));
                binding.etAge.setText(String.valueOf(user.getAge()));
                if (user.getFitnessGoals() != null) {
                    binding.cbEndurance.setChecked(user.getFitnessGoals().contains("Endurance"));
                    binding.cbMuscleGain.setChecked(user.getFitnessGoals().contains("Muscle Gain"));
                    binding.cbWeightLoss.setChecked(user.getFitnessGoals().contains("Weight Loss"));
                }
                if (user.getDietaryPreferences() != null){
                    binding.cbGlutenFree.setChecked(user.getDietaryPreferences().contains("Gluten Free"));
                    binding.cbVegan.setChecked(user.getDietaryPreferences().contains("Vegan"));
                    binding.cbVegetarian.setChecked(user.getDietaryPreferences().contains("Vegetarian"));}
            }
        });

        viewModel.getUpdateStatus().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                showMsg("Profile updated!");
            } else {
                showMsg("Update failed");
            }
        });
    }

}