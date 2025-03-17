package com.tm471a.intelligenthealthylifestyle.data.model;

import java.util.List;

public class Suggestion {
    private String meal;
    private List<Meal> suggestions;

    public Suggestion(){

    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public List<Meal> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Meal> suggestions) {
        this.suggestions = suggestions;
    }
}
