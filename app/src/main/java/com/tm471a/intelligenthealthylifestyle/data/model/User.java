package com.tm471a.intelligenthealthylifestyle.data.model;

import androidx.annotation.NonNull;

import java.util.List;

public class User {
    private String uid;
    private String name;
    private String email;
    private double height;
    private double weight;
    private List<String> fitnessGoals;
    private List<String> dietaryPreferences;

    // Add constructor
    public User() {} // Needed for Firestore

    public User(String uid, String name, String email, double height, double weight,
                List<String> fitnessGoals, List<String> dietaryPreferences) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.fitnessGoals = fitnessGoals;
        this.dietaryPreferences = dietaryPreferences;
    }

    // Getters and setters
    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<String> getFitnessGoals() {
        return fitnessGoals;
    }

    public void setFitnessGoals(List<String> fitnessGoals) {
        this.fitnessGoals = fitnessGoals;
    }

    public List<String> getDietaryPreferences() {
        return dietaryPreferences;
    }

    public void setDietaryPreferences(List<String> dietaryPreferences) {
        this.dietaryPreferences = dietaryPreferences;
    }
}