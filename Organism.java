
/**
 * Write a description of class Organism here.
 *
 * @author Chris M
 * @version (a version number or a date)
 */
public abstract class Organism
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    // Allows for each organsism to access a single instance of the simulator
    protected static Simulator simulator;

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
     * Set the simulator instance to be used by all animals.
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
}
