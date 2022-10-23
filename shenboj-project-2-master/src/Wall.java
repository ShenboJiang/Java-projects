import bagel.Image;
import bagel.util.Rectangle;

public class Wall extends Obstacle {
    // Fixed attribute
    private final Image wall = new Image("res/wall.png");

    // Setter for wall
    public Wall(double x, double y){
        super(x, y);
    }

    @Override
    public void draw() {
        wall.drawFromTopLeft(position.x, position.y);
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(position, wall.getWidth(), wall.getHeight());
    }
}
