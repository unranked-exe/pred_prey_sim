
/**
 * Write a description of class Algae here.
 *
 * @author Aman H, Chris M
 * @version (a version number or a date)
 */
public class Algae extends Plant
{
    public static final int FOOD_VALUE = 10;
    
    /**
     * Constructor for objects of class Algae
     */
    public Algae(Location location)
    {
        super(location);
    }

    @Override
    public String toString() {
        return "Algae{" +
                "alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }
}