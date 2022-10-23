import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

/**
 * class of player
 */
public class Player {
    // Fixed attributes of player
    private final static int SPEED = 2;
    private final static int DAMAGE = 20;
    private final static int MAX_HEALTH = 100;
    private final static int ATKTIME = 0;
    private final static int ATKCOOLDOWN = 1;
    private final static int INVTIME = 2;
    private final static int RESET_TIME = 0;
    private final static int ONESEC = 60;
    private final static int TWOSEC = 120;
    private final static int THREESEC = 180;

    // Player
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static int ORANGE_BOUND = 65;
    private final static int RED_BOUND = 35;
    private final static String ATTACK_LEFT = "res/fae/faeAttackLeft.png";
    private final static String ATTACK_RIGHT = "res/fae/faeAttackRight.png";


    // Text
    private final Font font = new Font("res/frostbite.ttf", 30);
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);


    // Initialize player attributes
    private double x, y;
    private int Health;
    private Image currImage;
    private Point prevPosition;
    private boolean facingRight;
    private boolean isInvincible;
    private boolean isAttack;
    private boolean isRightAttack;
    private boolean onCooldown = false;
    static int atktime;
    static int atkcooldown;
    static int invtime;


    /**
     * Setter for location and defaults to full health
     */

    Player(double start_x, double start_y){
        x = start_x;
        y = start_y;
        Health = 100;
        currImage = new Image(FAE_RIGHT);
        isInvincible = false;
        facingRight = true;
    }

    /**
     * Calculate amount of health
     */

    public void takeDamage(int received){
        Health = Health - received;
        isInvincible = true;
        if (this.Health < 0){
            this.Health = 0;
        }


    }

    /**
     * Getter for amount of damage points per damage
     */

    public int getDamage(){
        return DAMAGE;
    }

    /**
     * updates the player
     */
    public void update(Input input, ShadowDimension gameObject){
        if (input.isDown(Keys.UP)){
            setPrevPosition();
            this.y -= SPEED;
        }
        else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            this.y += SPEED;
        }
        else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            facingRight = false;

            this.x -= SPEED;

            if(!isAttack){
                this.currImage = new Image(FAE_LEFT);
            }
        }
        else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            facingRight = true;

            this.x += SPEED;

            if(!isAttack){
                this.currImage = new Image(FAE_RIGHT);
            }
        }

        // Attack and invincibility
        if(input.isDown(Keys.A) && !onCooldown) {
            isAttack = true;
            onCooldown = true;
            if(facingRight){
                currImage = new Image(ATTACK_RIGHT);
                isRightAttack = true;
            }
            else {
                currImage = new Image(ATTACK_LEFT);
                isRightAttack = false;
            }
        }
        // Attacking
        if(isAttack){
            if(timer(ATKTIME, ONESEC) == ONESEC) {
                isAttack = false;
                atktime = RESET_TIME;
            }
            if(isRightAttack && input.isDown(Keys.LEFT)){
                isAttack = false;
                atktime = RESET_TIME;
            }
            if(!isRightAttack && input.isDown(Keys.RIGHT)){
                isAttack = false;
                atktime = RESET_TIME;
            }
        }
        // Attack animation finished
        if(!isAttack){
            if(facingRight){
                currImage = new Image(FAE_RIGHT);
            }
            if(!facingRight){
                currImage = new Image(FAE_LEFT);
            }
        }
        // Attack cool down
        if(onCooldown){
            if(timer(ATKCOOLDOWN, TWOSEC) == TWOSEC){
                onCooldown = false;
                atkcooldown = RESET_TIME;
            }
        }
        // invincible
        if(isInvincible){
            if(timer(INVTIME, THREESEC) == THREESEC){
                isInvincible = false;
                invtime = RESET_TIME;
            }
        }
        currImage.drawFromTopLeft(x, y);
        gameObject.checkCollisions(this);
        renderHealthPoints();
        gameObject.checkOutOfBounds(this);

    }

    /**
     * Method that stores Fae's previous position
     */
    private void setPrevPosition(){
        this.prevPosition = new Point(x, y);
    }

    /**
     * Method that moves Fae back to previous position
     */
    public void moveBack(){
        x = prevPosition.x;
        y = prevPosition.y;
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    private void renderHealthPoints(){
        double percentageHP = ((double) Health/MAX_HEALTH) * 100;
        if(percentageHP > ORANGE_BOUND){
            COLOUR.setBlendColour(GREEN);
        }
        else if (percentageHP <= RED_BOUND){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUND){
            COLOUR.setBlendColour(ORANGE);
        }
        font.drawString(Math.round(percentageHP) + "%", HEALTH_X, HEALTH_Y, COLOUR);
    }

    public Image getCurrentImage() {
        return currImage;
    }

    public static int getMaxHealth(){
        return MAX_HEALTH;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getCenterX(){ return this.x+(currImage.getWidth()/2); }

    public double getCenterY(){ return this.y+(currImage.getHeight()/2); }

    // Health getter
    public int getHealth() {
        return Health;
    }

    public boolean isInvincible(){
        return isInvincible;
    }

    public boolean isAttack(){
        return isAttack;
    }

    /**
     * timer for different attributes
     */
    public int timer(int mode, int frames) {
        if(mode == ATKTIME){
            if(atktime < frames) {
                atktime++;
            }
            return atktime;
        }
        if(mode == ATKCOOLDOWN){
            if(atkcooldown< frames) {
                atkcooldown++;
            }
            return atkcooldown;
        }
        if(mode == INVTIME){
            if(invtime< frames) {
                invtime++;
            }
            return invtime;
        }
        return -1;
    }
}
