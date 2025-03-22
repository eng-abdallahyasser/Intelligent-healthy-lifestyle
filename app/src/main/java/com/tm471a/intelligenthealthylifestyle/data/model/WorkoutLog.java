package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class WorkoutLog {
    private int count;
    private Timestamp date;

    public WorkoutLog() {}

    public WorkoutLog(int count) {
        this.count = count;
        this.date = Timestamp.now();
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("date", date);
        return map;
    }

    // Getters
    public int getCount() { return count; }
    public Timestamp getDate() { return date; }
}
