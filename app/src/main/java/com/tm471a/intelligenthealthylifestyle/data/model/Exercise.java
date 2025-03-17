package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Exercise {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("primary_muscles")
    private List<String> primaryMuscles;

    @SerializedName("equipment")
    private List<String> equipment;

    @SerializedName("sets")
    private String sets;

    @SerializedName("reps")
    private String reps;

    public Exercise() {}

    // Getters and setters with @PropertyName annotations
    @PropertyName("name")
    public String getName() { return name; }

    @PropertyName("primary_muscles")
    public List<String> getPrimaryMuscles() { return primaryMuscles; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimaryMuscles(List<String> primaryMuscles) {
        this.primaryMuscles = primaryMuscles;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }
}