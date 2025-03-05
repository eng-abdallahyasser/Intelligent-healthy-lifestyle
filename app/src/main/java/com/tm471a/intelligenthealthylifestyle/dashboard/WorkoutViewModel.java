package com.tm471a.intelligenthealthylifestyle.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends ViewModel {

    private WorkoutRepository repository = new WorkoutRepository();
    private LiveData<List<WorkoutPlan>> workoutPlans;

    public WorkoutViewModel() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        workoutPlans = repository.getWorkoutPlans(userId);
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlans() {
        return workoutPlans;
    }
}