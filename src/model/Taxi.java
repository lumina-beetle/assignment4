package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class Taxi extends AbstractVehicle
{
    private static final int MY_DEATH_TIME = 15;
    private static final int MY_MAX_LIGHT_WAIT_TIME = 3;
    private static final Set<Light> MY_ALLOWED_STREET_LIGHTS = EnumSet.of(
            Light.GREEN,
            Light.YELLOW,
            Light.RED
    );

    private static final Set<Light> MY_ALLOWED_CROSSWALK_LIGHTS =  EnumSet.of(
            Light.GREEN
    );

    private static final Set<Light> MY_ALLOWED_LIGHTS =  EnumSet.of(
            Light.GREEN,
            Light.YELLOW
    );

    private int MY_WAIT_TIME = 0;

    public Taxi(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);
        // can only pass on streets, crosswalks, and likes
        addAllowedLandscape(Terrain.STREET, MY_ALLOWED_STREET_LIGHTS);
        addAllowedLandscape(Terrain.CROSSWALK, MY_ALLOWED_CROSSWALK_LIGHTS);
        addAllowedLandscape(Terrain.LIGHT, MY_ALLOWED_LIGHTS);
    }

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
