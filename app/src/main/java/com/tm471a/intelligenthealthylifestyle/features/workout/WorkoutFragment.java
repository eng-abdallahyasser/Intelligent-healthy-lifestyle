package com.tm471a.intelligenthealthylifestyle.features.workout;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                NavController navController = Navigation.findNavController(requireView());
                WorkoutAdapter adapter = new WorkoutAdapter(plans,navController);
                binding.rvWorkouts.setAdapter(adapter);
            }
        });
        viewModel.getStatusMessage().observe(getViewLifecycleOwner(), massage -> {
            binding.tvMessage.setText(massage);

            if (massage=="Initiating...") {
                binding.statusCardView.setVisibility(View.VISIBLE);
                binding.circularProgress.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }
            else if(massage=="Generating Suggested Workout Plan For You..."){
                binding.statusCardView.setVisibility(View.VISIBLE);
                binding.circularProgress.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.VISIBLE);
            }
            else if(massage=="done"){
                binding.statusCardView.setVisibility(View.GONE);
            }
            else {
            binding.tvMessage.setVisibility(View.VISIBLE);
            binding.circularProgress.setVisibility(View.GONE);
            binding.statusCardView.setVisibility(View.VISIBLE);}
        });
    }

    private void setupClickListeners() {
        binding.fabLogWorkout.setOnClickListener(v -> {
            viewModel.generateWorkoutPlan();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}