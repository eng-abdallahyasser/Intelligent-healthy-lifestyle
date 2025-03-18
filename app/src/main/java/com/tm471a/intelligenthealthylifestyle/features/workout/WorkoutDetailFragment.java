package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.databinding.FragmentWorkoutDetailBinding;


public class WorkoutDetailFragment extends Fragment {
    private FragmentWorkoutDetailBinding binding;
    private WorkoutPlan workoutPlan;

    public WorkoutDetailFragment() {
        // Required empty public constructor
    }

//    public static WorkoutDetailFragment newInstance(String param1, String param2) {
//        WorkoutDetailFragment fragment = new WorkoutDetailFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false);

        // Retrieve the workout plan from arguments
        if (getArguments() != null) {
            workoutPlan = (WorkoutPlan) getArguments().getSerializable("workoutPlan");

            // Populate UI with workout details
            assert workoutPlan != null;

            Log.d("WorkoutDetailFragment", "onCreateView: getArguments() != null \n workoutPlan: " + workoutPlan.getPlanName() + "");
            binding.tvPlanName.setText(workoutPlan.getPlanName());
            binding.tvDuration.setText("Duration: " + workoutPlan.getDuration());
            binding.tvDifficulty.setText("Difficulty: " + workoutPlan.getDifficulty());

            // Setup RecyclerView for Exercises
            ExerciseAdapter adapter = new ExerciseAdapter(workoutPlan.getExercises());
            binding.rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvExercises.setAdapter(adapter);

        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}