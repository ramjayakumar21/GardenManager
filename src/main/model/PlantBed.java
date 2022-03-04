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
    }

    //MODIFIES: this
    //EFFECTS: adds given plant p to plant list
    public void addPlant(Plant p) {
        plantArrayList.add(p);
    }


    //MODIFIES: this
    //EFFECTS: if plant with index n is in plants, check if isDry = true
    //              if true, change to false and return true
    //         if false or in all other cases, return false
    public boolean waterPlant(int n) {
        if (0 <= n && (plantArrayList.size() - 1) >= n) {
            return plantArrayList.get(n).water();
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: if plant exists at index n, remove it and return true
    //         else return false
    public boolean uprootPlant(int n) {
        if (0 <= n && (plantArrayList.size() - 1) >= n) {
            plantArrayList.remove(n);
            return true;
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
