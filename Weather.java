/**
 * Weather class that represents the weather in the simulation.
 * The weather can be sunny, rainy, cloudy or foggy.
 * The weather can change randomly.
 * 
 * @author Aman H
 */
import java.util.Random;

public class Weather {
    public enum Condition {
        SUNNY, RAINY, CLOUDY, FOGGY
    }

    private static final double WEATHER_CHANGE_PROBABILITY = 0.1;
    private Condition condition;
    private final Random rand;

    public Weather() {
        rand = Randomizer.getRandom();
        condition = Condition.SUNNY;
    }

    public void update() {
        if (rand.nextDouble() <= WEATHER_CHANGE_PROBABILITY) {
            condition = Condition.values()[rand.nextInt(Condition.values().length)];
        }
    }

    public Condition getCondition() {
        return condition;
    }
    
}
