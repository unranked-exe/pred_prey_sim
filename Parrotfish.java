import java.util.List;
import java.util.Random;

/**
 * A simple model of a parrotfish.
 * parrotfishs age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Parrotfish extends Animal
{
    // Characteristics shared by all parrotfishs (class variables).
    // The age at which a parrotfish can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a parrotfish can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a parrotfish breeding.
    private static final double BREEDING_PROBABILITY = 0.05;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The parrotfish's age.
    private int age;

    /**
     * Create a new parrotfish. A parrotfish may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the parrotfish will have a random age.
     * @param location The location within the field.
     */
    public Parrotfish(boolean randomAge, Location location)
    {
        super(location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the parrotfish does most of the time - it runs 
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
            
            Location currentLocation = getLocation();
    
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(currentLocation);
            // Check if there is space to move
            if(!freeLocations.isEmpty()) {
                Location nextLocation = freeLocations.remove(0);
                // Check if there is also space for a child
                if(!freeLocations.isEmpty()) {
                    // Breeding only during night time
                    if(hour <= 5 || hour >= 19) {
                        giveBirth(nextFieldState, freeLocations);
                    }
                }
                // Movement happens any time
                setLocation(nextLocation);
                nextFieldState.placeOrganism(this, nextLocation);
            }
            else {
                // Overcrowding
                setDead();
            }
        }
    }

    @Override
    public String toString() {
        return "parrotfish{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }

    /**
     * Increase the age.
     * This could result in the parrotfish's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this parrotfish is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New parrotfishs are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Parrotfish young = new Parrotfish(false, loc);
                nextFieldState.placeOrganism(young, loc);
            }
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * A parrotfish can breed if it has reached the breeding age.
     * @return true if the parrotfish can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
