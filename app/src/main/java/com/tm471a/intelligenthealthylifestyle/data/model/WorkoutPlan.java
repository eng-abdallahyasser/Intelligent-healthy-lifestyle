package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class WorkoutPlan implements Serializable {
    @PropertyName("plan_name")
    private String planName;
    @PropertyName("goal")
    private String goal;

    @PropertyName("duration")
    private String duration;
    @PropertyName("days_per_week")
    private String daysPerWeek;
    @PropertyName("session_duration")
    private String sessionDuration;

    @PropertyName("workout_day_list")
    private List<WorkoutDay> workoutDayList;
    @PropertyName("number_of_completed_weeks")
    private int numberOfCompletedWeeks;
    @PropertyName("difficulty")
    private String difficulty;

    // Firestore requires empty constructor
    public WorkoutPlan() {}

    public WorkoutPlan(String planName, String duration, List<WorkoutDay> exercises, String difficulty) {
        this.planName = planName;
        this.duration = duration;
        this.workoutDayList = exercises != null ? exercises : new ArrayList<>();
        this.difficulty = difficulty;
        this.goal = "";
        this.daysPerWeek = "";
        this.sessionDuration = "";
        this.numberOfCompletedWeeks = 0;
    }

    public WorkoutPlan initExerciseCompleted() {
        numberOfCompletedWeeks=0;
        for (WorkoutDay workoutDay : workoutDayList) {
            workoutDay.initExerciseCompleted();
        }
        return this;
    }
    @PropertyName("number_of_completed_weeks")
    public int getNumberOfCompletedWeeks() {
        return numberOfCompletedWeeks;
    }
    @PropertyName("number_of_completed_weeks")
    public void setNumberOfCompletedWeeks(int numberOfCompletedWeeks) {
        this.numberOfCompletedWeeks = numberOfCompletedWeeks;
    }

    public Boolean checkWeekCoplated(){
        Boolean completed = true;
        for(WorkoutDay workoutDay : workoutDayList){
            if(!workoutDay.getDayCompleted()){
               completed=false;
            }
        }
        if (completed){
            numberOfCompletedWeeks++;
            for(WorkoutDay workoutDay : workoutDayList){
                workoutDay.resetExerciseCompleted(false);
            }
        }
        return completed;
    }
    @PropertyName("goal")
    public String getGoal() {
        return goal;
    }
    @PropertyName("goal")
    public void setGoal(String goal) {
        this.goal = goal;
    }
    @PropertyName("days_per_week")
    public String getDaysPerWeek() {
        return daysPerWeek;
    }
    @PropertyName("days_per_week")
    public void setDaysPerWeek(String daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }
    @PropertyName("session_duration")
    public String getSessionDuration() {
        return sessionDuration;
    }
    @PropertyName("session_duration")
    public void setSessionDuration(String sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    @PropertyName("plan_name")
    public String getPlanName() { return planName; }

    @PropertyName("plan_name")
    public void setPlanName(String planName) { this.planName = planName; }
    @PropertyName("duration")
    public String getDuration() {
        return duration;
    }
    @PropertyName("duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }
    @PropertyName("workout_day_list")
    public List<WorkoutDay> getWorkoutDayList() {
        return workoutDayList;
    }
    @PropertyName("workout_day_list")
    public void setWorkoutDayList(List<WorkoutDay> workoutDayList) {
        this.workoutDayList = workoutDayList;
    }
    @PropertyName("difficulty")
    public String getDifficulty() {
        return difficulty;
    }
    @PropertyName("difficulty")
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}