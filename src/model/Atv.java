package model;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An all-terrain vehicle (ATV) in the Road Rage simulation. ATVs can travel on any
 * terrain except walls, ignore all traffic lights and crosswalk lights, never reverse
 * direction, and stay dead for 25 updates after colliding with a living truck, car,
 * or taxi.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public class Atv extends AbstractVehicle
{
    /** The number of updates an ATV stays dead after dying. */
    private static final int MY_DEATH_TIME = 25;

    /**
     * Creates an ATV at the given position facing the given direction. The ATV is
     * allowed to pass every terrain type except walls, under every light state.
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     */
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

    /**
     * ATV-specific behavior: randomly chooses to go straight, turn left, or turn right,
     * each with equal probability. An ATV never chooses to reverse direction. The
     * neighboring terrain is not consulted.
     *
     * @param theNeighbors the terrain surrounding this ATV (unused)
     * @return the chosen direction: straight ahead, left, or right of the current direction
     */
    @Override
    public Direction chooseDirection(Map<Direction, Terrain> theNeighbors)
    {
        return randomNonReversingDirection();
    }
}