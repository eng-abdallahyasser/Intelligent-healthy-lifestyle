package com.tm471a.intelligenthealthylifestyle.data.model;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.PropertyName;

public class WorkoutDay {
    @PropertyName("day")
    private String day;
    @PropertyName("exercises")

    private List<Exercise> exercises;
    @PropertyName("exerciseCompleted")
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
    @PropertyName("exerciseCompleted")
    public List<Boolean> getExerciseCompleted() {
        return exerciseCompleted;
    }
    public Boolean getDayCompleted() {
        if (exerciseCompleted == null) {
            return false;
        }
        for (Boolean completed : exerciseCompleted) {
            if (!completed) {
                return false;
            }
        }
        return true;
    }
    @PropertyName("exerciseCompleted")
    public void setExerciseCompleted(List<Boolean> exerciseCompleted) {
        this.exerciseCompleted = exerciseCompleted;
    }
    @PropertyName("day")
    public String getDay() {
        return day;
    }
    @PropertyName("day")
    public void setDay(String day) {
        this.day = day;
    }
    @PropertyName("exercises")
    public List<Exercise> getExercises() {
        return exercises;
    }
    @PropertyName("exercises")
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void resetExerciseCompleted(boolean b) {
        for (int i = 0; i < exerciseCompleted.size(); i++) {
            exerciseCompleted.set(i, b);
        }
    }
}
