package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlantBedTest {
    private PlantBed testBed;
    private ArrayList<Plant> listOfPlants;
    private Plant p1;
    private Plant p2;
    private Plant p3;


    @BeforeEach
    public void runBefore() {
        listOfPlants = new ArrayList<>();
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
        assertEquals(p1,testBed.getPlantsList().get(0));
        assertEquals(p2,testBed.getPlantsList().get(1));
        assertEquals(p3,testBed.getPlantsList().get(2));
        assertEquals(3, testBed.getPlantsList().size());

    }

    //TODO: Add repeated adding and deletion of plants
    @Test
    public void addPlantTest() {
        Plant p4 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        testBed.addPlant(p4);
        assertEquals(4,testBed.getPlantsList().size());
        assertEquals(p3, testBed.getPlantsList().get(2));
    }

    @Test
    public void uprootPlantTest() {
        assertTrue(testBed.uprootPlant(0));
        assertEquals(2,testBed.getPlantsList().size());
        assertEquals(p2,testBed.getPlantsList().get(0));
        assertEquals(p3,testBed.getPlantsList().get(1));
        assertFalse(testBed.uprootPlant(3));
        assertFalse(testBed.uprootPlant(-1));
    }

    @Test
    public void addAndRemoveTest() {
        Plant p4 = new Plant("Day-lily", "Daily", "Perennial", "Sprout");
        testBed.addPlant(p4);
        assertTrue(testBed.uprootPlant(2));
        assertEquals(3,testBed.getPlantsList().size());
        assertEquals(p1,testBed.getPlantsList().get(0));
        assertEquals(p2,testBed.getPlantsList().get(1));
        assertEquals(p4,testBed.getPlantsList().get(2));
    }

    @Test
    public void waterPlantTest() {
        assertTrue(testBed.waterPlant(2));
        assertFalse(testBed.getPlantsList().get(2).getDry());
        assertFalse(testBed.waterPlant(2));
        assertFalse(testBed.waterPlant(5));
        assertFalse(testBed.waterPlant(-3));
    }








}
