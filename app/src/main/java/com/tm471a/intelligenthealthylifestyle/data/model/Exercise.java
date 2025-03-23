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

    // Getters
    @SerializedName("name")
    public String getName() { return name; }

    @SerializedName("description")
    public String getDescription() { return description; }

    @SerializedName("primary_muscles")
    public List<String> getPrimaryMuscles() { return primaryMuscles; }
    @SerializedName("equipment")
    public List<String> getEquipment() { return equipment; }

    @SerializedName("sets")
    public String getSets() { return sets; }

    @SerializedName("reps")
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