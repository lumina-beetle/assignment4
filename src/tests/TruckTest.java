package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest
{
    private Truck myTruckSubject;

    @BeforeEach
    void setUp()
    {
        myTruckSubject = new Truck(2, 2, Direction.NORTH);
    }

    @Test
    void truckLivesOnCollisionTest()
    {
        Taxi taxiSubject = new Taxi(2, 3, Direction.SOUTH);

        myTruckSubject.collide(taxiSubject);
        assertTrue(myTruckSubject.isAlive());
    }

    @Test
    void imageTest()
    {
        Truck deadTruckSubject = new Truck(2, 2, Direction.NORTH);
        ImmovableRod rod = new ImmovableRod(2, 3, Direction.NORTH);
        deadTruckSubject.collide(rod); // should kill the truck for tests purposes

        assertEquals("truck.gif", myTruckSubject.getImageFileName());
        assertEquals("truck_dead.gif", deadTruckSubject.getImageFileName());
    }

    @Test
    void canPassThroughValidTerrainTest()
    {
        Terrain[] validTerrain = { Terrain.STREET, Terrain.CROSSWALK };

        for(Terrain terrain : validTerrain)
        {
            assertTrue(myTruckSubject.canPass(terrain, Light.GREEN));
        }
    }

    @Test
    void canPassThroughStandardLightsTest()
    {
        Light[] validLights = { Light.GREEN, Light.YELLOW, Light.RED };

        for(Light light : validLights)
        {
            assertTrue(myTruckSubject.canPass(Terrain.STREET, light));
        }
    }

    @Test
    void stopsAtCrossWalkLightTest()
    {
        assertFalse(myTruckSubject.canPass(Terrain.CROSSWALK, Light.RED));
        assertTrue(myTruckSubject.canPass(Terrain.CROSSWALK, Light.YELLOW));
        assertTrue(myTruckSubject.canPass(Terrain.CROSSWALK, Light.GREEN));
    }

    @Test
    void chooseRandomDirectionTest()
    {

    }

    @Test
    void chooseBlockedDirectionTest()
    {
        Map<Direction, Terrain> neighbors = new HashMap<>();
        myTruckSubject.setDirection(Direction.NORTH);
        neighbors.put(Direction.NORTH, Terrain.GRASS);
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.WEST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.STREET);



        assertEquals(Direction.SOUTH, myTruckSubject.chooseDirection(neighbors));
    }
}