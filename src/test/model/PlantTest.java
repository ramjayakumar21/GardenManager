package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlantTest {
    private Plant p;

    @BeforeEach
    public void runBefore() {
        p = new Plant("Rose","Weekly","Bulb","Mature");
    }

    @Test
    public void constructorTest() {
        assertEquals("Rose", p.getName());
        assertEquals("Weekly", p.getWaterCycle());
        assertEquals("Bulb", p.getPlantType());
        assertEquals("Mature",p.getLifeStage());
        assertTrue(p.getDry());
    }

    @Test
    public void waterTest() {
        assertTrue(p.getDry());
        p.water();
        assertFalse(p.getDry());
        p.water();
        assertFalse(p.getDry());
    }


}