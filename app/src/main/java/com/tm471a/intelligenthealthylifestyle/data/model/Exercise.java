package com.tm471a.intelligenthealthylifestyle.data.model;

public class Exercise {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryMuscle() {
        return primaryMuscle;
    }

    public void setPrimaryMuscle(String primaryMuscle) {
        this.primaryMuscle = primaryMuscle;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    private String name;
    private String primaryMuscle;
    private String equipment;
}
