package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Stream;

public class Bicycle extends AbstractVehicle
{
    private static final int MY_DEATH_TIME = 35;
    private Light myCurrentLight;

    public Bicycle(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        addAllowedLandscape(Terrain.STREET, EnumSet.allOf(Light.class));
        addAllowedLandscape(Terrain.LIGHT, Light.GREEN);
        addAllowedLandscape(Terrain.CROSSWALK, Light.GREEN);
        addAllowedLandscape(Terrain.TRAIL, EnumSet.allOf(Light.class));
    }

    @Override
    public boolean canPass(Terrain theTerrain, Light theLight)
    {
        myCurrentLight = theLight;
        return super.canPass(theTerrain, theLight);
    }

    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return chooseTrailDirection(theNeighbors);
    }

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

    private Direction chooseNonTrailDirection(Map<Direction, Terrain> theNeighbors)
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
