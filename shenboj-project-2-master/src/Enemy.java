import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Rectangle;
import bagel.Image;

import java.util.Random;

/**
 * abstract class of enemy that is the parent of Navec and Demon
 */
public abstract class Enemy {
    // Position
    protected double x, y;

    // Movement
    protected Random rand = new Random();
    protected final static int UP = 0;
    protected final static int DOWN = 1;
    protected final static int LEFT = 2;
    protected final static int RIGHT = 3;
    protected final static int PASSIVE = 4;
    protected int direction;
    protected double speed;
    protected boolean faceRight;
    protected boolean isUpDown;
    protected boolean isAggressive;

    // Fire
    protected final static int TL = 5;
    protected final static int TR = 6;
    protected final static int BL = 7;
    protected final static int BR = 8;
    protected final static DrawOptions TR_ROTATE = new DrawOptions().setRotation(1.571);
    protected final static DrawOptions BL_ROTATE = new DrawOptions().setRotation(4.712);
    protected final static DrawOptions BR_ROTATE = new DrawOptions().setRotation(3.142);
    protected Image Fire;


    // Font
    protected final static DrawOptions COLOUR = new DrawOptions();
    protected final static Colour GREEN = new Colour(0, 0.8, 0.2);
    protected final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    protected final static Colour RED = new Colour(1, 0, 0);
    protected final static int FONTSIZE = 15;
    protected final Font healthFont = new Font("res/frostbite.ttf", FONTSIZE);


    // Health
    protected int health;
    protected double healthPercentage;
    protected final static int HP_Y_OFFSET = -6;
    protected static int invTime = 0;
    protected boolean isInvincible;
    protected boolean isDead;

    // Time scale
    protected final static double SPEEDUP = 1.5;
    protected final static double SPEEDDOWN = 0.5;

    /**
     * Setter for location of enemy
     * @param x x coordinate
     * @param y y coordianate
     */
    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Performs update of Navec
     * @param gameObject other objects of the game
     */
    public abstract void update(ShadowDimension gameObject);

    /**
     * Getter for the hitbox of navec
     * @return hitbox
     */
    public abstract Rectangle getHitBox();

    /**
     * Hp render
     */
    public abstract void renderHP();

    /**
     * Method to generate fire hitbox for different quadrants
     * @param bound input of the direciton
     * @return hitbox of fire
     */
    public abstract Rectangle inAttackRange(int bound);

    /**
     * Rebound of navec when needed
     */
    public abstract void rebound();

    /**
     * set the proper image for the initial start
     * @param direction direciton
     * @return image
     */
    public abstract Image startImage(int direction);

    /**
     * getter for centre x of navec
     * @return x coordinates
     */
    public abstract double getCenterX();

    /**
     * getter for centre y of navec
     * @return y coordinates
     */
    public abstract double getCenterY();

    /**
     * getter for range of navec
     * @return range
     */
    public abstract int getRange();

    /**
     * getter for damage of navec
     * @return damage
     */
    public abstract int getDamage();


    /**
     * getter for health
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * method for when enemy takes damage
     * @param currHealth current health
     * @param damage damage dealt
     */
    public void takeDamage(int currHealth, int damage) {
        this.health = currHealth - damage;
        if (health < 0){
            health = 0;
        }

        if (health == 0){
            isDead = true;
        }
        isInvincible = true;
    }

    /**
     * timer for pauses
     * @param frames amount of time wanted fot pause
     * @return time passed
     */
    public int invTimer(int frames) {
        if(invTime < frames) {
            invTime++;
        }
        return invTime;
    }

    /**
     * getter for invincibility status
     * @return invincibility
     */
    public boolean getIsInvincible() {
        return isInvincible;
    }

    /**
     * getter for the enemy being dead or not
     * @return boolean whether it is dead
     */
    public boolean getIsDead() {
        return isDead;
    }

    /**
     * Speed up time (speed)
     */
    public void scaleUp(){
        speed *= SPEEDUP;
    }

    /**
     * Speed down time (speed)
     */
    public void scaleDown(){
        speed *= SPEEDDOWN;
    }
}
