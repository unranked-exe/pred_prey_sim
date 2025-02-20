import java.util.Iterator;
import java.util.List;

/**
 * A simple model of a shark.
 * Sharks age, move, eat fish, or die due to overcrowding or starvation.
 * 
 * @author David J. Barnes, Michael Kölling, Aman H, Chris M
 * @version 7.1
 */
public class Shark extends Animal
{
    // Characteristics shared by all sharks (class variables).
// Shark parameters
private static final int BREEDING_AGE = 8;
private static final int MAX_AGE = 150;    
private static final double BREEDING_PROBABILITY = 0.8;
private static final int MAX_LITTER_SIZE = 2;          
private static final int FISH_FOOD_VALUE = 25;          
    
    // Individual characteristics (instance fields).
    // The shark's age.
    private int age;
    
    /**
     * Create a shark. A shark can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the shark will have random age and hunger level.
     * @param location The location within the field.
     */
    public Shark(boolean randomAge, Location location)
    {
        super(randomAge, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        else {
            age = 0;
        }
        setFoodValue(rand.nextInt(FISH_FOOD_VALUE));
    }
    
    /**
     * This is what the shark does most of the time: it hunts for
     * fish. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    @Override
     public void act(Field currentField, Field nextFieldState)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
            // Checks if there is spaceto give birth
            if(!freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations);
            }
            // Move towards a source of food if found.
            Location nextLocation = findFood(currentField);
            if(nextLocation == null && ! freeLocations.isEmpty()) {
                // No food found - try to move to a free location.
                nextLocation = freeLocations.remove(0);
            }
            // See if it was possible to move.
            if(nextLocation != null) {
                setLocation(nextLocation);
                nextFieldState.placeOrganism(this, getLocation());
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    @Override
    public String toString() {
        return "Shark{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + getFoodValue() +
                ", gender=" + getGender() +
                '}';
    }

    
    /**
     * Increase the age. This could result in the shark's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Look for fish adjacent to the current location.
     * Only the first live fish is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood(Field field)
    {
        // Skip hunting if foggy
        if(getSimulator().getWeather().getCondition() == Weather.Condition.FOGGY) {
            return null;
        }
    
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Organism animal = field.getOrganismAt(loc);
            if(animal instanceof Tuna fish) {
                if(fish.isAlive()) {
                    fish.setDead();
                    setFoodValue(FISH_FOOD_VALUE);
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }
    /**
     * Check whether this shark is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // Call breed with the current field
        int births = breed(nextFieldState);
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Shark young = new Shark(false, loc);
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
            Shark mate = findMatingPartner(field);
            if(mate != null && rand.nextDouble() <= BREEDING_PROBABILITY) {
                births = rand.nextInt(MAX_LITTER_SIZE) + 1;
            }
        }
        return births;
    }
    
    /**
     * Find adjacent shark of opposite gender.
     * @param field The field with sharks
     * @return Shark of opposite gender or null
     */
    private Shark findMatingPartner(Field field) 
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        for(Location where : adjacent) {
            Object animal = field.getOrganismAt(where);
            if(animal instanceof Shark other) {
                if (other.canBreed(BREEDING_AGE) && other.getGender() != this.getGender()) {
                    return other;
                }
            }
        }
        return null;
    }
}