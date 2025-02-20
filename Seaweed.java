/**
 * Represents Seaweed in the Sealife simulation, a basic plant organism that serves
 * as a primary food source for Parrotfish and Goldfish.
 *
 * @author Aman H, Chris M
 */
public class Seaweed extends Plant
{
    // Fixed food value of 13 steps
    public static final int FOOD_VALUE = 13;
    /**
     * Constructor for objects of class Seaweed
     */
    public Seaweed(Location location)
    {
        super(location);
    }

    @Override
    public String toString() {
        return "Seaweed{" +
                "alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }
}