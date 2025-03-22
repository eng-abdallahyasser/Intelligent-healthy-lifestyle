package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

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




    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimaryMuscles(List<String> primaryMuscles) {
        this.primaryMuscles = primaryMuscles;
    }


    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }


    public void setSets(String sets) {
        this.sets = sets;
    }


    public void setReps(String reps) {
        this.reps = reps;
    }
}