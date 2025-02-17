import java.util.List;

/**
 * A simple model of a tuna.
 * tunas age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Tuna extends Animal
{
    // Characteristics shared by all tunas (class variables).
    // Tuna parameters
    private static final int BREEDING_AGE = 6;                    // Fast breeding
    private static final int MAX_AGE = 80;                       // Shorter life
    private static final double BREEDING_PROBABILITY = 0.4;       // Higher breed rate
    private static final int MAX_LITTER_SIZE = 3;                // Larger litters
    
    // Individual characteristics (instance fields).
    
    // The tuna's age.
    private int age;

    /**
     * Create a new tuna. A tuna may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the tuna will have a random age.
     * @param location The location within the field.
     */
    public Tuna(boolean randomAge, Location location)
    {
        super(randomAge, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the tuna does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param currentField The field occupied.
     * @param nextFieldState The updated field.
     */
    @Override
     public void act(Field currentField, Field nextFieldState)
    {
        incrementAge();
        if(isAlive()) {

            int hour = getSimulator().getTimeOfDay();

            // Always place the tuna in the next state, even if it doesn't move
            Location currentLocation = getLocation();
            nextFieldState.placeOrganism(this, currentLocation);

            if(hour>= 5 && hour <= 20) {  // Only move and breed during day time
                List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(currentLocation);
                if(!freeLocations.isEmpty()) {
                    Location nextLocation = freeLocations.remove(0);
                    giveBirth(nextFieldState, freeLocations);
                    setLocation(nextLocation);
                    nextFieldState.placeOrganism(this, nextLocation); // Moves the current Tuna object
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "tuna{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", gender=" + getGender() +
                '}';
    }

    /**
     * Increase the age.
     * This could result in the tuna's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this tuna is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New tunas are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed(nextFieldState);
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Tuna young = new Tuna(false, loc);
                nextFieldState.placeOrganism(young, loc);
            }
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed(Field field)
    {
        int births = 0;
        if(canBreed(BREEDING_AGE)) {
            // Look for mate of opposite gender
            Tuna mate = findMatingPartner(field);
            if(mate != null && rand.nextDouble() <= BREEDING_PROBABILITY) {
                births = rand.nextInt(MAX_LITTER_SIZE) + 1;
            }
        }
        return births;
    }
    
    /**
     * Find adjacent tuna of opposite gender.
     * @param field The field with tunas
     * @return Tuna of opposite gender or null
     */
    private Tuna findMatingPartner(Field field) 
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for(Location where : adjacent) {
            Object animal = field.getOrganismAt(where);
            if(animal instanceof Tuna other) {
                if (other.canBreed(BREEDING_AGE) && other.getGender() != this.getGender()) {
                    return other;
                }
            }
        }
        return null;
    }
}
