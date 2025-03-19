package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutViewModel extends ViewModel {

    private WorkoutRepository repository = new WorkoutRepository();
    private MutableLiveData<List<WorkoutPlan>> workoutPlans ;
    private final MutableLiveData<String> statusMessage =new MutableLiveData<String>("Initiating...");
    private final Gson gson;

    public WorkoutViewModel() {
        this.gson = new Gson();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        workoutPlans = repository.getWorkoutPlans(userId);
        repository.getIsInitialized().observeForever(isReady -> {
            if (isReady) initFourWorkoutPlan();
        });
    }

    public void generateWorkoutPlan() {
        statusMessage.postValue("Loading...");
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
                    statusMessage.postValue("done");
                } catch (Exception e) {
                    // Handle parsing errors
                    Log.e("WorkoutPlan", "Error parsing workout plan", e);
                    statusMessage.postValue(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                // Handle errors
                statusMessage.postValue(error);
            }
        });
    }
    public void initFourWorkoutPlan() {
        statusMessage.postValue("Loading...");
        repository.initFourWorkoutPlan(new WorkoutRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    String cleanedJson = response.replaceAll("```json\n|```", "");
                    Type listType = new TypeToken<List<WorkoutPlan>>() {}.getType();

                    List<WorkoutPlan> initWorkoutPlans = gson.fromJson(cleanedJson, listType);
                    Log.d("WorkoutPlan", "Plan Name: " + initWorkoutPlans.get(0).getPlanName());

                    // Post the updated list to the LiveData
                    workoutPlans.postValue(initWorkoutPlans);
                    statusMessage.postValue("done");
                } catch (Exception e) {
                    // Handle parsing errors
                    Log.e("WorkoutPlan", "Error parsing workout plan", e);
                    statusMessage.postValue(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                // Handle errors
                statusMessage.postValue(error);
            }
        });
    }

    public LiveData<List<WorkoutPlan>> getWorkoutPlans() {
        return workoutPlans;
    }

    public MutableLiveData<String> getStatusMessage() {
        return statusMessage;
    }
    public MutableLiveData<Boolean> getIsRepoInit(){
        return repository.getIsInitialized();
    }

}