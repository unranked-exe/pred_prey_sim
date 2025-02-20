import java.util.List;

/**
 * Abstract class representing animals in the Sealife simulation.
 * This class extends Organism and provides core functionality for all animal species,
 * including predators and prey. Animals have gender, age, food levels, and can be affected by diseases.
 *
 * @author David J. Barnes, Michael Kölling, Aman H, Chris M
 * @version 7.0
 */
public abstract class Animal extends Organism
{

    // Gender enum
    public enum Gender {
        MALE,FEMALE
    }

    // The animal's gender.
    private final Gender gender;
    private int age;
    // Food level, which is increased by eating plants/fish
    private int foodLevel = 0;
    private boolean infected = false;
    private static final double INFECTION_PROBABILITY = 0.001;
    private static final double SPREAD_PROBABILITY = 0.2;

    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(boolean randomAge, Location location)
    {
        super(location);
        this.gender = Math.random() < 0.5 ? Gender.MALE : Gender.FEMALE;
    }

    /**
     * Get the animal's gender.
     * @return The animal's gender.
     */
    public Gender getGender()
    {
        return gender;
    }

    /**
     * Get the animal's age.
     * @return The animal's age.
     */
    protected int getAge()
    {
        return age;
    }

    /**
     * Set the animal's age.
     * @param age The animal's age.
     */
    protected void setAge(int age)
    {
        this.age = age;
    }
    /**
     * Increase the age.
     * This could result in the parrotfish's death.
     */
    protected void incrementAge(int max_age)
    {
        age++;
        if(age > max_age) {
            setDead();
        }
    }

    /**
     * Getter for animal's food value
     * @return Current Food Level
     */
    protected int getFoodValue()
    {
        return foodLevel;
    }

    /**
     * Set the food value of the animal.
     * @param foodValue The food value of the animal.
     */
    protected void setFoodValue(int foodValue)
    {
        this.foodLevel = foodValue;
    }

    /**
     * Decrement the food level of the animal therefore making them hungry.
     */
    protected void incrementHunger()
    {
        this.foodLevel--;
        if(this.foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * An animal can breed if it has reached the breeding age.
     * @param breeding_age The age at which the animal can breed up to.
     * @return true if the animal can breed, false otherwise.
     */
    protected boolean canBreed(int breeding_age)
    {
        return age >= breeding_age;
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @param breeding_age The age at which the animal can breed up to.
     * @param breeding_probability The probability of breeding.
     * @param max_litter_size The maximum number of offspring.
     * @return The number of births (may be zero).
     */
    protected int breed(int breeding_age, double breeding_probability, int max_litter_size)
    {
        int births;
        if(canBreed(breeding_age) && rand.nextDouble() <= breeding_probability) {
            births = rand.nextInt(max_litter_size) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * Check if the animal is infected.
     * @return true if the animal is infected, false otherwise.
     */
    public boolean isInfected()
    {
        return infected;
    }
    
    /**
     * Set the infection status of the animal.
     * @param infected The infection status of the animal.
     */
    protected void setInfected(boolean infected)
    {
        this.infected = infected;
    }

    /**
     * Handle the infection from animal to animal by checking if already infected or there is a chance.
     */
    protected void handleInfection()
    {
        if(!infected && rand.nextDouble() <= INFECTION_PROBABILITY) {
            infected = true;
        }
    }

        protected void handleSpread(Field field) {
        if (infected) {
            List<Location> adjacent = field.getAdjacentLocations(getLocation());
            for (Location loc : adjacent) {
                Organism organism = field.getOrganismAt(loc);
                if (organism instanceof Animal other && !other.isInfected()) {
                    if (rand.nextDouble() <= SPREAD_PROBABILITY) {
                        other.setInfected(true);
                    }
                }
            }
        }
    }
}
