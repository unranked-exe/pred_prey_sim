import java.util.Iterator;
import java.util.List;

/**
 * A simple model of a goldfish.
 * goldfishs age, move, breed, or die due to overcrowding or starvation.
 * 
 * @author David J. Barnes, Michael Kölling, Aman H, Chris M
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
    
    /**
     * Create a new goldfish. A goldfish may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the goldfish will have a random age.
     * @param location The location within the field.
     */
    public Goldfish(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
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
        incrementAge(MAX_AGE);
        if(isAlive()) {
            int hour = getSimulator().getTimeOfDay();
            
            // Always place the goldfish in the next state
            Location currentLocation = getLocation();
            nextFieldState.placeOrganism(this, currentLocation);
    
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(currentLocation);
             
            // Check if there is food to eat
            Location foodLoc = findFood(currentField);
            if(foodLoc != null) {
                nextFieldState.placeOrganism(this, foodLoc);
            }
            else {
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
    }

    @Override
    public String toString() {
        return "goldfish{" +
                "age=" + getAge() +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }
    
    /**
     * Check whether or not this goldfish is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     * @return true if the goldfish gave birth, false otherwise.
     */
    private boolean giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New goldfishs are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed(BREEDING_AGE, BREEDING_PROBABILITY, MAX_LITTER_SIZE);
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Goldfish young = new Goldfish(false, loc);
                nextFieldState.placeOrganism(young, loc);
            }
        }
        return births > 0;
    }


    /**
     * Finds location of nearest food (Plants) and eats it
     * @param field The current state of the field
     * @return location of food
     */
    private Location findFood(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Organism organism = field.getOrganismAt(loc);
            if(organism instanceof Plant plant) {
                if(plant.isAlive()) {
                    switch (plant) {
                        case Seaweed seaweed -> {
                            setFoodValue(Seaweed.FOOD_VALUE);
                        }
                        case Algae algae -> {
                            setFoodValue(Algae.FOOD_VALUE);
                        } 
                        default -> {
                        }
                    }
                    //Plant is now eaten
                    plant.setDead();
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }
}
