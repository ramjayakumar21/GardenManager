package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PlantBedTest {
    private PlantBed testBed;
    private Plant p1;
    private Plant p2;
    private Plant p3;


    @BeforeEach
    public void runBefore() {
        ArrayList<Plant> listOfPlants = new ArrayList<>();
        p1 = new Plant("Rose","Weekly","Bulb","Mature");
        p2 = new Plant("Carrot", "Weekly", "Vegetable", "Young");
        p3 = new Plant("Tulip", "Daily", "Biennial", "Seed");
        listOfPlants.add(p1);
        listOfPlants.add(p2);
        listOfPlants.add(p3);
        testBed = new PlantBed("My Plant Bed", listOfPlants);
    }

    @Test
    public void constructorTest() {
        assertEquals("My Plant Bed", testBed.getName());
        assertEquals(p1,testBed.getPlantArrayList().get(0));
        assertEquals(p2,testBed.getPlantArrayList().get(1));
        assertEquals(p3,testBed.getPlantArrayList().get(2));
        assertEquals(3, testBed.getPlantArrayList().size());
    }

    @Test
    public void addPlantTest() {
        Plant p4 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        testBed.addPlant(p4);
        assertEquals(4,testBed.getPlantArrayList().size());
        assertEquals(p3, testBed.getPlantArrayList().get(2));
    }

    @Test
    public void uprootPlantTest() {
        assertTrue(testBed.uprootPlant(p1));
        assertEquals(2,testBed.getPlantArrayList().size());
        assertEquals(p2,testBed.getPlantArrayList().get(0));
        assertEquals(p3,testBed.getPlantArrayList().get(1));
        assertFalse(testBed.uprootPlant(new Plant("fake", "plant", "2", "test")));
    }

    @Test
    public void addAndRemoveTest() {
        Plant p4 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        testBed.addPlant(p4);
        assertTrue(testBed.uprootPlant(p3));
        assertEquals(3,testBed.getPlantArrayList().size());
        assertEquals(p1,testBed.getPlantArrayList().get(0));
        assertEquals(p2,testBed.getPlantArrayList().get(1));
        assertEquals(p4,testBed.getPlantArrayList().get(2));
    }

    @Test
    public void waterPlantTest() {
        assertTrue(testBed.waterPlant(p3));
        assertFalse(testBed.getPlantArrayList().get(2).getDry());
        assertFalse(testBed.waterPlant(
                new Plant("Tulip", "Daily", "Biennial", "Mature")));
    }








}
