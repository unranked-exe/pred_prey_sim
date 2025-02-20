import java.util.List;

/**
 *  Abstract base class representing plants in the Sealife simulation.
 *  This class extends Organism and provides core functionality for all plant species.
 *  Plants remain stationary but can grow and spread across the field based on environmental conditions.
 *
 * @author Aman H, Chris M
 */
public abstract class Plant extends Organism
{
    protected static final int MAX_GROWTH = 5;
    protected static final int GROWTH_RATE = 50;

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
     * First checks if there are any free locations in field and grows plants up to the growth rate per step.
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
