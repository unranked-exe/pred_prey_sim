import java.util.List;
import java.util.Random;

/**
 * A simple model of a goldfish.
 * goldfishs age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Goldfish extends Animal
{
    // Characteristics shared by all goldfishs (class variables).
    // The age at which a goldfish can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a goldfish can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a goldfish breeding.
    private static final double BREEDING_PROBABILITY = 0.01;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The goldfish's age.
    private int age;

    /**
     * Create a new goldfish. A goldfish may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the goldfish will have a random age.
     * @param location The location within the field.
     */
    public Goldfish(boolean randomAge, Location location)
    {
        super(location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the goldfish does most of the time - it runs 
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
            
            // Always place the goldfish in the next state
            Location currentLocation = getLocation();
            nextFieldState.placeOrganism(this, currentLocation);
    
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(currentLocation);
                

            // Checks if there is space around Goldfish to give birth
            if(!freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations);
                // Checks if there is still space to move
                if(!freeLocations.isEmpty()) { 
                    // Moving only during night time
                    if(hour <= 5 || hour >= 19) {
                        Location nextLocation = freeLocations.get(0);
                        setLocation(nextLocation);
                        nextFieldState.placeOrganism(this, getLocation());
                    }
                }
            }
            else {
                // Overcrowding
                setDead();
            }
        }
    }

    @Override
    public String toString() {
        return "goldfish{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }

    /**
     * Increase the age.
     * This could result in the goldfish's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this goldfish is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New goldfishs are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Goldfish young = new Goldfish(false, loc);
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
     * A goldfish can breed if it has reached the breeding age.
     * @return true if the goldfish can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
