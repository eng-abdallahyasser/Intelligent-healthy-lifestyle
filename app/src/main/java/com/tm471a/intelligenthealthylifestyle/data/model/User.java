package com.tm471a.intelligenthealthylifestyle.data.model;

import androidx.annotation.NonNull;

import java.util.List;

public class User {
    private String uid;
    private String name;
    private String email;
    private Boolean genderIsMale;
    private String  currentFitnessLevel;
    private String medicalConditionsOrInjuries;
    private double height;
    private double weight;
    private int age;
    private List<String> fitnessGoals;
    private List<String> dietaryPreferences;
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", age=" + age +
                ", fitnessGoals=" + fitnessGoals +
                ", dietaryPreferences=" + dietaryPreferences +
                ", genderIsMale=" + genderIsMale +
                ", currentFitnessLevel='" + currentFitnessLevel + '\'' +
                ", medicalConditionsOrInjuries='" + medicalConditionsOrInjuries + '\'' +
                '}';
    }

    public User() {
        this.uid = "uid";
        this.name = "name";
        this.email = "email";
        this.height = 100;
        this.weight = 100;
        this.currentFitnessLevel = "currentFitnessLevel";
        this.medicalConditionsOrInjuries = "medicalConditionsOrInjuries";
        this.genderIsMale = true;
        this.fitnessGoals = List.of("fitnessGoals");
        this.dietaryPreferences = List.of("dietaryPreferences");
        this.age = 20;
    }

    public User(String uid, String name, String email, double height, double weight,
                List<String> fitnessGoals, List<String> dietaryPreferences, int age, Boolean genderIsMale, String currentFitnessLevel, String medicalConditionsOrInjuries) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.height = height;
        this.weight = weight;
        this.fitnessGoals = fitnessGoals;
        this.dietaryPreferences = dietaryPreferences;
        this.age = age;
        this.genderIsMale = genderIsMale;
        this.currentFitnessLevel = currentFitnessLevel;
        this.medicalConditionsOrInjuries = medicalConditionsOrInjuries;
    }

    // Getters and setters
    @NonNull
    public String getUid() {
        return uid;
    }

    public Boolean getGenderIsMale() {
        return genderIsMale;
    }

    public void setGenderIsMale(Boolean genderIsMale) {
        this.genderIsMale = genderIsMale;
    }

    public String getCurrentFitnessLevel() {
        return currentFitnessLevel;
    }

    public void setCurrentFitnessLevel(String currentFitnessLevel) {
        this.currentFitnessLevel = currentFitnessLevel;
    }

    public String getMedicalConditionsOrInjuries() {
        return medicalConditionsOrInjuries;
    }

    public void setMedicalConditionsOrInjuries(String medicalConditionsOrInjuries) {
        this.medicalConditionsOrInjuries = medicalConditionsOrInjuries;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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