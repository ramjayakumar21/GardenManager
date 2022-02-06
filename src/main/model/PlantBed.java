package model;

import java.util.ArrayList;
import java.util.HashSet;

// class representing a plant bed with an arbitrary number of plants
public class PlantBed {
    private String name;
    private ArrayList<Plant> plants;

    public PlantBed(String name, ArrayList<Plant> plants) {
        this.name = name;
        this.plants = plants;
    }

    public PlantBed(String name) {
        this.name = name;
        this.plants = new ArrayList<>();
    }

    public PlantBed() {
        this.name = "Unnamed Plant Bed";
        this.plants = new ArrayList<>();
    }

    //REQUIRES: waterCycle should be one of:
    //              "Daily", "Every 2 Days", "Every 3 Days", "Weekly", "Monthly"
    //          plantType should be one of:
    //              "Perennial", "Biennial", "Cacti", "Bulb", "Shrub", "Fruit"
    //          age should be one of:
    //              "Seed", "Sprout", "Young", "Mature"
    //MODIFIES: this
    //EFFECTS: calls plants constructor with given argument and adds it to plants
    public void addPlant(String name, String waterCycle, String plantType, String life) {
        Plant p = new Plant(name, waterCycle, plantType, life);
        plants.add(p);
    }

    public void addPlant(Plant p) {
        plants.add(p);
    }


    //MODIFIES: if plant with index n is in plants, check if isDry = true
    //          if true, change to false and return true
    //          if false or in all other cases, return false
    public boolean waterPlant(int n) {
        if (0 <= n && (plants.size() - 1) >= n) {
            return plants.get(n).water();
        }
        return false;
    }

    //REQUIRES: plant with index n is in plants
    //MODIFIES: if plant exists at index n, remove it and return true
    //          else return false
    public boolean uprootPlant(int n) {
        if (0 <= n && (plants.size() - 1) >= n) {
            plants.remove(n);
            return true;
        }
        return false;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public String getName() {
        return name;
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
    }

    public void setName(String name) {
        this.name = name;
    }
}
