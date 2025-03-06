package com.tm471a.intelligenthealthylifestyle.data.model;

public class MealPlan {
    private String mealName;
    private String calories;
    private String ingredients;

    public MealPlan(String mealName, String calories, String ingredients) {
        this.mealName = mealName;
        this.calories = calories;
        this.ingredients = ingredients;
    }

    public String getMealName() {
        return mealName;
    }

    public String getCalories() {
        return calories;
    }

    public String getIngredients() {
        return ingredients;
    }
}