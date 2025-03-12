package com.tm471a.intelligenthealthylifestyle.features.nutrition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tm471a.intelligenthealthylifestyle.databinding.FragmentNutritionBinding;


public class NutritionFragment extends Fragment {

    private FragmentNutritionBinding binding;
    private NutritionViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNutritionBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NutritionViewModel.class);

        setupObservers();
        setupClickListeners();

        return binding.getRoot();
    }

    private void setupObservers() {

    }

    private void setupClickListeners() {
        binding.fabAddMeal.setOnClickListener(v -> {
            // Navigate to add meal screen
        });
    }
}