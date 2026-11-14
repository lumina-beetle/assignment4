/*
 * TCSS 305 - Road Rage
 */

package tests;

import model.AbstractVehicle;
import model.Direction;
import model.Light;
import model.Terrain;

import java.util.Map;

/**
 * A test-only vehicle that can never move and never dies. It is used in unit tests as a
 * stand-in collision partner that is always alive and has the smallest possible death time.
 * How is it immovable? Everything else moves around it. Including the ground it sits on. And
 * the planet. Obviously
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public class ImmovableRod extends AbstractVehicle
{
    /**
     * The death time of an immovable rod. Because it is zero, the rod never dies in a
     * collision.
     */
    private static final int ROD_RESPAWN_LENGTH = 0;

    /**
     * Creates an immovable rod at the given position facing the given direction.
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     */
    public ImmovableRod(int theX, int theY, Direction theDirection)
    {
        super(theX, theY, theDirection, ROD_RESPAWN_LENGTH);
    }

    /**
     * Rod-specific behavior: an immovable rod cannot pass any terrain under any light
     * state.
     *
     * @param theTerrain the terrain to be traversed (ignored)
     * @param theLight the current state of the lights (ignored)
     * @return always false
     */
    @Override
    public boolean canPass(Terrain theTerrain, Light theLight)
    {
        return false;
    }

    /**
     * Rod-specific behavior: an immovable rod has no preferred direction, so this method
     * does not choose one.
     *
     * @param theNeighbors the terrain surrounding this rod (ignored)
     * @return always null
     */
    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return null;
    }
}