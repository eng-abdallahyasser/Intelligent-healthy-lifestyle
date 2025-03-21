package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WorkoutPlan implements Serializable {
    @SerializedName("plan_name")
    private String planName;
    @SerializedName("goal")
    private String goal;

    @SerializedName("duration")
    private String duration;
    @SerializedName("days_per_week")
    private String daysPerWeek;
    @SerializedName("session_duration")
    private String sessionDuration;

    @SerializedName("workout_day_list")
    private List<WorkoutDay> workoutDayList;

    @SerializedName("difficulty")
    private String difficulty;

    // Firestore requires empty constructor
    public WorkoutPlan() {}

    public WorkoutPlan(String planName, String duration, List<WorkoutDay> exercises, String difficulty) {
        this.planName = planName;
        this.duration = duration;
        this.workoutDayList = exercises;
        this.difficulty = difficulty;
    }
    public WorkoutPlan initExerciseCompleted() {
        for (WorkoutDay workoutDay : workoutDayList) {
            workoutDay.initExerciseCompleted();
        }
        return this;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(String daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public String getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(String sessionDuration) {
        this.sessionDuration = sessionDuration;
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

    public List<WorkoutDay> getWorkoutDayList() {
        return workoutDayList;
    }

    public void setWorkoutDayList(List<WorkoutDay> workoutDayList) {
        this.workoutDayList = workoutDayList;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}