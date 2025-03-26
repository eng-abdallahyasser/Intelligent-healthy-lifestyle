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
import android.widget.Toast;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentMyPlanBinding;
import com.tm471a.intelligenthealthylifestyle.features.profile.ProfileViewModel;

import java.util.Objects;


public class MyPlanFragment extends Fragment {

    private FragmentMyPlanBinding binding;
    private MyPlanViewModel viewModel;
    WorkoutDayAdapter workoutDayAdapter;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyPlanBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MyPlanViewModel.class);
        binding.rvWorkoutDays.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.statusMessage.observe(getViewLifecycleOwner(), message -> {
            if (message.equals("done")) {
                workoutDayAdapter = new WorkoutDayAdapter(Objects.requireNonNull(viewModel.subscribedPlan.getValue()).getWorkoutDayList(), viewModel);
                binding.rvWorkoutDays.setAdapter(workoutDayAdapter);

                Log.d("MyPlanFragment", "Plan Name: " + viewModel.plan.getPlanName());
            }
            else{
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.subscribedPlan.observe(getViewLifecycleOwner(), workoutPlan -> {
            if (viewModel.statusMessage.getValue().equals("done") && workoutPlan.checkWeekCoplated()) {
                workoutDayAdapter = new WorkoutDayAdapter(Objects.requireNonNull(viewModel.subscribedPlan.getValue()).getWorkoutDayList(), viewModel);
                binding.rvWorkoutDays.setAdapter(workoutDayAdapter);
            }
            if (workoutPlan != null) {
                binding.tvPlanName.setText(workoutPlan.getPlanName());
                binding.tvDuration.setText("Duration: " + workoutPlan.getDuration());
                binding.tvDifficulty.setText("Difficulty: " + workoutPlan.getDifficulty());
                binding.tvGoal.setText("Goal: " + workoutPlan.getGoal());
                binding.tvDaysPerWeek.setText("Days Per Week: " + workoutPlan.getDaysPerWeek() + "days");
                binding.tvSessionDuration.setText("Session Duration: " + workoutPlan.getSessionDuration());
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