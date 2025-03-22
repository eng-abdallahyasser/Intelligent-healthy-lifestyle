package com.tm471a.intelligenthealthylifestyle.features.progress;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.model.MeasurementLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WeightLog;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutLog;
import com.tm471a.intelligenthealthylifestyle.data.repository.ProgressRepository;
import java.util.List;

public class ProgressViewModel extends ViewModel {
    private final ProgressRepository repo = new ProgressRepository();




    // Save methods
    public void saveWeight(float weight) { repo.logWeight(new WeightLog(weight)); }
    public void saveWorkout(int count) { repo.logWorkout(new WorkoutLog(count)); }
    public void saveBodyMeasurement(float chest, float waist, float hips) {
        repo.logMeasurement(new MeasurementLog(chest, waist, hips));
    }
    public LiveData<List<WeightLog>> getWeightLogs() {
        return repo.getWeightLogs();
    }
    public LiveData<List<WorkoutLog>> getWorkoutLogs() {
        return repo.getWorkoutLogs();
    }
    public LiveData<List<MeasurementLog>> getMeasurementLogs() {
        return repo.getMeasurementLogs();
    }
}