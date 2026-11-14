/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * A taxi in the Road Rage simulation. Taxis travel only on streets, lights, and
 * crosswalks. They prefer to drive straight ahead, then turn left, then turn right, and
 * turn around only as a last resort. Taxis stop for red lights, and stop for a red
 * crosswalk light for at most 3 clock cycles before proceeding. They drive through yellow
 * and green crosswalk lights. A taxi dies if it collides with a living truck and stays
 * dead for 15 updates.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public class Taxi extends AbstractVehicle
{
    /** The number of updates a taxi stays dead after dying. */
    private static final int MY_DEATH_TIME = 15;

    /** The maximum number of clock cycles a taxi waits at a red crosswalk light. */
    private static final int MY_MAX_LIGHT_WAIT_TIME = 3;

    /** The light states under which a taxi may pass a street: all of them. */
    private static final Set<Light> MY_ALLOWED_STREET_LIGHTS = EnumSet.of(
            Light.GREEN,
            Light.YELLOW,
            Light.RED
    );

    /** The light states under which a taxi may pass a crosswalk without waiting. */
    private static final Set<Light> MY_ALLOWED_CROSSWALK_LIGHTS =  EnumSet.of(
            Light.GREEN
    );

    /** The light states under which a taxi may pass a traffic light: green and yellow. */
    private static final Set<Light> MY_ALLOWED_LIGHTS =  EnumSet.of(
            Light.GREEN,
            Light.YELLOW
    );

    /** The number of consecutive clock cycles this taxi has waited at a crosswalk light. */
    private int MY_WAIT_TIME = 0;

    /**
     * Creates a taxi at the given position facing the given direction. The taxi may pass
     * streets under any light state, traffic lights when the light is green or yellow
     * (it stops for red), and crosswalks when the light is allowed by this taxi's
     * crosswalk rules (see {@link #canPass(Terrain, Light)}).
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     */
    public Taxi(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);
        // can only pass on streets, crosswalks, and likes
        addAllowedLandscape(Terrain.STREET, MY_ALLOWED_STREET_LIGHTS);
        addAllowedLandscape(Terrain.CROSSWALK, MY_ALLOWED_CROSSWALK_LIGHTS);
        addAllowedLandscape(Terrain.LIGHT, MY_ALLOWED_LIGHTS);
    }

    /**
     * Taxi-specific behavior: a taxi can pass streets under any light state, traffic
     * lights when green or yellow, and crosswalks when the light is green or yellow. At a
     * red crosswalk light the taxi stays still, counting each call as one clock cycle, and
     * proceeds once it has waited more than 3 cycles or the light changes. The wait counter
     * resets whenever the taxi is not being held at a red crosswalk light. Grass, trails,
     * and walls are never passable.
     *
     * @param theTerrain the terrain to be traversed
     * @param theLight the current state of the lights
     * @return true if this taxi may pass the terrain under the given light, false otherwise
     */
    @Override
    public boolean canPass(Terrain theTerrain, Light theLight)
    {
        boolean result = super.canPass(theTerrain, theLight);

        if(!result && theTerrain == Terrain.CROSSWALK)
        {
            MY_WAIT_TIME++;
            if(MY_WAIT_TIME > MY_MAX_LIGHT_WAIT_TIME)
            {
                result = true;
            }
        }
        else
        {
            MY_WAIT_TIME = 0;
        }

        return result;
    }

    /**
     * Taxi-specific behavior: prefers to go straight ahead if that terrain is a street,
     * light, or crosswalk. If not, it turns left if possible, then right if possible, and
     * as a last resort turns around. A taxi facing a light or crosswalk still chooses to go
     * straight and relies on canPass to hold it in place; it never turns to avoid a light.
     *
     * @param theNeighbors the terrain surrounding this taxi, keyed by direction
     * @return the direction this taxi would like to move
     */
    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection())) ?
                getDirection() :
                tryTurnLeft(theNeighbors);
    }

    /**
     * Chooses left if the terrain in that direction is one this taxi can travel on;
     * otherwise defers to {@link #tryTurnRight(Map)}.
     *
     * @param theNeighbors the terrain surrounding this taxi, keyed by direction
     * @return the left direction if passable terrain is there, else the result of trying right
     */
    private Direction tryTurnLeft(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().left())) ?
                getDirection().left() :
                tryTurnRight(theNeighbors);
    }

    /**
     * Chooses right if the terrain in that direction is one this taxi can travel on;
     * otherwise turns around as a last resort.
     *
     * @param theNeighbors the terrain surrounding this taxi, keyed by direction
     * @return the right direction if passable terrain is there, else the reverse direction
     */
    private Direction tryTurnRight(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().right())) ?
                getDirection().right() :
                getDirection().reverse();
    }
}