package model;

import java.util.ArrayList;

// class representing a plant bed with an arbitrary number of plants
public class PlantBed {
    private String name;
    private ArrayList<Plant> plantsList;

    //EFFECTS: makes plant bed with name and list of plants p
    public PlantBed(String n, ArrayList<Plant> p) {
        this.name = n;
        this.plantsList = p;
    }

    //EFFECTS: makes plant bed with name and empty plants
    public PlantBed(String name) {
        this.name = name;
        this.plantsList = new ArrayList<>();
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
        plantsList.add(p);
    }

    //MODIFIES: this
    //EFFECTS: adds given plant p to plant list
    public void addPlant(Plant p) {
        plantsList.add(p);
    }


    //MODIFIES: this
    //EFFECTS: if plant with index n is in plants, check if isDry = true
    //              if true, change to false and return true
    //         if false or in all other cases, return false
    public boolean waterPlant(int n) {
        if (0 <= n && (plantsList.size() - 1) >= n) {
            return plantsList.get(n).water();
        }
        return false;
    }

    //MODIFIES: this
    //EFFECTS: if plant exists at index n, remove it and return true
    //         else return false
    public boolean uprootPlant(int n) {
        if (0 <= n && (plantsList.size() - 1) >= n) {
            plantsList.remove(n);
            return true;
        }
        return false;
    }

    public ArrayList<Plant> getPlantsList() {
        return plantsList;
    }

    public String getName() {
        return name;
    }
}
