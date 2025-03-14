package com.tm471a.intelligenthealthylifestyle.features.progress;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.repository.ProgressRepository;
import java.util.List;

public class ProgressViewModel extends ViewModel {
    private final ProgressRepository repository = new ProgressRepository();
    private final MutableLiveData<List<Float>> weightEntries = new MutableLiveData<>();
    private final MutableLiveData<List<Float>> workoutEntries = new MutableLiveData<>();

    public void loadProgressData() {

    }



    public LiveData<List<Float>> getWeightEntries() {
        return weightEntries;
    }

    public LiveData<List<Float>> getWorkoutEntries() {
        return workoutEntries;
    }
    public void saveWeight(float weight) {
        repository.saveWeight(weight);
    }

    public void saveWorkout(int count) {
        repository.saveWorkout(count);
    }

    public void saveBodyMeasurement(float chest, float waist, float hips) {
        repository.saveBodyMeasurement(chest, waist, hips);
    }
}