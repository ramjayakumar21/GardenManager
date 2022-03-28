package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// class representing a plant bed with an arbitrary number of plants
public class PlantBed implements Writable {
    private String name;
    private ArrayList<Plant> plantArrayList;

    //EFFECTS: makes plant bed with name and list of plants p
    public PlantBed(String n, ArrayList<Plant> p) {
        this.name = n;
        this.plantArrayList = p;
    }

    //EFFECTS: makes plant bed with name and empty plants
    public PlantBed(String name) {
        this.name = name;
        this.plantArrayList = new ArrayList<>();
    }

    //REQUIRES: waterCycle should be one of:
    //              "Daily", "Every 2 Days", "Every 3 Days", "Weekly", "Monthly"
    //          plantType should be one of:
    //              "Perennial", "Biennial", "Cacti", "Bulb", "Shrub", "Fruit"
    //          age should be one of:
    //              "Seed", "Sprout", "Young", "Mature"
    //MODIFIES: this
    //EFFECTS: calls plants constructor with given argument and adds it to plants list
    public void addPlant(String name, String waterCycle, String plantType, String life) {
        Plant p = new Plant(name, waterCycle, plantType, life);
        plantArrayList.add(p);
        EventLog.getInstance().logEvent(new Event("Added plant " + name + " to plant bed "
                + this.name + "."));
    }

    //MODIFIES: this
    //EFFECTS: adds given plant p to plant list
    public void addPlant(Plant p) {
        plantArrayList.add(p);
        EventLog.getInstance().logEvent(new
                Event("Added plant " + p.getName() + " to plant bed " + this.name + "."));
    }


    //MODIFIES: this
    //EFFECTS: if plant with is contained in plantArrayList, check if isDry = true
    //              if true, change to false and return true
    //         if false or in all other cases, return false
    public boolean waterPlant(Plant p) {
        if (plantArrayList.contains(p)) {
            EventLog.getInstance().logEvent(new Event("Watered plant "
                    + p.getName() + " in plant bed " + this.name  + "."));
            return p.water();
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: if plant is contained in plantArrayList, remove it and return true
    //         else return false
    public boolean uprootPlant(Plant p) {
        if (plantArrayList.contains(p)) {
            EventLog.getInstance().logEvent(new Event("Removed plant "
                    + p.getName() + " from plant bed " + this.name  + "."));
            return plantArrayList.remove(p);
        }
        return false;
    }

    //EFFECTS: returns arraylist of plants in plant bed
    public ArrayList<Plant> getPlantArrayList() {
        return plantArrayList;
    }

    //EFFECTS: returns name of plant bed as string
    public String getName() {
        return name;
    }

    //EFFECTS: converts plant bed into JSONObject and returns it
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("plantArrayList", plantsToJson());
        return jsonObject;
    }

    //EFFECTS: converts all plants in plantArrayList into JSONObjects,
    //         then adds them to a JSONArray and returns the array
    private JSONArray plantsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Plant p: plantArrayList) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }


}
