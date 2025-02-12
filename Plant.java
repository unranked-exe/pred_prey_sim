/**
 * Plant class
 * Inherits from abstract class Organism
 *
 * @author Aman H, Chris M
 * @version (a version number or a date)
 */
public class Plant extends Organism
{
    private int growth;
    private static final int MAX_GROWTH = 10;
    private static final int GROWTH_RATE = 1;

    public Plant(Location location) {
        super(location);
        this.growth = 1;
    }

    public void grow(Field currentField) {
        if (isAlive() && growth < MAX_GROWTH && Math.random() < GROWTH_RATE) {
            growth++;
        }
    }
    
    public int getGrowth() {
        return growth;
    }
    
    @Override
    public void act(Field currentField, Field nextFieldState) {
        grow(currentField);
        if (isAlive()) {
            Location currentLocation = getLocation();
            nextFieldState.placeOrganism(this, currentLocation);
        }
    }
}
