import bagel.Image;
import bagel.util.Rectangle;

public class Tree extends Obstacle {
    // Fixed attribute
    private final Image TREE_IMAGE = new Image("res/tree.png");

    // Setter for tree
    public Tree(double x, double y) {
        super(x, y);
    }

    @Override
    public void draw() {
        TREE_IMAGE.drawFromTopLeft(position.x, position.y);
    }

    @Override
    public Rectangle getBoundingBox(){
        return new Rectangle(position, TREE_IMAGE.getWidth(), TREE_IMAGE.getHeight());
    }
}
