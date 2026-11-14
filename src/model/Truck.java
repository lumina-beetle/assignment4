/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * A truck in the Road Rage simulation. Trucks travel only on streets, lights, and
 * crosswalks. They randomly choose to go straight, turn left, or turn right, and turn
 * around only as a last resort. Trucks drive through all traffic lights without stopping,
 * stop for red crosswalk lights, and drive through yellow and green crosswalk lights.
 * A truck survives a collision with any vehicle, living or dead.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public class Truck extends AbstractVehicle
{
    /**
     * The death time of a truck. It is smaller than every other vehicle's death time, so a
     * truck never dies in a collision.
     */
    private static final int MY_DEATH_TIME = 1;

    /** The light states under which a truck may pass a crosswalk: green and yellow. */
    private static final Set<Light> MY_ALLOWED_CROSSWALK_LIGHTS = EnumSet.of(
            Light.GREEN, Light.YELLOW
    );

    /**
     * Creates a truck at the given position facing the given direction. The truck may pass
     * streets and traffic lights under any light state, and crosswalks only when the
     * crosswalk light is green or yellow (it stops for red).
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     */
    public Truck(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        addAllowedLandscape(Terrain.STREET, EnumSet.allOf(Light.class));
        addAllowedLandscape(Terrain.LIGHT, EnumSet.allOf(Light.class));
        addAllowedLandscape(Terrain.CROSSWALK, MY_ALLOWED_CROSSWALK_LIGHTS);
    }

    /**
     * Truck-specific behavior: randomly chooses to go straight, turn left, or turn right,
     * each with equal probability. As a last resort, if straight, left, and right are all
     * impassable terrain (not a street, light, or crosswalk), the truck turns around.
     *
     * @param theNeighbors the terrain surrounding this truck, keyed by direction
     * @return the direction this truck would like to move
     */
    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return isPreferredDirectionsBlocked(theNeighbors) ?
                getDirection().reverse() :
                randomNonReversingDirection();
    }

    /**
     * Determines whether this truck cannot travel straight, left, or right because none of
     * those neighboring terrains is one it can pass.
     *
     * @param theNeighbors the terrain surrounding this truck, keyed by direction
     * @return true if straight, left, and right are all impassable terrain, false otherwise
     */
    private boolean isPreferredDirectionsBlocked(Map<Direction, Terrain> theNeighbors)
    {
        return !getAllowedLandscape().containsKey(theNeighbors.get(getDirection())) &&
                !getAllowedLandscape().containsKey(theNeighbors.get(getDirection().left())) &&
                !getAllowedLandscape().containsKey(theNeighbors.get(getDirection().right()));
    }
}