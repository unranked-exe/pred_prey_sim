
/**
 * Common elements of all organisms in the simulation.
 *
 * @author David J. Barnes and Michael Kölling
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

    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(Location location)
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
}
