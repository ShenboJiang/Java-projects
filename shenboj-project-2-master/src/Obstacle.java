import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * class for Obstacle
 */
public abstract class Obstacle {
    protected Point position;

    /**
     * setter for obstacles
     */
    public Obstacle(double x, double y) {
        this.position = new Point(x, y);
    }

    /**
     * render the obstacle
     */
    public abstract void draw();

    /**
     * Get hitbox of obstacles
     * @return hitbox
     */
    public abstract Rectangle getBoundingBox();
}
