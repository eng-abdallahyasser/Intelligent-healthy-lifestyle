package com.tm471a.intelligenthealthylifestyle.data.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class MeasurementLog {
    private double chest;
    private double waist;
    private double hips;
    private Timestamp date;
    public MeasurementLog() {}  // Needed for Firestore

    public MeasurementLog(double chest, double waist, double hips) {
        this.chest = chest;
        this.waist = waist;
        this.hips = hips;
        this.date = Timestamp.now();
    }

    // Getters
    public double getChest() { return chest; }
    public double getWaist() { return waist; }
    public double getHips() { return hips; }
    public Timestamp getDate() { return date; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("chest", chest);
        map.put("waist", waist);
        map.put("hips", hips);
        map.put("date", date);
        return map;
    }
}