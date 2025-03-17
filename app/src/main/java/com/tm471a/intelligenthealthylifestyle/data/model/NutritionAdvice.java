package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NutritionAdvice {
    @SerializedName("exercise_name")
    private String exerciseName;
    private String description;
    private String goal;
    @SerializedName("calories_per_day")
    private int caloriesPerDay;
    private Macronutrients macronutrients;
    @SerializedName("meal_suggestions")
    private List<Suggestion> mealSuggestions;
    @SerializedName("seo_title")
    private String seoTitle;
    @SerializedName("seo_content")
    private String seoContent;
    @SerializedName("seo_keywords")
    private String seoKeywords;

    public NutritionAdvice(){

    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoContent() {
        return seoContent;
    }

    public void setSeoContent(String seoContent) {
        this.seoContent = seoContent;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public List<Suggestion> getMealSuggestions() {
        return mealSuggestions;
    }

    public void setMealSuggestions(List<Suggestion> mealSuggestions) {
        this.mealSuggestions = mealSuggestions;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Macronutrients getMacronutrients() {
        return macronutrients;
    }

    public void setMacronutrients(Macronutrients macronutrients) {
        this.macronutrients = macronutrients;
    }
}
