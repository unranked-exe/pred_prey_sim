public class Plant {
    private boolean alive;
    private Location location;
    private int growth;
    private static final int MAX_GROWTH = 10;
    private static final int GROWTH_RATE = 1;

    public Plant(Location location) {
        this.alive = true;
        this.location = location;
        this.growth = 1;
    }

    public void grow(Field currentField) {
        if (alive && growth < MAX_GROWTH && Math.random() < GROWTH_RATE) {
            growth++;
        }
    }

    public boolean isAlive() {
        return alive;
    }
    
    public void setDead() {
        alive = false;
        location = null;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public int getGrowth() {
        return growth;
    }
    
}
