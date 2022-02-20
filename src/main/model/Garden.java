package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// class representing a garden with plant beds, each with multiple plants
public class Garden implements Writable {
    private ArrayList<PlantBed> plantBedArrayList;

    //EFFECTS: creates new garden with plant beds pb
    public Garden(ArrayList<PlantBed> pb) {
        plantBedArrayList = pb;
    }

    //REQUIRES: x should be in range [0, (# of plant beds - 1)]
    //EFFECT: returns plant bed with index i in plant bed array list
    public PlantBed getPlantBedByIndex(int x) {
        return plantBedArrayList.get(x);
    }

    //MODIFIES: this
    //EFFECTS: adds given PlantBed p to plantBedArrayList
    public void addPlantBed(PlantBed pb) {
        plantBedArrayList.add(pb);
    }

    //MODIFIES: this
    //EFFECT: if plant bed with index i exists, removes it and returns true
    //        else return false
    public boolean removePlantBed(int x) {
        if (x >= 0 && x <= (plantBedArrayList.size() - 1)) {
            plantBedArrayList.remove(x);
            return true;
        }
        return false;
    }

    //EFFECTS: returns total number of plants in garden
    public int getNumOfPlants() {
        int total = 0;
        for (PlantBed pb: plantBedArrayList) {
            total += pb.getPlantArrayList().size();
        }
        return total;
    }

    //EFFECTS: returns total number of plant beds in garden
    public int getNumOfPlantBeds() {
        return plantBedArrayList.size();
    }

    public ArrayList<PlantBed> getPlantBedArrayList() {
        return plantBedArrayList;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("plantBedArrayList", plantBedsToJson());
        return jsonObject;
    }

    private JSONArray plantBedsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (PlantBed pb : plantBedArrayList) {
            jsonArray.put(pb.toJson());
        }

        return jsonArray;
    }
}
