package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlantBedTest {
    private PlantBed testBed;
    private ArrayList<Plant> listOfPlants;
    private Plant p1 = new Plant("Rose","Weekly","Bulb","Mature");
    private Plant p2 = new Plant("Carrot", "Weekly", "Vegetable", "Young");


    @BeforeEach
    public void runBefore() {
        listOfPlants = new ArrayList<>();
        listOfPlants.add(p1);
        listOfPlants.add(p2);
        testBed = new PlantBed("My Plant Bed", listOfPlants);
    }

    @Test
    public void constructorTest() {
        assertEquals("My Plant Bed", testBed.getName());
        assertEquals(p1,testBed.getPlants().get(0));
        assertEquals(p2,testBed.getPlants().get(1));
        assertEquals(2, testBed.getPlants().size());

    }

    //TODO: Add repeated adding and deletion of plants
    @Test
    public void addPlantTest() {
        Plant p3 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        testBed.addPlant(p3);
        assertEquals(3,testBed.getPlants().size());
        assertEquals(p3, testBed.getPlants().get(2));
    }

    @Test
    public void uprootPlantTest() {
        testBed.uprootPlant(0);
        assertEquals(1,testBed.getPlants().size());
        assertEquals(p2,testBed.getPlants().get(0));

    }

    @Test
    public void waterPlantTest() {
        assertTrue(testBed.waterPlant(1));
        assertFalse(testBed.getPlants().get(1).getDry());
    }








}
