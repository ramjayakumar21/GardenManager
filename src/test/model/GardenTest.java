package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class GardenTest {
    Garden testGarden;
    private Plant p1;
    private Plant p2;
    private Plant p3;
    private PlantBed bed1;
    private PlantBed bed2;

    @BeforeEach
    public void runBefore() {
        p1 = new Plant("Rose","Weekly","Bulb","Mature");
        p2 = new Plant("Carrot", "Weekly", "Vegetable", "Young");
        p3 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        bed1 = new PlantBed("Bed 1");
        bed1.addPlant(p1);
        bed1.addPlant(p2);
        bed2 = new PlantBed("Bed 2");
        bed2.addPlant(p3);
        ArrayList<PlantBed> testPlantBeds = new ArrayList<>();
        testPlantBeds.add(bed1);
        testPlantBeds.add(bed2);
        testGarden = new Garden(testPlantBeds);
    }

    @Test
    public void constructorTest() {
        assertEquals(2,testGarden.getPlantBedArrayList().get(0).getPlantArrayList().size());
        assertEquals("Bed 1",testGarden.getPlantBedArrayList().get(0).getName());
        assertEquals(1,testGarden.getPlantBedArrayList().get(1).getPlantArrayList().size());
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
    public void getNumOfPlantBedsTest() {
        assertEquals(3,testGarden.getNumOfPlants());
        assertTrue(testGarden.getPlantBedByIndex(0).uprootPlant(p1));
        assertEquals(2,testGarden.getNumOfPlants());
    }

    @Test
    public void selectPlantBedTest() {
        PlantBed bedCopy = testGarden.getPlantBedByIndex(0);
        assertEquals("Bed 1", bedCopy.getName());
        assertEquals(bedCopy, testGarden.getPlantBedByIndex(0));
    }

    @Test
    public void removePlantBedTest() {
        PlantBed pb1 = new PlantBed("Fruit Plants");
        testGarden.addPlantBed(pb1);
        assertEquals(3,testGarden.getNumOfPlantBeds());
        assertTrue(testGarden.removePlantBed(pb1));
        assertEquals(2,testGarden.getPlantBedArrayList().size());
        assertTrue(testGarden.removePlantBed(bed1));
        assertEquals(1,testGarden.getPlantBedArrayList().size());
        assertFalse(testGarden.removePlantBed(new PlantBed("BEd 1")));
    }

}
