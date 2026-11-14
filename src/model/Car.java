/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * A car in the Road Rage simulation. Cars travel only on streets, lights, and crosswalks.
 * They prefer to drive straight ahead, then turn left, then turn right, and turn around
 * only as a last resort. Cars stop for red lights and for red or yellow crosswalk lights.
 * A car dies if it collides with a living truck and stays dead for 15 updates.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public class Car extends AbstractVehicle
{
    /** The number of updates a car stays dead after dying. */
    private static final int MY_DEATH_TIME = 15;

    /** The light states under which a car may pass a street: all of them. */
    private static final Set<Light> MY_ALLOWED_STREET_LIGHTS = EnumSet.of(
            Light.GREEN,
            Light.YELLOW,
            Light.RED
    );

    /** The light states under which a car may pass a traffic light: green and yellow. */
    private static final Set<Light> MY_ALLOWED_LIGHTS =  EnumSet.of(
            Light.GREEN,
            Light.YELLOW
    );

    /**
     * Creates a car at the given position facing the given direction. The car may pass
     * streets under any light state, traffic lights when the light is green or yellow
     * (it stops for red), and crosswalks only when the light is green (it stops for
     * yellow and red).
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     */
    public Car(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        // can only pass on streets, crosswalks, and lights
        addAllowedLandscape(Terrain.STREET, MY_ALLOWED_STREET_LIGHTS);
        addAllowedLandscape(Terrain.CROSSWALK, Light.GREEN);
        addAllowedLandscape(Terrain.LIGHT, MY_ALLOWED_LIGHTS);
    }

    /**
     * Car-specific behavior: prefers to go straight ahead if that terrain is a street,
     * light, or crosswalk. If not, it turns left if possible, then right if possible,
     * and as a last resort turns around. A car facing a light or crosswalk still chooses
     * to go straight and relies on canPass to hold it in place when the light is red
     * (or yellow, for crosswalks); it never turns to avoid a light.
     *
     * @param theNeighbors the terrain surrounding this car, keyed by direction
     * @return the direction this car would like to move
     */
    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection())) ?
                getDirection() :
                tryTurnLeft(theNeighbors);
    }

    /**
     * Chooses left if the terrain in that direction is one this car can travel on;
     * otherwise defers to {@link #tryTurnRight(Map)}.
     *
     * @param theNeighbors the terrain surrounding this car, keyed by direction
     * @return the left direction if passable terrain is there, else the result of trying right
     */
    private Direction tryTurnLeft(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().left())) ?
                getDirection().left() :
                tryTurnRight(theNeighbors);
    }

    /**
     * Chooses right if the terrain in that direction is one this car can travel on;
     * otherwise turns around as a last resort.
     *
     * @param theNeighbors the terrain surrounding this car, keyed by direction
     * @return the right direction if passable terrain is there, else the reverse direction
     */
    private Direction tryTurnRight(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().right())) ?
                getDirection().right() :
                getDirection().reverse();
    }
}