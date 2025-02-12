import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal/object.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The dimensions of the field.
    private final int depth, width;
    // Animals mapped by location.
    private final Map<Location, Organism> field = new HashMap<>();
    // The animals.
    private final List<Organism> organisms = new ArrayList<>();

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place an organism at the given location.
     * If there is already an organism at the location it will
     * be lost.
     * Must be called even if organism does not move to allow persistance into next state.
     * @param anOrganism The organism to be placed.
     * @param location Where to place the organism.
     */
    public void placeOrganism(Organism anOrganism, Location location)
    {
        assert location != null;
        Organism other = field.get(location);
        if(other != null) {
            organisms.remove(other);
        }
        field.put(location, anOrganism);
        organisms.add(anOrganism);
        if (getAnimalAt(location).getClass() == Tuna.class){
            //System.out.println("TUNANANANAAAAAAA why u not working!!!!!!");
        }
    }
    
    /**
     * Return the animal at the given location, if any.
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Organism getAnimalAt(Location location)
    {
        return field.get(location);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {
            Organism anAnimal = field.get(next);
            if(anAnimal == null) {
                free.add(next);
            }
            else if(!anAnimal.isAlive()) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

/**
 * Print out the number of all species in the field.
 */
public void fieldStats()
{
    int numSharks = 0, numBarracudas = 0, numGoldfish = 0;
    int numTuna = 0, numParrotfish = 0;
    
    for(Organism anOrganism : field.values()) {
        switch (anOrganism) {
            case Shark shark -> {
                if(shark.isAlive()) {
                    numSharks++;
                }
            }
            case Barracuda barracuda -> {
                if(barracuda.isAlive()) {
                    numBarracudas++;
                }
            }
            case Tuna tuna -> {
                if(tuna.isAlive()) {
                    numTuna++;
                }
            }
            case Goldfish goldfish -> {
                if(goldfish.isAlive()) {
                    numGoldfish++;
                }
            }
            case Parrotfish parrotfish -> {
                if(parrotfish.isAlive()) {
                    numParrotfish++;
                }
            }
            default -> {
                
            }
        }
    }
    System.out.println("Goldfish: " + numGoldfish +
                     " Barracudas: " + numBarracudas +
                     " Sharks: " + numSharks +
                     " Tuna: " + numTuna +
                     " Parrotfish: " + numParrotfish);
}
 
     /**
      * Empty the field.
      */
     public void clear()
     {
         field.clear();
         organisms.clear();
     }
 
    /**
 * Return whether there is at least one of each species in the field.
 * @return true if there is at least one of each species in the field.
 */
public boolean isViable()
{
    boolean goldfishFound = false;
    boolean barracudaFound = false;
    boolean sharkFound = false;
    boolean tunaFound = false;
    boolean parrotfishFound = false;
    
    Iterator<Organism> it = organisms.iterator();
    while(it.hasNext() &&
          !(goldfishFound && barracudaFound && sharkFound && 
            tunaFound && parrotfishFound)) {
        Organism check = it.next();
        if (check instanceof Animal){
            Animal anAnimal = (Animal) check;
            switch (anAnimal) {
                case Goldfish goldfish -> {
                    if(goldfish.isAlive()) {
                        goldfishFound = true;
                    }
                }
                case Barracuda barracuda -> {
                    if(barracuda.isAlive()) {
                        barracudaFound = true;
                    }
                }
                case Shark shark -> {
                    if(shark.isAlive()) {
                        sharkFound = true;
                    }
                }
                case Tuna tuna -> {
                    if(tuna.isAlive()) {
                        tunaFound = true;
                    }
                }
                case Parrotfish parrotfish -> {
                    if(parrotfish.isAlive()) {
                        parrotfishFound = true;
                    }
                }
                default -> {
                }
            }
        }
    }
    return goldfishFound && barracudaFound && sharkFound && 
           tunaFound && parrotfishFound;
}
    
    /**
     * Get the list of animals.
     */
    public List<Organism> getOrganisms()
    {
        return organisms;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
}
