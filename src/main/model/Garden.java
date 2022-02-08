package model;

import java.util.ArrayList;

/*
Class representing Garden with plant beds each with multiple plants
 */
public class Garden {
    private ArrayList<PlantBed> plantBedArrayList;


    public Garden(ArrayList<PlantBed> pb) {
        plantBedArrayList = pb;
    }

    //EFFECTS: adds given PlantBed p to plantBedArrayList
    public void addPlantBed(PlantBed p) {
        plantBedArrayList.add(p);
    }

    //REQUIRES: i should be in range [0, number of plantBeds - 1]
    //EFFECT: selects plantBed to modify
    public PlantBed selectPlantBed(int i) {
        return plantBedArrayList.get(i);
    }

    //EFFECT: if plant bed with index i exists, removes it and returns true
    //        else return false
    public boolean removePlantBed(int i) {
        if (i >= 0 && i <= (plantBedArrayList.size() - 1)) {
            plantBedArrayList.remove(i);
            return true;
        }
        return false;
    }

    public ArrayList<PlantBed> getPlantBedArrayList() {
        return plantBedArrayList;
    }


    //EFFECTS: returns total number of plants in garden
    public int getNumOfPlants() {
        int total = 0;
        for (PlantBed pb: plantBedArrayList) {
            for (Plant p: pb.getPlants()) {
                total += 1;
            }
        }
        return total;
    }

    //TODO: ADD tests for this
    //EFFECTS: returns total number of plant beds in garden
    public int getNumOfPlantBeds() {
        int total = 0;
        for (PlantBed pb: plantBedArrayList) {
            total += 1;
        }
        return total;
    }

}
