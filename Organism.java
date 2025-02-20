import java.util.Random;

/**
 * An abstract class representing an organism in the simulation.
 * All animals and plants inherit properties from this class.
 * Each organism has a location in the field and can be either alive or dead.
 * 
 * @author Aman H, Chris M
 */
public abstract class Organism
{
    // Allows for each organsism to access a single instance of the simulator
    protected static Simulator simulator;
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();

    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;

    /**
     * Constructor for objects of class Animal.
     * @param location The organism's location.
     */
    public Organism(Location location)
    {
        this.alive = true;
        this.location = location;
    }

    /**
     * Set the simulator instance to be used by all organisms.
     * @param sim Simultor instance to be set.
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
     * Check whether the organism is alive or not.
     * @return true if the organism is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the organism is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }
    
    /**
     * Return the organisms's location.
     * @return The organism's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Set the organism's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }

    /**
     * Calls the grow method for all plants in the field.
     * @param nextFieldState The new state being built.
     */
    public static void growPlants(Field nextFieldState)
    {
        Plant.checkGrow(nextFieldState);
    }
}
