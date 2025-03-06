package com.tm471a.intelligenthealthylifestyle.data.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class ProgressRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<Float>> weightData = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> workoutData = new MutableLiveData<>();

    public void loadWeightProgress() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .collection("weight_logs")
                .orderBy("date", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Float> weights = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Float weight = doc.getDouble("weight").floatValue();
                        weights.add(weight);
                    }
                    weightData.postValue(weights);
                });
    }

    public void loadWorkoutFrequency() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .collection("workout_logs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Integer> workouts = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Integer count = doc.getLong("count").intValue();
                        workouts.add(count);
                    }
                    workoutData.postValue(workouts);
                });
    }

    public MutableLiveData<List<Float>> getWeightData() {
        return weightData;
    }

    public MutableLiveData<List<Integer>> getWorkoutData() {
        return workoutData;
    }
}
