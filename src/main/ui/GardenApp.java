package ui;

import model.Plant;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

/*
Class representing Garden with plant beds each with multiple plants
 */
public class GardenApp {
    private ArrayList<Plant> gardenPlants;
    private ArrayList<Plant> emptyList = new ArrayList<>();

    public GardenApp() {
        // stub
    }

    //
    public void getNumOfPlants() {
        // stub
    }

    //REQUIRES: option is = 0 OR if > 0 corresponding plant bed exists
    //EFFECTS: returns list of plants from specified plant bed
    //  0 = un-planted/potted
    //  1,2,3 = plant beds 1,2,3
    public ArrayList<Plant> checkPlantBed(int option) {
        return emptyList;
    }

    //EFFECTS: returns list of all plants in garden
    public ArrayList<Plant> getAllPlants() {
        return gardenPlants;
    }

}
