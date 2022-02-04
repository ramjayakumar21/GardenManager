package model;

/*
Represents plant and given attributes
*/
public class Plant {
    private int daysSincePlanted;
    private String name;
    private String latinName;
    private int plantBed;
    // 0 = unassigned/potted, 1 = Plant Bed 1, 2 = Plant Bed 2


    public Plant() {
        // stub
    }

    //MODIFIES: this
    //EFFECTS: If isDry = true, change to false and return isDry
    // else return isDry
    public void water() {
        // stub
    }

    //REQUIRES: plantBed = 0
    //MODIFIES: this
    //EFFECTS: If isDry = true, change to false and return isDry
    // else return isDry
    public void uproot() {
        // stub
    }

    public int getDaysSincePlanted() {
        return 0;
    }

    public String getLatinName() {
        return "";
    }

}
