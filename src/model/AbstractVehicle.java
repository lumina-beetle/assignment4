/*
 * TCSS 305 - Road Rage
 */

package model;

import java.util.*;

public abstract class AbstractVehicle implements Vehicle
{
    private static final Random MY_RANDOM = new Random();
    private final Map<Terrain, Set<Light>> MY_ALLOWED_LANDSCAPES =
            new EnumMap<>(Terrain.class);

    private final InitialState myOriginalState;
    private final int myDeathTime;
    private boolean myAliveState;
    private int myCurrentDeathTime;
    private Direction myDirection;
    private int[] myPosition;

    public AbstractVehicle(final int theX, final int theY,
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

    @Override
    public String toString()
    {
        return "%s - {X: %d, Y: %d}".formatted(getClass().getSimpleName(), getX(), getY());
    }

    // getters
    protected final Map<Terrain, Set<Light>> getAllowedLandscape()
    {
        return MY_ALLOWED_LANDSCAPES;
    }

    @Override
    public final int getDeathTime()
    {
        return myDeathTime;
    }

    @Override
    public final Direction getDirection()
    {
        return myDirection;
    }

    @Override
    public final int getX()
    {
        return myPosition[0];
    }

    @Override
    public final int getY()
    {
        return myPosition[1];
    }

    @Override
    public final boolean isAlive()
    {
        return myAliveState;
    }

    // setters
    @Override
    public final void reset()
    {
        setX(myOriginalState.myPosition[0]);
        setY(myOriginalState.myPosition[1]);
        myDirection = myOriginalState.myDirection;

        myAliveState = true;
        myCurrentDeathTime = 0;
    }

    @Override
    public final void setDirection(final Direction theDirection)
    {
        myDirection = theDirection;
    }

    @Override
    public final void setX(final int theX)
    {
        if (theX < 0)
        {
            throw new IllegalArgumentException("X position cannot be less than 0!");
        }

        myPosition[0] = theX;
    }

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

    protected final void addAllowedLandscape(Terrain theTerrain, Set<Light> theAllowedLightsOnTerrain)
    {
        MY_ALLOWED_LANDSCAPES.put(theTerrain, theAllowedLightsOnTerrain);
    }

    protected final void addAllowedLandscape(Terrain theTerrain, Light theLight)
    {
        MY_ALLOWED_LANDSCAPES.put(theTerrain, EnumSet.of(theLight));
    }

    protected final void addAllowedLandscape(Set<Terrain> theTerrain,
                                             Set<Light> theAllowedLightsOnTerrain)
    {
        for(Terrain terrainElement : theTerrain)
        {
            addAllowedLandscape(terrainElement, theAllowedLightsOnTerrain);
        }
    }

    private static final class InitialState
    {
        int[] myPosition;
        Direction myDirection;

        InitialState(int[] thePosition, Direction theDirection)
        {

            myPosition = Arrays.copyOf(thePosition, thePosition.length);
            myDirection = theDirection;
        }
    }
}
