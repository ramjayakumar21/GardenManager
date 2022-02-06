package model;

import java.util.ArrayList;

/*
Represents plant and given attributes
*/
public class Plant {
    private String name;
    private String lifeStage;
    private String waterCycle;
    private String plantType;
    private Boolean isDry;


    //REQUIRES: waterCycle should be one of:
    //              "Daily", "Every 2 Days", "Every 3 Days", "Weekly", "Monthly"
    //          plantType should be one of:
    //              "Perennial", "Biennial", "Cacti", "Bulb", "Shrub", "Fruit"
    //          age should be one of:
    //              "Seed", "Sprout", "Young", "Mature"
    public Plant(String name, String waterCycle, String plantType, String lifeStage) {
        this.name = name;
        this.waterCycle = waterCycle;
        this.plantType = plantType;
        this.lifeStage = lifeStage;
        isDry = true;
    }

    //MODIFIES: this
    //EFFECTS: changes isDry to false if true, returns true
    // if isDry is false, do nothing and return false
    public boolean water() {
        if (isDry) {
            isDry = false;
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getLifeStage() {
        return lifeStage;
    }

    public String getWaterCycle() {
        return waterCycle;
    }

    public String getPlantType() {
        return plantType;
    }

    public Boolean getDry() {
        return isDry;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLifeStage(String lifeStage) {
        this.lifeStage = lifeStage;
    }

    public void setWaterCycle(String waterCycle) {
        this.waterCycle = waterCycle;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public void setDry(Boolean dry) {
        isDry = dry;
    }
}
