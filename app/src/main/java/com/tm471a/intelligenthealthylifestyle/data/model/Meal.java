package com.tm471a.intelligenthealthylifestyle.data.model;

import java.util.List;

public class Meal {
    private String name;
    private List<String> ingredients;
    private int calories;

    public Meal() {
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
