package model;


import org.json.JSONObject;
import persistence.Writable;

// represents plant and given attributes it can have
public class Plant implements Writable {
    public static final String[] PLANT_TYPES = {
            "Perennial", "Biennial", "Cacti", "Bulb", "Shrub", "Fruit", "Vegetable"
    };
    public static final String[] LIFE_STAGE = {
            "Seed", "Sprout", "Young", "Mature"
    };
    public static final String[] WATER_CYCLE = {
            "Daily", "Every 2 Days", "Every 3 Days", "Weekly", "Monthly"
    };
    private String name;
    private String lifeStage;
    private String waterCycle;
    private String plantType;
    private Boolean isDry;



    //REQUIRES: waterCycle should be one of:
    //              "Daily", "Every 2 Days", "Every 3 Days", "Weekly", "Monthly"
    //          plantType should be one of:
    //              "Perennial", "Biennial", "Cacti", "Bulb", "Shrub", "Fruit", "Vegetable"
    //          lifeStage should be one of:
    //              "Seed", "Sprout", "Young", "Mature"
    //EFFECTS: creates new plant with name, waterCycle, plantType, lifeStage, and isDry as true
    public Plant(String name, String waterCycle, String plantType, String lifeStage) {
        this.name = name;
        this.waterCycle = waterCycle;
        this.plantType = plantType;
        this.lifeStage = lifeStage;
        isDry = true;
    }

    //MODIFIES: this
    //EFFECTS: changes isDry to false if true, returns true
    //         if isDry is false, do nothing and return false
    public boolean water() {
        if (isDry) {
            isDry = false;
            return true;
        }
        return false;
    }

    //EFFECTS: returns plant name as string
    public String getName() {
        return name;
    }

    //EFFECTS: returns plant life stage as string
    public String getLifeStage() {
        return lifeStage;
    }

    //EFFECTS: returns plant watering cycle as string
    public String getWaterCycle() {
        return waterCycle;
    }

    //EFFECTS: returns plant type as string
    public String getPlantType() {
        return plantType;
    }

    //EFFECTS: returns true if plant is dry, false if it isn't
    public Boolean getDry() {
        return isDry;
    }

    //EFFECTS: sets isDry to b
    public void setDry(Boolean b) {
        this.isDry = b;
    }

    //EFFECTS: converts plant into JSONObject and returns it
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("lifeStage", lifeStage);
        jsonObject.put("waterCycle", waterCycle);
        jsonObject.put("plantType", plantType);
        jsonObject.put("isDry", isDry);
        return jsonObject;
    }
}
