package com.tm471a.intelligenthealthylifestyle.features.workout;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutViewModel extends ViewModel {

    private WorkoutRepository repository = new WorkoutRepository();
    private boolean hasInitialized = false;
    private MutableLiveData<List<WorkoutPlan>> workoutPlans= new MutableLiveData<>() ;
    private final MutableLiveData<String> statusMessage =new MutableLiveData<String>("Initiating...");
    private final Gson gson;

    public WorkoutViewModel() {
        this.gson = new Gson();
        repository.getIsInitialized().observeForever(isReady -> {
            if (isReady && !hasInitialized) {
                fetchWorkoutPlans();
                hasInitialized = true;
            }
        });
    }

    public void generateWorkoutPlan() {
        statusMessage.postValue("Generating Suggested Workout Plan For You...");
        repository.generateWorkoutPlan(new WorkoutRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    String cleanedJson = response.replaceAll("```json\n|```", "");
                    WorkoutPlan workoutPlan = gson.fromJson(cleanedJson, WorkoutPlan.class);
                    Log.d("WorkoutPlan", "response : " + cleanedJson);

                    List<WorkoutPlan> currentPlans = workoutPlans.getValue();
                    if (currentPlans == null) {
                        currentPlans = new ArrayList<>();
                    }
                    // Add the new workout plan to the list
                    currentPlans.add(workoutPlan);

                    // Post the updated list to the LiveData
                    workoutPlans.postValue(currentPlans);
                    statusMessage.postValue("done");
                    repository.saveWorkoutPlan(workoutPlan);
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
    public void fetchWorkoutPlans() {

        repository.fetchWorkoutPlans(new WorkoutRepository.OnWorkoutPlansFetchedListener() {
            @Override
            public void onSuccess(List<WorkoutPlan> fetchedWorkoutPlans) {
                workoutPlans.postValue(fetchedWorkoutPlans);
                Log.d("FetchSuccess", "Fetched " + fetchedWorkoutPlans.size() + " workout plans");
                statusMessage.postValue("done");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FetchError", "Failed to fetch workout plans from firestore", e);
                statusMessage.postValue("Generating Suggested Workout Plan For You...");
                repository.initWorkoutPlans(new WorkoutRepository.ResponseCallback() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String cleanedJson = response.replaceAll("```json\n|```", "");
                            Type listType = new TypeToken<List<WorkoutPlan>>() {}.getType();
                            Log.d("WorkoutPlan", "response : " + cleanedJson);
                            List<WorkoutPlan> generatedtWorkoutPlans = gson.fromJson(cleanedJson, listType);

                            // Post the updated list to the LiveData
                            workoutPlans.postValue(generatedtWorkoutPlans);
                            statusMessage.postValue("done");

                            for (WorkoutPlan plan: generatedtWorkoutPlans){
                                repository.saveWorkoutPlan(plan);
                            }

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

    public void subscribeToWorkoutPlan(WorkoutPlan workoutPlan) {
        repository.subscribeToWorkoutPlan(workoutPlan);

        statusMessage.setValue("workout plan subscribed...");
        Log.d("WorkoutViewModel", " workout plan subscribed...\nstatusMessage = "+statusMessage.getValue());
    }
}