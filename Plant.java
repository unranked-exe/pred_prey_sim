import java.util.List;

/**
 * Plant class
 * Inherits from abstract class Organism
 *
 * @author Aman H, Chris M
 * @version (a version number or a date)
 */
public abstract class Plant extends Organism
{
    protected static final int MAX_GROWTH = 5;
    protected static final int GROWTH_RATE = 20;

    public Plant(Location location) {
        super(location);
    }

    @Override
    public void act(Field currentField, Field nextFieldState) {
        if (isAlive())  {
            Location currentLocation = getLocation();
            nextFieldState.placeOrganism(this, currentLocation);
        }
    }

    /**
     * Function to grow the plant
     * @param nextFieldState The new state being built.
     */
    public static void checkGrow(Field nextFieldState) {
        List<Location> freeLocations = nextFieldState.getFreeLocations();
        for (int i = 0; i <= GROWTH_RATE; i++) {
            if (freeLocations.isEmpty()) {
                break;
            }
            Location loc = freeLocations.remove(0);
            Algae algae = new Algae(loc);
            nextFieldState.placeOrganism(algae, loc);
            if (freeLocations.isEmpty()) {
                break;
            }
            loc = freeLocations.remove(0);
            Seaweed seaweed = new Seaweed(loc);
            nextFieldState.placeOrganism(seaweed, loc);
        }
    }
}
