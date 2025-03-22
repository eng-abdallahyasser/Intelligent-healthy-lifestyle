package com.tm471a.intelligenthealthylifestyle.features.myplan;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.model.Exercise;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutDay;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyPlanViewModel extends ViewModel {

    WorkoutRepository workoutRepository;

    MutableLiveData<WorkoutPlan> subscribedPlan = new MutableLiveData<>();
    public MyPlanViewModel() {
        workoutRepository = new WorkoutRepository();
        workoutRepository.getSubscribedWorkoutPlan(
                new WorkoutRepository.OnWorkoutPlanFetchedListener() {
                    @Override
                    public void onSuccess(WorkoutPlan workoutPlan) {
                        if (workoutPlan != null && workoutPlan.getWorkoutDayList() != null &&
                                !workoutPlan.getWorkoutDayList().isEmpty() &&
                                workoutPlan.getWorkoutDayList().get(0).getExercises() != null &&
                                !workoutPlan.getWorkoutDayList().get(0).getExercises().isEmpty() &&
                                workoutPlan.getWorkoutDayList().get(0).getExercises().get(0).getPrimaryMuscles() != null) {
                            String firstMuscle = workoutPlan.getWorkoutDayList()
                                    .get(0).getExercises().get(0).getPrimaryMuscles().get(0);

                            Log.d("MyPlanViewModel DEBUG", "First primary muscle: " + firstMuscle);
                        } else {
                            Log.e("MyPlanViewModel error", "Workout plan or primaryMuscles is null!");
                        }
                        subscribedPlan.postValue(workoutPlan);
                    }
                    public void onError(String errorMessage) {
                        Log.e("MyPlanViewModel", "Error fetching workout plan: " + errorMessage);

                        // Create a placeholder workout plan
                        List<Exercise> placeholderExercises = new ArrayList<>();

                        List<WorkoutDay> placeholderDays = new ArrayList<>();
                        placeholderDays.add(new WorkoutDay("", placeholderExercises));

                        WorkoutPlan placeholderPlan = new WorkoutPlan(
                                "please subscribe to a workout plan",
                                "1 week",
                                placeholderDays,
                                "Beginner"
                        );

                        // Set the placeholder plan as the default value
                        subscribedPlan.postValue(placeholderPlan.initExerciseCompleted());
                    }
                }
        );
    }

    public void updateExerciseCompletion(int position, WorkoutDay workoutDay) {
        workoutRepository.updateExerciseCompletion(position, workoutDay);
    }
}
