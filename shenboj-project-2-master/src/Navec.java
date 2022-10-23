
import bagel.Image;
import bagel.util.Rectangle;

/**
 * class for Navec
 */
public class Navec extends Enemy{
    // Fixed variable
    private final static int DAMAGE = 20;
    private final static int RANGE = 200;
    private final static int MAX_HEALTH = 80;
    private final static int THREESEC = 180;

    // Image
    private Image Navec_Image;
    private final static int ORANGE_BOUND = 52;
    private final static int RED_BOUND = 28;
    private final static String NAVEC_LEFT = "res/navec/navecLeft.png";
    private final static String NAVEC_RIGHT = "res/navec/navecRight.png";
    private final static String NAVEC_INV_LEFT = "res/navec/navecInvincibleLeft.PNG";
    private final static String NAVEC_INV_RIGHT = "res/navec/navecInvincibleRight.PNG";
    private final static String NAVEC_FIRE = "res/navec/navecFire.png";



    public Navec(double x, double y) {
        super(x, y);
        health = MAX_HEALTH;
        isInvincible = false;
        isDead = false;
        Fire = new Image(NAVEC_FIRE);

        // Random one of four diretion
        direction = rand.nextInt(4);
        Navec_Image = startImage(direction);
        // Random of 0.2 to 0.7
        speed = rand.nextDouble()* 0.5 + 0.2;
    }


    @Override
    public void update(ShadowDimension gameObject) {
        if(!isDead){
            if(direction == LEFT) {
                this.x -= speed;
            }
            else if(direction == RIGHT) {
                this.x += speed;
            }
            else if(direction == UP) {
                this.y -= speed;
            }
            else if(direction == DOWN) {
                this.y += speed;
            }
            // Invincible
            if(isInvincible){
                if(invTimer(THREESEC) == THREESEC) {
                    isInvincible = false;
                    invTime = 0;
                }
                if(faceRight){
                    Navec_Image = new Image(NAVEC_INV_RIGHT);
                }
                if (!faceRight) {
                    Navec_Image = new Image(NAVEC_INV_LEFT);
                }
            }
            // Out of invincible
            if(!isInvincible){
                if(faceRight) {
                    Navec_Image = new Image(NAVEC_RIGHT);
                }
                else {
                    Navec_Image = new Image(NAVEC_LEFT);
                }
            }
            // Render the image of navec
            Navec_Image.drawFromTopLeft(this.x, this.y);

            // Collision logic
            gameObject.enemyCollision(this);
            renderHP();
        }

    }


    @Override
    public Rectangle getHitBox() {
        return new Rectangle(x, y, Navec_Image.getWidth(), Navec_Image.getHeight());
    }


    @Override
    public void renderHP() {
        if(health > ORANGE_BOUND){
            COLOUR.setBlendColour(GREEN);
        }
        else if (health <= RED_BOUND){
            COLOUR.setBlendColour(RED);
        } else if (health <= ORANGE_BOUND){
            COLOUR.setBlendColour(ORANGE);
        }
        healthPercentage = ((double)health/MAX_HEALTH)*100;
        healthFont.drawString(Math.round(healthPercentage) + "%", this.x, this.y + HP_Y_OFFSET, COLOUR);
    }


    @Override
    public Rectangle inAttackRange(int bound) {
        // Top left
        if(bound == TL && !isDead) {
            Fire.drawFromTopLeft(x - Fire.getWidth()
                    , y - Fire.getHeight());
            return new Rectangle(x - Fire.getWidth(), y - Fire.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        // Top right
        if(bound == TR && !isDead) {
            Fire.drawFromTopLeft(x + Navec_Image.getWidth()
                    , y - Fire.getHeight(), TR_ROTATE);
            return new Rectangle(x + Navec_Image.getWidth(), y - Fire.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        // Bottom Left
        if(bound == BL && !isDead) {
            Fire.drawFromTopLeft(x - Fire.getWidth()
                    , y + Navec_Image.getHeight(), BL_ROTATE);
            return new Rectangle(x - Fire.getWidth(), y + Navec_Image.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        // Bottom right
        if(bound == BR && !isDead) {
            Fire.drawFromTopLeft(x + Navec_Image.getWidth()
                    , y + Navec_Image.getHeight(), BR_ROTATE);
            return new Rectangle(x + Navec_Image.getWidth(), y + Navec_Image.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        return null;
    }


    @Override
    public void rebound() {
        this.speed = speed * -1;
        if(faceRight && !isUpDown) {
            Navec_Image = new Image(NAVEC_LEFT);
            faceRight = false;
        }
        else if(!faceRight && !isUpDown){
            Navec_Image = new Image(NAVEC_RIGHT);
            faceRight = true;
        }
    }


    @Override
    public Image startImage(int direction) {
        if(direction == LEFT) {
            faceRight = false;
            return new Image(NAVEC_LEFT);
        }
        if(direction == RIGHT) {
            faceRight = true;
            return new Image(NAVEC_RIGHT);
        }
        else {
            isUpDown = true;
            if(rand.nextInt(2)  + 2 == LEFT) {
                return new Image(NAVEC_LEFT);
            }
            else {
                return new Image(NAVEC_RIGHT);
            }

        }
    }


    @Override
    public double getCenterX() {
        return this.x+(Navec_Image.getWidth()/2);
    }


    @Override
    public double getCenterY() {
        return this.y+(Navec_Image.getHeight()/2);
    }


    @Override
    public int getRange(){
        return RANGE;
    }


    @Override
    public int getDamage(){
        return DAMAGE;
    }
}
