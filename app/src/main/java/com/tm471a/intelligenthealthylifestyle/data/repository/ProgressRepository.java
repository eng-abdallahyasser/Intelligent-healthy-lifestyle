package com.tm471a.intelligenthealthylifestyle.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<Float>> weightData = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> workoutData = new MutableLiveData<>();

    public void loadWeightProgress() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("Users").document(userId)
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

        db.collection("Users").document(userId)
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
    public void saveWeight(float weight) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("weight", weight);
        data.put("date", FieldValue.serverTimestamp());

        db.collection("Users").document(userId)
                .collection("weight_logs")
                .add(data)
                .addOnSuccessListener(documentReference ->
                        Log.d("ProgressRepo", "Weight logged successfully"))
                .addOnFailureListener(e ->
                        Log.w("ProgressRepo", "Error logging weight", e));
    }

    public void saveWorkout(int count) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        data.put("date", FieldValue.serverTimestamp());

        db.collection("Users").document(userId)
                .collection("workout_logs")
                .add(data)
                .addOnSuccessListener(documentReference ->
                        Log.d("ProgressRepo", "Workout logged successfully"))
                .addOnFailureListener(e ->
                        Log.w("ProgressRepo", "Error logging workout", e));
    }

    public void saveBodyMeasurement(float chest, float waist, float hips) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("chest", chest);
        data.put("waist", waist);
        data.put("hips", hips);
        data.put("date", FieldValue.serverTimestamp());

        db.collection("Users").document(userId)
                .collection("measurement_logs")
                .add(data)
                .addOnSuccessListener(documentReference ->
                        Log.d("ProgressRepo", "Measurements logged successfully"))
                .addOnFailureListener(e ->
                        Log.w("ProgressRepo", "Error logging measurements", e));
    }
}
