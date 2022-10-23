import bagel.Image;
import bagel.util.Rectangle;

public class Sinkhole extends Obstacle{
    // Fixed attributes
    private final Image sink = new Image("res/sinkhole.png");
    private final static int DAMAGE = 30;

    // Initialize attribute

    private boolean isActive;



    // Setter for sinkhole
    public Sinkhole(double x, double y){
        super(x, y);
        isActive = true;
    }

    @Override
    public void draw() {
        if (isActive) {
            sink.drawFromTopLeft(this.position.x, this.position.y);
        }
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(position, sink.getWidth(), sink.getHeight());
    }

    public boolean isActive() {
        return isActive;
    }

    public int getDamage(){
        return DAMAGE;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
