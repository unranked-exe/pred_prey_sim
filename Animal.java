
/**
 * Common elements of all organisms in the simulation.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Animal
{

    // Gender enum
    public enum Gender {
        MALE,FEMALE
    }

    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    // The animal's gender.
    private Gender gender;
    // Allows for each animal to access a single instance of the simulator
    protected static Simulator simulator;


    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(Location location)
    {
        this.alive = true;
        this.location = location;
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
     * Set the simulator instance to be used by all animals.
     * @param sim SImultor instance to be set.
     */

    public static void setSimulator(Simulator sim) {
        simulator = sim;
    }

    /**
     * Get the static simulator instance.
     * @return The simulator instance.
     */
    protected static Simulator getSimulator()
    {
        return simulator;
    }
    
    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState);
    
    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }
    
    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Set the animal's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }
}
