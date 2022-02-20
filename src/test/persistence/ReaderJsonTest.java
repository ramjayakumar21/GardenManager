package persistence;

import model.Garden;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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




}
