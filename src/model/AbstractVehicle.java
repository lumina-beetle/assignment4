/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.*;

/**
 * Abstract parent class for every vehicle in the Road Rage simulation. It holds all
 * state and behavior shared by the concrete vehicle types (position, direction,
 * death and revival handling, collision rules, image file names, reset, and the
 * table of terrain and light combinations a vehicle may pass). Child classes supply
 * their own death time, register the terrain they can traverse, and implement
 * {@link Vehicle#chooseDirection(Map)}.
 *
 * @author Your Name Here
 * @version Date or Version Here
 */
public abstract class AbstractVehicle implements Vehicle
{
    /** The single Random object shared by all vehicles. */
    private static final Random MY_RANDOM = new Random();

    /**
     * Maps each terrain this vehicle may traverse to the set of light states under
     * which it may pass that terrain.
     */
    private final Map<Terrain, Set<Light>> myAllowedLandscapes =
            new EnumMap<>(Terrain.class);

    /** The position and direction this vehicle had when it was constructed. */
    private final InitialState myOriginalState;

    /** The number of updates this vehicle stays dead after a fatal collision. */
    private final int myDeathTime;

    /** Whether this vehicle is currently alive. */
    private boolean myAliveState;

    /** The number of pokes remaining until this vehicle revives. */
    private int myCurrentDeathTime;

    /** The direction this vehicle is currently facing. */
    private Direction myDirection;

    /** The current position of this vehicle, stored as {x, y}. */
    private int[] myPosition;

    /**
     * Creates a vehicle at the given position, facing the given direction, that
     * stays dead for the given number of updates after dying.
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDirection the starting direction
     * @param theDeathTime the number of updates this vehicle stays dead
     * @throws IllegalArgumentException if theX, theY, or theDeathTime is negative
     */
    protected AbstractVehicle(final int theX, final int theY,
                           final Direction theDirection,
                           final int theDeathTime)
    {
        validateConstructorArguments(theX, theY, theDeathTime);

        myPosition = new int[] {theX, theY};
        myDirection = theDirection;
        myOriginalState = new InitialState(new int[]{theX, theY}, theDirection);

        myDeathTime = theDeathTime;
        myAliveState = true;
    }

    /**
     * Checks that the constructor arguments are valid.
     *
     * @param theX the starting x coordinate
     * @param theY the starting y coordinate
     * @param theDeathTime the number of updates this vehicle stays dead
     * @throws IllegalArgumentException if theX, theY, or theDeathTime is negative
     */
    private final void validateConstructorArguments(final int theX, final int theY,
                                                    final int theDeathTime)
    {
        if (theX < 0 || theY < 0)
        {
            throw new IllegalArgumentException("Passed in coordinates cannot be less than 0!");
        }

        if(theDeathTime < 0)
        {
            throw new IllegalArgumentException("Passed in death time cannot be less than 0!");
        }
    }

    /**
     * Returns the name of the image file used to draw this vehicle. The name is the
     * lowercase simple class name, followed by "_dead" if the vehicle is dead, followed
     * by ".gif" (for example "taxi.gif" or "taxi_dead.gif").
     *
     * @return the image file name for this vehicle's current state
     */
    @Override
    public final String getImageFileName()
    {
        final StringBuilder fileNameBuilder = new StringBuilder(
                getClass().
                        getSimpleName().
                        toLowerCase(Locale.US)
        );

        if (!isAlive())
        {
            fileNameBuilder.append("_dead");
        }

        fileNameBuilder.append(".gif");
        return fileNameBuilder.toString();
    }

    /**
     * Default pass rule: this vehicle can pass the given terrain only if that terrain
     * was registered as allowed by the child class and the given light state is one of
     * the light states allowed for that terrain. Child classes may override this.
     *
     * @param theTerrain the terrain to be traversed
     * @param theLight the current state of the lights
     * @return true if this vehicle may pass the terrain under the given light, false otherwise
     */
    @Override
    public boolean canPass(Terrain theTerrain, Light theLight)
    {
        boolean result = false;

        if(getAllowedLandscape().containsKey(theTerrain))
        {
            result = getAllowedLandscape().get(theTerrain).contains(theLight);
        }

        return result;
    }

    // death related methods
    /**
     * Handles a collision with another vehicle. Collisions with a dead vehicle have no
     * effect. This vehicle dies only if the other vehicle is alive and has a smaller death
     * time than this vehicle. Only this vehicle's own state is updated.
     *
     * @param theOther the vehicle this vehicle collided with
     */
    @Override
    public final void collide(final Vehicle theOther)
    {
        if (!theOther.isAlive())
            return;

        if (getDeathTime() > theOther.getDeathTime())
        {
            myAliveState = false;
            myCurrentDeathTime = getDeathTime();
        }
    }

    /**
     * Notifies this dead vehicle that one update has occurred. When the remaining death
     * time reaches zero, the vehicle revives and faces a random direction.
     */
    @Override
    public final void poke()
    {
        myCurrentDeathTime--;

        if(myCurrentDeathTime <= 0)
        {
            myAliveState = true;
            setDirection(Direction.random());
        }
    }

    /**
     * Returns a string representation of this vehicle for debugging, containing its
     * class name and its x and y coordinates.
     *
     * @return a string describing this vehicle
     */
    @Override
    public String toString()
    {
        return "%s - {X: %d, Y: %d}".formatted(getClass().getSimpleName(), getX(), getY());
    }

    // getters
    /**
     * Returns the table of terrain and light combinations this vehicle may pass.
     *
     * @return the map from allowed terrain to the light states allowed on it
     */
    protected final Map<Terrain, Set<Light>> getAllowedLandscape()
    {
        return myAllowedLandscapes;
    }

    /**
     * Returns the number of updates this vehicle stays dead after dying.
     *
     * @return the death time of this vehicle
     */
    @Override
    public final int getDeathTime()
    {
        return myDeathTime;
    }

    /**
     * Returns the direction this vehicle is facing.
     *
     * @return the current direction
     */
    @Override
    public final Direction getDirection()
    {
        return myDirection;
    }

    /**
     * Returns the x coordinate of this vehicle.
     *
     * @return the current x coordinate
     */
    @Override
    public final int getX()
    {
        return myPosition[0];
    }

    /**
     * Returns the y coordinate of this vehicle.
     *
     * @return the current y coordinate
     */
    @Override
    public final int getY()
    {
        return myPosition[1];
    }

    /**
     * Returns whether this vehicle is alive.
     *
     * @return true if this vehicle is alive, false if it is dead
     */
    @Override
    public final boolean isAlive()
    {
        return myAliveState;
    }

    // setters
    /**
     * Returns this vehicle to the position and direction it had when constructed,
     * and makes it alive again.
     */
    @Override
    public final void reset()
    {
        setX(myOriginalState.myPosition[0]);
        setY(myOriginalState.myPosition[1]);
        myDirection = myOriginalState.myDirection;

        myAliveState = true;
        myCurrentDeathTime = 0;
    }

    /**
     * Sets the direction this vehicle is facing.
     *
     * @param theDirection the new direction
     */
    @Override
    public final void setDirection(final Direction theDirection)
    {
        myDirection = theDirection;
    }

    /**
     * Sets the x coordinate of this vehicle.
     *
     * @param theX the new x coordinate
     * @throws IllegalArgumentException if theX is negative
     */
    @Override
    public final void setX(final int theX)
    {
        if (theX < 0)
        {
            throw new IllegalArgumentException("X position cannot be less than 0!");
        }

        myPosition[0] = theX;
    }

    /**
     * Sets the y coordinate of this vehicle.
     *
     * @param theY the new y coordinate
     * @throws IllegalArgumentException if theY is negative
     */
    @Override
    public final void setY(final int theY)
    {
        if (theY < 0)
        {
            throw new IllegalArgumentException("Y position cannot be less than 0!");
        }

        myPosition[1] = theY;
    }

    // helper methods
    /**
     * Randomly picks straight ahead, left, or right relative to the current direction,
     * each with equal probability. Never returns the reverse direction.
     *
     * @return the current direction, or the direction to its left or right
     */
    protected final Direction randomNonReversingDirection()
    {
        int decider = MY_RANDOM.nextInt(-1, 2);
        Direction result = myDirection;

        if(decider == -1)
        {
            return myDirection.left();
        }
        else if (decider == 1)
        {
            return myDirection.right();
        }


        return result;
    }

    /**
     * Registers a terrain this vehicle may pass, along with the set of light states
     * under which it may pass it. Replaces any previous entry for that terrain.
     *
     * @param theTerrain the terrain to allow
     * @param theAllowedLightsOnTerrain the light states under which the terrain may be passed
     */
    protected final void addAllowedLandscape(Terrain theTerrain, Set<Light> theAllowedLightsOnTerrain)
    {
        myAllowedLandscapes.put(theTerrain, theAllowedLightsOnTerrain);
    }

    /**
     * Registers a terrain this vehicle may pass only under a single light state.
     * Replaces any previous entry for that terrain.
     *
     * @param theTerrain the terrain to allow
     * @param theLight the only light state under which the terrain may be passed
     */
    protected final void addAllowedLandscape(Terrain theTerrain, Light theLight)
    {
        myAllowedLandscapes.put(theTerrain, EnumSet.of(theLight));
    }

    /**
     * Registers several terrains this vehicle may pass, all under the same set of
     * light states.
     *
     * @param theTerrain the terrains to allow
     * @param theAllowedLightsOnTerrain the light states under which each terrain may be passed
     */
    protected final void addAllowedLandscape(Set<Terrain> theTerrain,
                                             Set<Light> theAllowedLightsOnTerrain)
    {
        for(Terrain terrainElement : theTerrain)
        {
            addAllowedLandscape(terrainElement, theAllowedLightsOnTerrain);
        }
    }

    /**
     * Holds the starting position and direction of a vehicle so that it can be
     * restored by {@link AbstractVehicle#reset()}.
     */
    private static final class InitialState
    {
        /** The starting position, stored as {x, y}. */
        int[] myPosition;

        /** The starting direction. */
        Direction myDirection;

        /**
         * Creates a snapshot of a starting state. The position array is copied.
         *
         * @param thePosition the starting position as {x, y}
         * @param theDirection the starting direction
         */
        InitialState(int[] thePosition, Direction theDirection)
        {

            myPosition = Arrays.copyOf(thePosition, thePosition.length);
            myDirection = theDirection;
        }
    }
}