package com.tm471a.intelligenthealthylifestyle.features.myplan;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;

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
                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                        Log.e("MyPlanViewModel", "Error fetching workout plan: " + errorMessage);
                    }
                }
        );
    }

}
