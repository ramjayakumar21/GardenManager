package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GardenTest {
    Garden testGarden;
    PlantBed bed1;

    //TODO: Fix tests to work better
    @BeforeEach
    public void runBefore() {
        Plant p1 = new Plant("Rose","Weekly","Bulb","Mature");
        Plant p2 = new Plant("Carrot", "Weekly", "Vegetable", "Young");
        Plant p3 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        bed1 = new PlantBed("Bed 1");
        bed1.addPlant(p1);
        bed1.addPlant(p2);
        PlantBed bed2 = new PlantBed("Bed 2");
        bed2.addPlant(p3);
        ArrayList<PlantBed> testPlantBeds = new ArrayList<>();
        testPlantBeds.add(bed1);
        testPlantBeds.add(bed2);
        testGarden = new Garden(testPlantBeds);
    }

    @Test
    public void constructorTest() {
        assertEquals(2,testGarden.getPlantBedArrayList().get(0).getPlants().size());
        assertEquals("Bed 1",testGarden.getPlantBedArrayList().get(0).getName());
        assertEquals(1,testGarden.getPlantBedArrayList().get(1).getPlants().size());
        assertEquals("Bed 2",testGarden.getPlantBedArrayList().get(1).getName());
    }

    @Test
    public void getNumOfPlantsTest() {
        PlantBed pb1 = new PlantBed("Fruit Plants");
        pb1.addPlant("Rose","Weekly","Bulb","Mature");
        pb1.addPlant("Carrot", "Weekly", "Vegetable", "Young");
        testGarden.addPlantBed(pb1);
        testGarden.getPlantBedArrayList().get(0).addPlant("Day-lily", "Daily", "Perennial", "Sprout");
        assertEquals(6, testGarden.getNumOfPlants());
    }

    @Test
    public void selectPlantBedTest() {
        assertEquals(bed1,testGarden.selectPlantBed(0));
    }

    @Test
    public void removePlantBedTest() {
        assertTrue(testGarden.removePlantBed(1));
        assertEquals(1,testGarden.getPlantBedArrayList().size());
        assertFalse(testGarden.removePlantBed(1));
    }

}
