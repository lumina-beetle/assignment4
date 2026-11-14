package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class Truck extends AbstractVehicle
{
    private static final int MY_DEATH_TIME = 1;
    private static final Set<Light> MY_ALLOWED_CROSSWALK_LIGHTS = EnumSet.of(
            Light.GREEN, Light.YELLOW
    );

    public Truck(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        addAllowedLandscape(Terrain.STREET, EnumSet.allOf(Light.class));
        addAllowedLandscape(Terrain.LIGHT, EnumSet.allOf(Light.class));
        addAllowedLandscape(Terrain.CROSSWALK, MY_ALLOWED_CROSSWALK_LIGHTS);
    }

    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
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
