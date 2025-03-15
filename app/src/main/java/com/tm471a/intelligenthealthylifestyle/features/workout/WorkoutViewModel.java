package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkoutViewModel extends ViewModel {

    private WorkoutRepository repository = new WorkoutRepository();
    private MutableLiveData<List<WorkoutPlan>> workoutPlans ;
    private final Gson gson;

    public WorkoutViewModel() {
        this.gson = new Gson();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        workoutPlans = repository.getWorkoutPlans(userId);
    }

    public void generateWorkoutPlan() {
        repository.generateWorkoutPlan(new WorkoutRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    String cleanedJson = response.replaceAll("```json\n|```", "");
                    WorkoutPlan workoutPlan = gson.fromJson(cleanedJson, WorkoutPlan.class);
                    Log.d("WorkoutPlan", "Plan Name: " + workoutPlan.getPlanName());

                    List<WorkoutPlan> currentPlans = workoutPlans.getValue();
                    if (currentPlans == null) {
                        currentPlans = new ArrayList<>();
                    }
                    // Add the new workout plan to the list
                    currentPlans.add(workoutPlan);

                    // Post the updated list to the LiveData
                    workoutPlans.postValue(currentPlans);
                } catch (Exception e) {
                    // Handle parsing errors
                    Log.e("WorkoutPlan", "Error parsing workout plan", e);
                }
            }

            @Override
            public void onError(String error) {
                // Handle errors
            }
        });
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlans() {
        return workoutPlans;
    }
}