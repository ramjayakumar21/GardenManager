package persistence;

import model.Garden;
import model.Plant;
import model.PlantBed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WriterJsonTest {
    private Garden testGarden;

    @Test
    public void writerIncorrectFileTest() {
        try {
            WriterJson writer = new WriterJson("./data/test\0WriterFake.json");
            writer.open();
            fail();
        } catch (IOException e) {
            // do nothing
        }
    }

    @Test
    public void writerEmptyGardenTest() {
        try {
            ArrayList<Plant> plants = new ArrayList<>();
            PlantBed pb = new PlantBed("Bed 1",plants);
            ArrayList<PlantBed> plantBeds = new ArrayList<>();
            plantBeds.add(pb);
            testGarden = new Garden(plantBeds);

            WriterJson writer = new WriterJson("./data/testWriterEmptyGarden.json");
            writer.open();
            writer.writeToJson(testGarden);
            writer.close();

            ReaderJson reader = new ReaderJson("./data/testWriterEmptyGarden.json");
            testGarden = reader.readSource();
            assertEquals("Bed 1",testGarden.getPlantBedArrayList().get(0).getName());
            assertEquals(1,testGarden.getNumOfPlantBeds());
            assertEquals(0,testGarden.getPlantBedByIndex(0).getPlantArrayList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void writerGeneralGardenTest() {
        try {
            Plant p1 = new Plant("Rose","Weekly","Bulb","Mature");
            Plant p2 = new Plant("Carrot", "Weekly", "Vegetable", "Young");
            PlantBed bed1 = new PlantBed("Bed 1");
            bed1.addPlant(p1);
            bed1.addPlant(p2);
            PlantBed bed2 = new PlantBed("Bed 2");
            ArrayList<PlantBed> plantBeds = new ArrayList<>();
            plantBeds.add(bed1);
            plantBeds.add(bed2);
            testGarden = new Garden(plantBeds);

            WriterJson writer = new WriterJson("./data/testWriterGeneralGarden.json");
            writer.open();
            writer.writeToJson(testGarden);
            writer.close();

            ReaderJson reader = new ReaderJson("./data/testWriterGeneralGarden.json");
            testGarden = reader.readSource();
            assertEquals("Bed 1",testGarden.getPlantBedArrayList().get(0).getName());
            assertEquals("Bed 2",testGarden.getPlantBedArrayList().get(1).getName());
            assertEquals(2,testGarden.getPlantBedByIndex(0).getPlantArrayList().size());
            assertEquals(0,testGarden.getPlantBedByIndex(1).getPlantArrayList().size());
            assertEquals("Rose",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(0).getName());
            assertEquals("Bulb",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(0).getPlantType());
            assertEquals("Weekly",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(0).getWaterCycle());
            assertEquals("Mature",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(0).getLifeStage());
            assertEquals("Carrot",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(1).getName());
            assertEquals("Vegetable",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(1).getPlantType());
            assertEquals("Weekly",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(1).getWaterCycle());
            assertEquals("Young",testGarden.getPlantBedByIndex(0).getPlantArrayList().get(1).getLifeStage());
            assertEquals(2,testGarden.getNumOfPlantBeds());
            assertEquals(2,testGarden.getNumOfPlants());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
