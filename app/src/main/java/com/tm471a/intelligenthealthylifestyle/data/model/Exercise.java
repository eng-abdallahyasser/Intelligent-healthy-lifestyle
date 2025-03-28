package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Exercise {
    @PropertyName("name")
    private String name;

    @PropertyName("description")
    private String description;

    @PropertyName("primary_muscles")
    private List<String> primaryMuscles;

    @PropertyName("equipment")
    private List<String> equipment;

    @PropertyName("sets")
    private String sets;

    @PropertyName("reps")
    private String reps;

    public Exercise() {}

    // Getters
    @PropertyName("name")
    public String getName() { return name; }

    @PropertyName("description")
    public String getDescription() { return description; }

    @PropertyName("primary_muscles")
    public List<String> getPrimaryMuscles() { return primaryMuscles; }
    @PropertyName("equipment")
    public List<String> getEquipment() { return equipment; }

    @PropertyName("sets")
    public String getSets() { return sets; }

    @PropertyName("reps")
    public String getReps() { return reps; }



    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }
    @PropertyName("primary_muscles")
    public void setPrimaryMuscles(List<String> primaryMuscles) {
        this.primaryMuscles = primaryMuscles;
    }

    @PropertyName("equipment")
    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    @PropertyName("sets")
    public void setSets(String sets) {
        this.sets = sets;
    }

    @PropertyName("reps")
    public void setReps(String reps) {
        this.reps = reps;
    }
}