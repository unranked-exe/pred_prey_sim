import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * fish, barracudas, sharks and plants
 * 
 * @author David J., Aman H, Chris M
 * @version 7.1
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;

    // Population probabilities
    private static final double SHARK_CREATION_PROBABILITY = 0.02;      // Fewer apex predators
    private static final double BARRACUDA_CREATION_PROBABILITY = 0.02;  // Medium predators
    private static final double TUNA_CREATION_PROBABILITY = 0.06;       // More prey
    private static final double GOLDFISH_CREATION_PROBABILITY = 0.025;   // Increased prey population
    private static final double PARROTFISH_CREATION_PROBABILITY = 0.025; // Increased prey population
    
    // Food Source Probabilities
    private static final double ALGAE_CREATION_PROBABILITY = 0.0175;
    private static final double SEAWEED_CREATION_PROBABILITY = 0.0175;

    // Time of day constants
    private static final int DAY_START = 5;
    private static final int DAY_END = 24;
    private static final int HOURS_PER_STEP = 1;

    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // The current time of day in the simulation.
    private int timeOfDay;
    // A graphical view of the simulation.
    private final SimulatorView view;
    // Weather
    private Weather weather;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        field = new Field(depth, width);
        view = new SimulatorView(depth, width, this);
        weather = new Weather();

        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (700 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }
    
    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        reportStats();
        for(int n = 1; n <= numSteps && field.isViable(); n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each organism.
     * Checks for infection and spread of infection if current organism is an animal.
     * Grows plants in preparation for the next field state.
     */
    public void simulateOneStep()
    {
        step++;
        timeOfDay = (timeOfDay + HOURS_PER_STEP) % DAY_END;
        weather.update();
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());    
        List<Organism> organisms = field.getOrganisms();
        for (Organism anOrganism : organisms) {
            if (anOrganism instanceof Animal animal) {
                animal.handleInfection();
                animal.handleSpread(field);
            }
            
            anOrganism.act(field, nextFieldState);
        }
        Organism.growPlants(nextFieldState);
        field = nextFieldState;
        reportStats();
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public final void reset()
    {
        step = 0;
        timeOfDay = DAY_START;
        
        populate();
        view.showStatus(step, field);
    }
    

    /**
     * Get the current hour of the simulation
     * in 24-hour format.
     * @return The current hour of the simulation.
     */
    public int getTimeOfDay() {
        return timeOfDay;
    }

    /**
     * Get the current weather state from Weather class
     * @return The current weather of the simulation.
     */
    public Weather getWeather() {
        return weather;
    }

    /**
     * Randomly populate the field with barracudas, sharks, fish and plants.
     * Called at start and reset
     */
    private void populate()
    {
        Organism.setSimulator(this);
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                
                if(rand.nextDouble() <= SHARK_CREATION_PROBABILITY) {
                    Shark shark = new Shark(true, location);
                    field.placeOrganism(shark, location);
                }
                else if(rand.nextDouble() <= BARRACUDA_CREATION_PROBABILITY) {
                    Barracuda barracuda = new Barracuda(true, location);
                    field.placeOrganism(barracuda, location);
                }
                else if(rand.nextDouble() <= TUNA_CREATION_PROBABILITY) {
                    Tuna tuna = new Tuna(true, location);
                    field.placeOrganism(tuna, location);
                }
                else if(rand.nextDouble() <= GOLDFISH_CREATION_PROBABILITY) {
                    Goldfish goldfish = new Goldfish(true, location);
                    field.placeOrganism(goldfish, location);
                }
                else if(rand.nextDouble() <= PARROTFISH_CREATION_PROBABILITY) {
                    Parrotfish parrotfish = new Parrotfish(true, location);
                    field.placeOrganism(parrotfish, location);
                }
                else if(rand.nextDouble() <= ALGAE_CREATION_PROBABILITY) {
                    Algae algae = new Algae(location);
                    field.placeOrganism(algae, location);
                }
                else if(rand.nextDouble() <= SEAWEED_CREATION_PROBABILITY) {
                    Seaweed seaweed = new Seaweed(location);
                    field.placeOrganism(seaweed, location);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        field.fieldStats();
    }
    
    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }

    /**
     * Main method to run the simulation.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args)
    {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }
}