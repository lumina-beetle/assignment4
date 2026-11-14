package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class Car extends AbstractVehicle
{
    private static final int MY_DEATH_TIME = 15;

    private static final Set<Light> MY_ALLOWED_STREET_LIGHTS = EnumSet.of(
            Light.GREEN,
            Light.YELLOW,
            Light.RED
    );

    private static final Set<Light> MY_ALLOWED_LIGHTS =  EnumSet.of(
            Light.GREEN,
            Light.YELLOW
    );

    public Car(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        // can only pass on streets, crosswalks, and lights
        addAllowedLandscape(Terrain.STREET, MY_ALLOWED_STREET_LIGHTS);
        addAllowedLandscape(Terrain.CROSSWALK, Light.GREEN);
        addAllowedLandscape(Terrain.LIGHT, MY_ALLOWED_LIGHTS);
    }

    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection())) ?
                getDirection() :
                tryTurnLeft(theNeighbors);
    }

    private Direction tryTurnLeft(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().left())) ?
                getDirection().left() :
                tryTurnRight(theNeighbors);
    }

    private Direction tryTurnRight(Map<Direction, Terrain> theNeighbors)
    {
        return getAllowedLandscape().containsKey(theNeighbors.get(getDirection().right())) ?
                getDirection().right() :
                getDirection().reverse();
    }
}
