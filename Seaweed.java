
/**
 * Write a description of class Seaweed here.
 *
 * @author Aman H, Chris M
 * @version (a version number or a date)
 */
public class Seaweed extends Plant
{
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