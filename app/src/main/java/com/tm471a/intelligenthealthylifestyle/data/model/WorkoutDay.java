package com.tm471a.intelligenthealthylifestyle.data.model;

import java.util.List;

public class WorkoutDay {
    private String day;
    private List<Exercise> exercises;

    public WorkoutDay() {
    }
    public WorkoutDay(String day, List<Exercise> exercises) {
        this.day = day;
        this.exercises = exercises;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
