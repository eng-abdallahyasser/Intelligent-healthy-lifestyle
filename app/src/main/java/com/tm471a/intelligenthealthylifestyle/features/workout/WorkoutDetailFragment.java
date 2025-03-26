package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentWorkoutDetailBinding;


public class WorkoutDetailFragment extends Fragment {
    private FragmentWorkoutDetailBinding binding;
    private WorkoutViewModel workoutViewModel;
    private WorkoutPlan workoutPlan;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false);
        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        // Retrieve the workout plan from arguments
        if (getArguments() != null) {
            workoutPlan = (WorkoutPlan) getArguments().getSerializable("workoutPlan");
            // Populate UI with workout details
            assert workoutPlan != null;
            workoutPlan.initExerciseCompleted();

            Log.d("WorkoutDetailFragment", "onCreateView: getArguments() != null \n workoutPlan: " + workoutPlan.getPlanName() + "");
            binding.tvPlanName.setText(workoutPlan.getPlanName());
            binding.tvDuration.setText("Duration: " + workoutPlan.getDuration());
            binding.tvDifficulty.setText("Difficulty: " + workoutPlan.getDifficulty());
            binding.tvGoal.setText("Goal: " + workoutPlan.getGoal());
            binding.tvDaysPerWeek.setText("Days Per Week: " + workoutPlan.getDaysPerWeek() + "days");
            binding.tvSessionDuration.setText("Session Duration: " + workoutPlan.getSessionDuration());

            // Setup RecyclerView for Exercises
            WorkoutDayAdapter adapter = new WorkoutDayAdapter(workoutPlan.getWorkoutDayList());
            binding.rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvExercises.setAdapter(adapter);
            binding.btnSubscribe.setOnClickListener(v -> {
                NavController navController= Navigation.findNavController(requireView());
                workoutViewModel.subscribeToWorkoutPlan(workoutPlan);
                navController.popBackStack();
                Toast.makeText(getContext(), "Workout Plan Subscribed", Toast.LENGTH_SHORT).show();
            });

        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}