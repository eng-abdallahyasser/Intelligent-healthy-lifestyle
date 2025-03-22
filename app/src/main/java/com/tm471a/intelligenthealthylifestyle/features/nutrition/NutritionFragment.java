package com.tm471a.intelligenthealthylifestyle.features.nutrition;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tm471a.intelligenthealthylifestyle.databinding.FragmentNutritionBinding;


public class NutritionFragment extends Fragment {

    private FragmentNutritionBinding binding;
    private NutritionViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNutritionBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NutritionViewModel.class);
        binding.rvMeals.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.nutritionContentLayout.setVisibility(View.GONE);

        setupObservers();
        setupClickListeners();

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    private void setupObservers() {
        viewModel.getNutritionAdvice().observe(getViewLifecycleOwner(), nutritionAdvice -> {
            binding.tvTitle.setText(nutritionAdvice.getSeoTitle());
            binding.tvSeoContent.setText(nutritionAdvice.getDescription());
            binding.tvGoal.setText(nutritionAdvice.getGoal());
            binding.tvDescription.setText(nutritionAdvice.getSeoContent());
            binding.tvCalories.setText(nutritionAdvice.getCaloriesPerDay()+" calories per day");

            binding.tvCarbs.setText(nutritionAdvice.getMacronutrients().getCarbohydrates());
            binding.progressCarbs.setProgress(formJsonToInt(nutritionAdvice.getMacronutrients().getCarbohydrates()));
            binding.tvProteins.setText(nutritionAdvice.getMacronutrients().getProteins());
            binding.progressProteins.setProgress(formJsonToInt(nutritionAdvice.getMacronutrients().getProteins()));
            binding.tvFats.setText(nutritionAdvice.getMacronutrients().getFats());
            binding.progressFats.setProgress(formJsonToInt(nutritionAdvice.getMacronutrients().getFats()));

            MealSuggestionAdapter mealSuggestionAdapter=new MealSuggestionAdapter(nutritionAdvice.getMealSuggestions());
            binding.rvMeals.setAdapter(mealSuggestionAdapter);
        } );
        viewModel.getStatusMessage().observe(getViewLifecycleOwner(),status->{
            binding.tvMessage.setText(status);
            if (status=="Initiating...") {
                binding.statusCardView.setVisibility(View.VISIBLE);
                binding.circularProgress.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }
            else if(status=="Generating Nutrition Advice For You..."){
                binding.statusCardView.setVisibility(View.VISIBLE);
                binding.circularProgress.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }
            else if(status=="done"){
                binding.statusCardView.setVisibility(View.GONE);
                binding.nutritionContentLayout.setVisibility(View.VISIBLE);
            }
            else {
                binding.tvMessage.setVisibility(View.VISIBLE);
                binding.circularProgress.setVisibility(View.GONE);
                binding.statusCardView.setVisibility(View.VISIBLE);}
        });

    }

    private int formJsonToInt(String input) {
        return Integer.parseInt(input.split(" ")[0].replaceAll("[^0-9]", ""));
    }
    private void setupClickListeners() {
        binding.fabAddNutritionAdvice.setOnClickListener(v -> {
            Log.d("NutritionFragment", "FAB clicked!");
            viewModel.generateNutritionAdvice();
        });
    }
}