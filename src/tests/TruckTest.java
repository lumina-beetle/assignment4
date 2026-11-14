/*
 * TCSS 305 - Road Rage
 */

package tests;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Truck} class, covering collision survival, image file names,
 * the terrain and lights a truck can pass, and direction choice.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
class TruckTest
{
    /** The truck under test, recreated before each test. */
    private Truck myTruckSubject;

    /**
     * Creates a fresh truck at (2, 2) facing north before each test.
     */
    @BeforeEach
    void setUp()
    {
        myTruckSubject = new Truck(2, 2, Direction.NORTH);
    }

    /**
     * Tests that a truck stays alive after colliding with a taxi.
     */
    @Test
    void truckLivesOnCollisionTest()
    {
        Taxi taxiSubject = new Taxi(2, 3, Direction.SOUTH);

        myTruckSubject.collide(taxiSubject);
        assertTrue(myTruckSubject.isAlive());
    }

    /**
     * Tests that a living truck uses the "truck.gif" image and a dead truck uses the
     * "truck_dead.gif" image. A collision with an immovable rod is used to kill the truck
     * for the purposes of the test.
     */
    @Test
    void imageTest()
    {
        Truck deadTruckSubject = new Truck(2, 2, Direction.NORTH);
        ImmovableRod rod = new ImmovableRod(2, 3, Direction.NORTH);
        deadTruckSubject.collide(rod); // should kill the truck for tests purposes

        assertEquals("truck.gif", myTruckSubject.getImageFileName());
        assertEquals("truck_dead.gif", deadTruckSubject.getImageFileName());
    }

    /**
     * Tests that a truck can pass streets and crosswalks when the light is green.
     */
    @Test
    void canPassThroughValidTerrainTest()
    {
        Terrain[] validTerrain = { Terrain.STREET, Terrain.CROSSWALK };

        for(Terrain terrain : validTerrain)
        {
            assertTrue(myTruckSubject.canPass(terrain, Light.GREEN));
        }
    }

    /**
     * Tests that a truck can pass a street under every light state.
     */
    @Test
    void canPassThroughStandardLightsTest()
    {
        Light[] validLights = { Light.GREEN, Light.YELLOW, Light.RED };

        for(Light light : validLights)
        {
            assertTrue(myTruckSubject.canPass(Terrain.STREET, light));
        }
    }

    /**
     * Tests that a truck stops for a red crosswalk light but passes a yellow or green one.
     */
    @Test
    void stopsAtCrossWalkLightTest()
    {
        assertFalse(myTruckSubject.canPass(Terrain.CROSSWALK, Light.RED));
        assertTrue(myTruckSubject.canPass(Terrain.CROSSWALK, Light.YELLOW));
        assertTrue(myTruckSubject.canPass(Terrain.CROSSWALK, Light.GREEN));
    }

    /**
     * Tests that a truck turns around when straight, left, and right are all grass and
     * only the street behind it is passable.
     */
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