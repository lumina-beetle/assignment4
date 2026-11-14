/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class Human extends AbstractVehicle
{
    private static final int MY_DEATH_TIME = 45;
    private static final Set<Light> MY_ALLOWED_GRASS_LIGHTS = EnumSet.of(
            Light.RED,
            Light.YELLOW,
            Light.GREEN
    );
    private static final Set<Light> MY_ALLOWED_CROSSWALK_LIGHTS = EnumSet.of(
            Light.RED,
            Light.YELLOW
    );

    public Human(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        // can only pass on grass and crosswalks
        addAllowedLandscape(Terrain.GRASS, MY_ALLOWED_GRASS_LIGHTS);
        addAllowedLandscape(Terrain.CROSSWALK, MY_ALLOWED_CROSSWALK_LIGHTS);
    }

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

    private Direction getStandardDirection(Map<Direction, Terrain> theNeighbors)
    {
            return isPreferredDirectionsBlocked(theNeighbors) ?
                    getDirection().reverse() :
                    randomNonReversingDirection();
    }

    private boolean isPreferredDirectionsBlocked(Map<Direction, Terrain> theNeighbors)
    {
        return !getAllowedLandscape().containsKey(theNeighbors.get(getDirection())) &&
                !getAllowedLandscape().containsKey(theNeighbors.get(getDirection().left())) &&
                !getAllowedLandscape().containsKey(theNeighbors.get(getDirection().right()));
    }
}
