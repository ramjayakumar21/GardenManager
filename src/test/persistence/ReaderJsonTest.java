package persistence;

import model.Garden;
import model.PlantBed;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderJsonTest {

    @Test
    public void readerMissingFileTest() {
        ReaderJson reader = new ReaderJson("./data/testReaderFakeGarden.json");
        try {
            Garden g = reader.readSource();
            fail("Was able to find a nonexistent file!");
        } catch (IOException e) {
            // do nothing
        }
    }

    @Test
    public void readerEmptyFileTest() {
        ReaderJson reader = new ReaderJson("./data/testReaderEmptyGarden.json");
        try {
            Garden g = reader.readSource();
            assertTrue(g.getPlantBedArrayList().isEmpty());
        } catch (IOException e) {
            fail("Failed to read from empty file!");
        }
    }

    @Test public void readerGeneralFileTest() {
        ReaderJson reader = new ReaderJson("./data/testReaderGeneralGarden.json");
        try {
            Garden g = reader.readSource();
            assertEquals(1,g.getPlantBedArrayList().size());
            PlantBed pb = g.getPlantBedByIndex(0);
            assertEquals("Plant Bed 1",pb.getName());
            assertEquals("Rose",pb.getPlantArrayList().get(0).getName());
            assertEquals("Mature",pb.getPlantArrayList().get(0).getLifeStage());
            assertEquals("Weekly",pb.getPlantArrayList().get(0).getWaterCycle());
            assertEquals("Bulb",pb.getPlantArrayList().get(0).getPlantType());
            assertEquals(true,pb.getPlantArrayList().get(0).getDry());
            assertEquals("Carrot",pb.getPlantArrayList().get(1).getName());
            assertEquals("Young",pb.getPlantArrayList().get(1).getLifeStage());
            assertEquals("Weekly",pb.getPlantArrayList().get(1).getWaterCycle());
            assertEquals("Vegetable",pb.getPlantArrayList().get(1).getPlantType());
            assertEquals(false,pb.getPlantArrayList().get(1).getDry());
        } catch (IOException e) {
            fail("Failed to read from empty file!");
        }

    }




}
