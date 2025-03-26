package com.tm471a.intelligenthealthylifestyle.data.model;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDay {
    private String day;
    private List<Exercise> exercises;

    private List<Boolean> exerciseCompleted;

    public WorkoutDay() {
    }
    public WorkoutDay(String day, List<Exercise> exercises) {
        this.day = day;
        this.exercises = exercises;
    }
    public void initExerciseCompleted() {
        exerciseCompleted = new ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            exerciseCompleted.add(false);
        }
    }
    public List<Boolean> getExerciseCompleted() {
        return exerciseCompleted;
    }
    public Boolean getDayCompleted() {
        for (Boolean completed : exerciseCompleted) {
            if (!completed) {
                return false;
            }
        }
        return true;
    }

    public void setExerciseCompleted(List<Boolean> exerciseCompleted) {
        this.exerciseCompleted = exerciseCompleted;
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

    public void resetExerciseCompleted(boolean b) {
        for (int i = 0; i < exerciseCompleted.size(); i++) {
            exerciseCompleted.set(i, b);
        }
    }
}
