/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.EnumSet;
import java.util.Map;

/**
 * A bicycle in the Road Rage simulation. Bicycles can travel on streets, trails, lights,
 * and crosswalks, but they prefer trails. They pass lights and crosswalk lights only when
 * green, and stay dead for 35 updates after colliding with a living truck, car, taxi, or ATV.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public class Bicycle extends AbstractVehicle
{
    /** The number of updates a bicycle stays dead after dying. */
    private static final int MY_DEATH_TIME = 35;

    /** The light state most recently passed to {@link #canPass(Terrain, Light)}. */
    private Light myCurrentLight;

    /**
     * Creates a bicycle at the given position facing the given direction. The bicycle
     * may pass streets and trails under any light state, and lights and crosswalks
     * only when the light is green.
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     */
    public Bicycle(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        addAllowedLandscape(Terrain.STREET, EnumSet.allOf(Light.class));
        addAllowedLandscape(Terrain.LIGHT, Light.GREEN);
        addAllowedLandscape(Terrain.CROSSWALK, Light.GREEN);
        addAllowedLandscape(Terrain.TRAIL, EnumSet.allOf(Light.class));
    }

    /**
     * Bicycle-specific behavior: a bicycle can pass streets and trails under any light
     * state, but can pass lights and crosswalk lights only when the light is green (it
     * stops for yellow and red). Also remembers the given light state for use when
     * choosing a direction. Walls and grass are never passable.
     *
     * @param theTerrain the terrain to be traversed
     * @param theLight the current state of the lights
     * @return true if this bicycle may pass the terrain under the given light, false otherwise
     */
    @Override
    public boolean canPass(Terrain theTerrain, Light theLight)
    {
        myCurrentLight = theLight;
        return super.canPass(theTerrain, theLight);
    }

    /**
     * Bicycle-specific behavior: prefers trails. If the terrain straight ahead is a
     * passable trail, the bicycle goes straight. Otherwise, if a trail is to the left or
     * right, it turns toward the trail. If there is no trail, it goes straight if that
     * terrain is a street, light, or crosswalk, else turns left if possible, else turns
     * right if possible, and as a last resort turns around. A bicycle facing a light or
     * crosswalk still chooses to go straight, and relies on {@link #canPass(Terrain, Light)}
     * to hold it in place when the light is not green.
     *
     * @param theNeighbors the terrain surrounding this bicycle, keyed by direction
     * @return the direction this bicycle would like to move
     */
    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return chooseTrailDirection(theNeighbors);
    }

    /**
     * Chooses straight ahead if the terrain in that direction is a trail this bicycle can
     * pass; otherwise defers to {@link #chooseNonForwardTrailDirection(Map)}.
     *
     * @param theNeighbors the terrain surrounding this bicycle, keyed by direction
     * @return the direction this bicycle would like to move
     */
    private Direction chooseTrailDirection(Map<Direction, Terrain> theNeighbors)
    {
        return theNeighbors.entrySet().stream()
                .filter(v -> v.getValue() == Terrain.TRAIL &&
                        v.getKey() == getDirection() &&
                        canPass(v.getValue(), myCurrentLight))
                .findAny()
                .map(Map.Entry::getKey)
                .orElseGet(() -> chooseNonForwardTrailDirection(theNeighbors));
    }

    /**
     * Chooses the left or right direction if the terrain in that direction is a trail;
     * otherwise defers to {@link #chooseNonTrailDirection(Map)}.
     *
     * @param theNeighbors the terrain surrounding this bicycle, keyed by direction
     * @return the direction this bicycle would like to move
     */
    private Direction chooseNonForwardTrailDirection(Map<Direction, Terrain> theNeighbors)
    {
        return theNeighbors.entrySet().stream()
                .filter(v -> v.getValue() == Terrain.TRAIL &&
                        v.getKey() != getDirection() &&
                        v.getKey() != getDirection().reverse())
                .findAny()
                .map(Map.Entry::getKey)
                .orElseGet(() -> chooseNonTrailDirection(theNeighbors));
    }

    /**
     * Chooses straight ahead if the terrain in that direction is one this bicycle can
     * travel on; otherwise defers to {@link #tryTurnLeft(Map)}.
     *
     * @param theNeighbors the terrain surrounding this bicycle, keyed by direction
     * @return the direction this bicycle would like to move
     */
    private Direction chooseNonTrailDirection(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection())) ?
                getDirection() :
                tryTurnLeft(theNeighbors);
    }

    /**
     * Chooses left if the terrain in that direction is one this bicycle can travel on;
     * otherwise defers to {@link #tryTurnRight(Map)}.
     *
     * @param theNeighbors the terrain surrounding this bicycle, keyed by direction
     * @return the direction this bicycle would like to move
     */
    private Direction tryTurnLeft(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().left())) ?
                getDirection().left() :
                tryTurnRight(theNeighbors);
    }

    /**
     * Chooses right if the terrain in that direction is one this bicycle can travel on;
     * otherwise turns around as a last resort.
     *
     * @param theNeighbors the terrain surrounding this bicycle, keyed by direction
     * @return the right direction if passable terrain is there, else the reverse direction
     */
    private Direction tryTurnRight(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().right())) ?
                getDirection().right() :
                getDirection().reverse();
    }
}