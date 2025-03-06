package com.tm471a.intelligenthealthylifestyle.dashboard;

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
}