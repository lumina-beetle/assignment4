package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

public class Atv extends AbstractVehicle
{
    private static final int MY_DEATH_TIME = 25;

    public Atv(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, MY_DEATH_TIME);

        // effectively add all terrains (minus walls) to the allowed traversable terrain
        // and allow all lights for each of these terrains
        addAllowedLandscape(EnumSet.allOf(Terrain.class).stream()
                .filter(t -> t != Terrain.WALL)
                .collect(Collectors.toSet()),
                EnumSet.allOf(Light.class));
    }

    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return randomNonReversingDirection();
    }
}
