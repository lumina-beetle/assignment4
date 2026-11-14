/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * A human pedestrian in the Road Rage simulation. Humans travel only on grass and
 * crosswalks, move in a random direction (straight, left, or right), and never reverse
 * unless there is no other option. A human always turns toward an adjacent crosswalk.
 * Humans cross crosswalks only when the light is yellow or red, and wait when it is
 * green. A human dies if it collides with any living vehicle except another human and
 * stays dead for 45 updates.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public class Human extends AbstractVehicle
{
    /** The number of updates a human stays dead after dying. */
    private static final int MY_DEATH_TIME = 45;

    /** The light states under which a human may pass grass: all of them. */
    private static final Set<Light> MY_ALLOWED_GRASS_LIGHTS = EnumSet.of(
            Light.RED,
            Light.YELLOW,
            Light.GREEN
    );

    /** The light states under which a human may pass a crosswalk: red and yellow. */
    private static final Set<Light> MY_ALLOWED_CROSSWALK_LIGHTS = EnumSet.of(
            Light.RED,
            Light.YELLOW
    );

    /**
     * Creates a human at the given position facing the given direction. The human may
     * pass grass under any light state, and crosswalks only when the crosswalk light is
     * red or yellow (it waits when the light is green).
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     */
    public Human(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        // can only pass on grass and crosswalks
        addAllowedLandscape(Terrain.GRASS, MY_ALLOWED_GRASS_LIGHTS);
        addAllowedLandscape(Terrain.CROSSWALK, MY_ALLOWED_CROSSWALK_LIGHTS);
    }

    /**
     * Human-specific behavior: if a crosswalk is adjacent (ahead, left, or right, but not
     * behind), the human always turns to face it, even if the crosswalk light is green, in
     * which case canPass holds the human in place until the light changes. Otherwise, the
     * human randomly chooses straight, left, or right, and reverses only if none of those
     * three directions is grass or crosswalk.
     *
     * @param theNeighbors the terrain surrounding this human, keyed by direction
     * @return the direction this human would like to move
     */
    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return theNeighbors.entrySet().stream()
                .filter(e ->
                        e.getKey() != getDirection().reverse()
                )
                .filter(e ->
                        e.getValue() == Terrain.CROSSWALK
                )
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseGet(() -> getStandardDirection(theNeighbors));

    }

    /**
     * Chooses the direction used when no crosswalk is adjacent: reverses if straight,
     * left, and right are all impassable, and otherwise picks randomly among straight,
     * left, and right.
     *
     * @param theNeighbors the terrain surrounding this human, keyed by direction
     * @return the reverse direction if every preferred direction is blocked, else a random
     *         non-reversing direction
     */
    private Direction getStandardDirection(Map<Direction, Terrain> theNeighbors)
    {
        return isPreferredDirectionsBlocked(theNeighbors) ?
                getDirection().reverse() :
                randomNonReversingDirection();
    }

    /**
     * Determines whether this human cannot travel straight, left, or right because none
     * of those neighboring terrains is one it can pass.
     *
     * @param theNeighbors the terrain surrounding this human, keyed by direction
     * @return true if straight, left, and right are all impassable terrain, false otherwise
     */
    private boolean isPreferredDirectionsBlocked(Map<Direction, Terrain> theNeighbors)
    {
        return !getAllowedLandscape().containsKey(theNeighbors.get(getDirection())) &&
                !getAllowedLandscape().containsKey(theNeighbors.get(getDirection().left())) &&
                !getAllowedLandscape().containsKey(theNeighbors.get(getDirection().right()));
    }
}