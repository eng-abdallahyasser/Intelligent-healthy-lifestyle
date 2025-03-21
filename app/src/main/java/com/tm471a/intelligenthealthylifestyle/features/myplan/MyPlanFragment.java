package com.tm471a.intelligenthealthylifestyle.features.myplan;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentMyPlanBinding;
import com.tm471a.intelligenthealthylifestyle.features.profile.ProfileViewModel;


public class MyPlanFragment extends Fragment {

    private FragmentMyPlanBinding binding;
    private MyPlanViewModel viewModel;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyPlanBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MyPlanViewModel.class);

        viewModel.subscribedPlan.observe(getViewLifecycleOwner(), workoutPlan -> {
            if (workoutPlan != null) {
                binding.tvPlanName.setText(workoutPlan.getPlanName());
                binding.tvDuration.setText("Duration: " + workoutPlan.getDuration());
                binding.tvDifficulty.setText("Difficulty: " + workoutPlan.getDifficulty());
                binding.tvGoal.setText("Goal: " + workoutPlan.getGoal());
                binding.tvDaysPerWeek.setText("Days Per Week: " + workoutPlan.getDaysPerWeek() + "days");
                binding.tvSessionDuration.setText("Session Duration: " + workoutPlan.getSessionDuration());

                WorkoutDayAdapter workoutDayAdapter = new WorkoutDayAdapter(workoutPlan.getWorkoutDayList());
                binding.rvWorkoutDays.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvWorkoutDays.setAdapter(workoutDayAdapter);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}