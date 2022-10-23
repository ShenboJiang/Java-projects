import bagel.Image;
import bagel.util.Rectangle;


public class Demon extends Enemy{

    // Fixed attributes
    private final static int DAMAGE = 10;
    private final static int RANGE = 150;
    private final static int MAX_HEALTH = 40;
    private final static int THREESEC = 180;


    // Image
    private Image Demon_Image;
    protected final static int ORANGE_BOUND = 26;
    protected final static int RED_BOUND = 14;
    private final static String DEMON_LEFT = "res/demon/demonLeft.png";
    private final static String DEMON_RIGHT = "res/demon/demonRight.png";
    private final static String DEMON_INV_LEFT = "res/demon/demonInvincibleLeft.PNG";
    private final static String DEMON_INV_RIGHT = "res/demon/demonInvincibleRight.PNG";
    private final static String DEMON_FIRE = "res/demon/demonFire.png";




    public Demon(double x, double y) {
        super(x, y);
        health = MAX_HEALTH;
        isInvincible = false;
        isDead = false;
        isAggressive = rand.nextBoolean();
        Fire = new Image(DEMON_FIRE);

        if(isAggressive) {
            direction = rand.nextInt(4);
            Demon_Image = startImage(direction);
            speed = rand.nextDouble()* 0.5 + 0.2;
        }
        else {
            Demon_Image = startImage(PASSIVE);
        }
    }

    @Override
    public void update(ShadowDimension gameObject) {
        if(!isDead){
            if(isAggressive) {
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
            }
            if(isInvincible){
                if(invTimer(THREESEC) == THREESEC) {
                    isInvincible = false;
                    invTime = 0;
                }
                if(faceRight){
                    Demon_Image = new Image(DEMON_INV_RIGHT);
                }
                if (!faceRight) {
                    Demon_Image = new Image(DEMON_INV_LEFT);
                }
            }
            if(!isInvincible){
                if(faceRight) {
                    Demon_Image = new Image(DEMON_RIGHT);
                }
                else {
                    Demon_Image = new Image(DEMON_LEFT);
                }
            }
            Demon_Image.drawFromTopLeft(this.x, this.y);
            gameObject.enemyCollision(this);
            renderHP();
        }
    }

    @Override
    public Rectangle getHitBox() {
        return new Rectangle(x, y, Demon_Image.getWidth(), Demon_Image.getHeight());
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
        if(bound == TL && !isDead) {
            Fire.drawFromTopLeft(x - Fire.getWidth()
                    , y - Fire.getHeight());
            return new Rectangle(x - Fire.getWidth(), y - Fire.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        if(bound == TR && !isDead) {
            Fire.drawFromTopLeft(x + Demon_Image.getWidth()
                    , y - Fire.getHeight(), TR_ROTATE);
            return new Rectangle(x + Demon_Image.getWidth(), y - Fire.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        if(bound == BL && !isDead) {
            Fire.drawFromTopLeft(x - Fire.getWidth()
                    , y + Demon_Image.getHeight(), BL_ROTATE);
            return new Rectangle(x - Fire.getWidth(), y + Demon_Image.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        if(bound == BR && !isDead) {
            Fire.drawFromTopLeft(x + Demon_Image.getWidth()
                    , y + Demon_Image.getHeight(), BR_ROTATE);
            return new Rectangle(x + Demon_Image.getWidth(), y + Demon_Image.getHeight()
                    , Fire.getWidth(), Fire.getHeight());
        }
        return null;
    }
    @Override
    public void rebound() {
        this.speed = speed * -1;
        if(faceRight && !isUpDown) {
            Demon_Image = new Image(DEMON_LEFT);
            faceRight = false;
        }
        else if(!faceRight && !isUpDown){
            Demon_Image = new Image(DEMON_RIGHT);
            faceRight = true;
        }
    }

    @Override
    public Image startImage(int direction) {
        if(direction == LEFT) {
            faceRight = false;
            return new Image(DEMON_LEFT);
        }
        if(direction == RIGHT) {
            faceRight = true;
            return new Image(DEMON_RIGHT);
        }
        else {
            isUpDown = true;
            if(rand.nextInt(2) == 0) {
                return new Image(DEMON_LEFT);
            }
            else {
                return new Image(DEMON_RIGHT);
            }

        }

    }



    @Override
    public double getCenterX(){
        return this.x+(Demon_Image.getWidth()/2);
    }

    @Override
    public double getCenterY(){
        return this.y+(Demon_Image.getHeight()/2);
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
