
/**
 * Represents Algae in the Sealife simulation, a basic plant organism that serves
 * as a primary food source for Parrotfish and Goldfish
 *
 * @author Aman H, Chris M
 * @version (a version number or a date)
 */
public class Algae extends Plant
{
    // Fixed food value of 10 steps
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