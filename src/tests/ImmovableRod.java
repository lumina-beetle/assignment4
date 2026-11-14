package tests;

import model.AbstractVehicle;
import model.Direction;
import model.Light;
import model.Terrain;

import java.util.Map;

public class ImmovableRod extends AbstractVehicle
{
    private static final int ROD_RESPAWN_LENGTH = 0;

    public ImmovableRod(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, ROD_RESPAWN_LENGTH);
    }

    @Override
    public boolean canPass(Terrain theTerrain, Light theLight)
    {
        return false;
    }

    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return null;
    }
}
