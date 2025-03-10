package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class WorkoutPlan {
    private String planName;
    private String duration;
    private List<Exercise> exercises;
    private String difficulty;

    // Firestore requires empty constructor
    public WorkoutPlan() {}

    public WorkoutPlan(String planName, String duration, List<Exercise> exercises, String difficulty) {
        this.planName = planName;
        this.duration = duration;
        this.exercises = exercises;
        this.difficulty = difficulty;
    }

    @PropertyName("plan_name")
    public String getPlanName() { return planName; }

    @PropertyName("plan_name")
    public void setPlanName(String planName) { this.planName = planName; }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}