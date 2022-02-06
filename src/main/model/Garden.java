package model;

import java.util.ArrayList;

/*
Class representing Garden with plant beds each with multiple plants
 */
public class Garden {
    private ArrayList<PlantBed> plantBeds;

    public Garden() {
        plantBeds = new ArrayList<>();
        PlantBed p = new PlantBed("Bed 1");
    }

    public Garden(ArrayList<PlantBed> pb) {
        plantBeds = pb;
    }

    public void addPlantBed(PlantBed p) {
        return;
    }

    //REQUIRES: option should be in range [0, number of plantBeds - 1]
    //EFFECT: selects plantBed to modify
    public PlantBed selectPlantBed(int option) {
        return new PlantBed();
    }

    //EFFECT: if plant bed with name pb exists, removes it and returns true
    //        else return false
    public boolean removePlantBed(String pb) {
        return false;
    }

    public ArrayList<PlantBed> getPlantBeds() {
        return plantBeds;
    }


    //EFFECTS: returns total number of plants in garden
    public int getNumOfPlants() {
        return 0;
    }

}
