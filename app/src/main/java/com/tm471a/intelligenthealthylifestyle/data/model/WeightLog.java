package com.tm471a.intelligenthealthylifestyle.data.model;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class WeightLog {
    private double weight;
    private Timestamp date;

    public WeightLog() {}

    public WeightLog(double weight) {
        this.weight = weight;
        this.date = Timestamp.now();
    }

    // Getters
    public double getWeight() { return weight; }
    public Timestamp getDate() { return date; }

    // For Firestore serialization
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("weight", weight);
        map.put("date", date);
        return map;
    }
}