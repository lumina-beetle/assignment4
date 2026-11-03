package model;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

public abstract class AbstractVehicle implements Vehicle
{
    /** Constant random to be used across every vehicle. */
    protected static final Random MY_RANDOM = new Random();

    private int myX;
    private int myY;
    private Direction myDirection;

    protected AbstractVehicle(final int theX, final int theY,
                              final Direction theDirection)
    {
        if (theX < 0 || theY < 0)
        {
            throw new IllegalArgumentException("Passed in coordinates cannot be less than 0!");
        }

        myX = theX;
        myY = theY;
        myDirection = theDirection;
    }

    @Override
    public void collide(final Vehicle theOther)
    {
        if (getDeathTime() < theOther.getDeathTime())
        {

        }
    }

    @Override
    public int getDeathTime()
    {
        return 0;
    }

    @Override
    public String getImageFileName()
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
    public Direction getDirection()
    {
        return myDirection;
    }

    @Override
    public int getX()
    {
        return myX;
    }

    @Override
    public int getY()
    {
        return myY;
    }

    @Override
    public boolean isAlive()
    {
        return false;
    }

    @Override
    public void poke()
    {

    }

    @Override
    public void reset()
    {

    }

    @Override
    public void setDirection(final Direction theDir)
    {

    }

    @Override
    public void setX(final int theX)
    {
        if (theX < 0)
        {
            throw new IllegalArgumentException("X position cannot be less than 0!");
        }
    }

    @Override
    public void setY(final int theY)
    {

    }
}
