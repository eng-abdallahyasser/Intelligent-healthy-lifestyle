package com.tm471a.intelligenthealthylifestyle.data.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.tm471a.intelligenthealthylifestyle.data.model.MeasurementLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WeightLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProgressRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<WeightLog>> weightLogs = new MutableLiveData<>();
    private final MutableLiveData<List<WorkoutLog>> workoutLogs = new MutableLiveData<>();
    private final MutableLiveData<List<MeasurementLog>> measurementLogs = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ProgressRepository() {
        setupFirestoreListeners();
    }

    private void setupFirestoreListeners() {
        String userId = getCurrentUserId();
        if (userId == null) return;

        setupWeightLogsListener(userId);
        setupWorkoutLogsListener(userId);
        setupMeasurementLogsListener(userId);
    }

    private void setupWeightLogsListener(String userId) {
        db.collection("Users").document(userId)
                .collection("weight_logs")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> handleSnapshot(
                        value, error,
                        "Weight logs",
                        snapshot -> snapshot.toObject(WeightLog.class),
                        weightLogs
                ));
    }

    private void setupWorkoutLogsListener(String userId) {
        db.collection("Users").document(userId)
                .collection("workout_logs")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> handleSnapshot(
                        value, error,
                        "Workout logs",
                        snapshot -> snapshot.toObject(WorkoutLog.class),
                        workoutLogs
                ));
    }

    private void setupMeasurementLogsListener(String userId) {
        db.collection("Users").document(userId)
                .collection("measurement_logs")
                .orderBy("date", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> handleSnapshot(
                        value, error,
                        "Measurement logs",
                        snapshot -> snapshot.toObject(MeasurementLog.class),
                        measurementLogs
                ));
    }

    private <T> void handleSnapshot(QuerySnapshot snapshot,
                                    Exception error,
                                    String logType,
                                    DocumentMapper<T> mapper,
                                    MutableLiveData<List<T>> liveData) {
        if (error != null) {
            logError("Error loading " + logType + ": " + error.getMessage());
            return;
        }

        if (snapshot == null) {
            logError(logType + " snapshot is null");
            return;
        }

        List<T> items = new ArrayList<>();
        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            T item = mapper.map(doc);
            if (item != null) {
                items.add(item);
            }
        }
        liveData.postValue(items);
    }

    public void logWeight(WeightLog weightLog) {
        db.collection("Users").document(Objects.requireNonNull(getCurrentUserId()))
                .collection("weight_logs")
                .add(weightLog.toMap())
                .addOnSuccessListener(documentReference ->
                        Log.d("ProgressRepo", "Weight logged successfully"))
                .addOnFailureListener(e ->
                        Log.w("ProgressRepo", "Error logging weight", e));
        // Assuming weightLog.getWeight() returns a valid weight value
        Map<String, Object> weightData = new HashMap<>();
        weightData.put("weight", weightLog.getWeight());
        // Set data in Firestore and merge with existing data
        db.collection("Users").document(Objects.requireNonNull(getCurrentUserId()))
                .set(weightData, SetOptions.merge());
    }

    public void logWorkout(WorkoutLog workoutLog) {
        db.collection("Users").document(Objects.requireNonNull(getCurrentUserId()))
                .collection("workout_logs")
                .add(workoutLog.toMap())
                .addOnSuccessListener(documentReference ->
                        Log.d("ProgressRepo", "Weight logged successfully"))
                .addOnFailureListener(e ->
                        Log.w("ProgressRepo", "Error logging weight", e));
    }

    public void logMeasurement(MeasurementLog measurementLog) {
        db.collection("Users").document(Objects.requireNonNull(getCurrentUserId()))
                .collection("measurement_logs")
                .add(measurementLog.toMap())
                .addOnSuccessListener(documentReference ->
                        Log.d("ProgressRepo", "Weight logged successfully"))
                .addOnFailureListener(e ->
                        Log.w("ProgressRepo", "Error logging weight", e));
    }

    // LiveData Getters
    public LiveData<List<WeightLog>> getWeightLogs() { return weightLogs; }
    public LiveData<List<WorkoutLog>> getWorkoutLogs() { return workoutLogs; }
    public LiveData<List<MeasurementLog>> getMeasurementLogs() { return measurementLogs; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            logError("User not authenticated");
            return null;
        }
        return user.getUid();
    }

    private void logError(String message) {
        Log.e("ProgressRepo", message);
        errorMessage.postValue(message);
    }

    private interface DocumentMapper<T> {
        T map(DocumentSnapshot snapshot);
    }
}