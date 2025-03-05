package com.tm471a.intelligenthealthylifestyle.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;

import java.util.ArrayList;
import java.util.List;

public class WorkoutRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<WorkoutPlan>> workoutPlans = new MutableLiveData<>();

    public LiveData<List<WorkoutPlan>> getWorkoutPlans(String userId) {
        db.collection("users").document(userId)
                .collection("workout_plans")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        workoutPlans.postValue(null);
                        return;
                    }

                    List<WorkoutPlan> plans = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        WorkoutPlan plan = doc.toObject(WorkoutPlan.class);
                        plans.add(plan);
                    }
                    workoutPlans.postValue(plans);
                });

        return workoutPlans;
    }
}