import java.util.*;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * fish, barracudas, and sharks.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a barracuda will be created in any given grid position.
    private static final double BARRACUDA_CREATION_PROBABILITY = 0.02;
    // The probability that a shark will be created in any given grid position.
    private static final double SHARK_CREATION_PROBABILITY = 0.01;
    // The probability that a goldfish will be created in any given position.
    private static final double GOLDFISH_CREATION_PROBABILITY = 0.08;  
    // The probability that a tuna will be created in any given position.
    private static final double TUNA_CREATION_PROBABILITY = 0.015;
    // The probability that a parrotfish will be created in any given position.
    private static final double PARROTFISH_CREATION_PROBABILITY = 0.06;    


    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;

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
        view = new SimulatorView(depth, width);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
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
     * Iterate over the whole field updating the state of each animal.
     */
    public void simulateOneStep()
    {
        step++;
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());

        List<Animal> animals = field.getAnimals();
        for (Animal anAnimal : animals) {
            anAnimal.act(field, nextFieldState);
        }
        
        field = nextFieldState;
        reportStats();
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        populate();
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with barracudas, sharks and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                
                if(rand.nextDouble() <= SHARK_CREATION_PROBABILITY) {
                    Shark shark = new Shark(true, location);
                    field.placeAnimal(shark, location);
                }
                else if(rand.nextDouble() <= BARRACUDA_CREATION_PROBABILITY) {
                    Barracuda barracuda = new Barracuda(true, location);
                    field.placeAnimal(barracuda, location);
                }
                else if(rand.nextDouble() <= TUNA_CREATION_PROBABILITY) {
                    Tuna tuna = new Tuna(true, location);
                    field.placeAnimal(tuna, location);
                }
                else if(rand.nextDouble() <= GOLDFISH_CREATION_PROBABILITY) {
                    Goldfish goldfish = new Goldfish(true, location);
                    field.placeAnimal(goldfish, location);
                }
                else if(rand.nextDouble() <= PARROTFISH_CREATION_PROBABILITY) {
                    Parrotfish parrotfish = new Parrotfish(true, location);
                    field.placeAnimal(parrotfish, location);
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