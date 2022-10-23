import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.*;




/**
 * Game Code for SWEN20003 Project 1, Semester 2, 2022
 *
 * Please enter your name below
 * Shenbo Jiang
 * Parts inspired by Project 1 solution on Canvas for SWEN20003
 */

public class ShadowDimension extends AbstractGame {
    // Window spec
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private Point topLeft;
    private Point botRight;
    private final static int WIN_X = 950;
    private final static int WIN_Y = 670;

    // Game objects
    private final static int NUMWALLS = 52;
    private final static int NUMSINKHOLES = 5;
    private final static int NUMTREES = 15;
    private final Wall[] wall = new Wall[NUMWALLS];
    private final Tree[] tree = new Tree[NUMTREES];
    private final Sinkhole[] sinkhole = new Sinkhole[NUMSINKHOLES];


    // Resources import
    private final Image BACKGROUND0_IMAGE = new Image("res/background0.png");
    private final Image BACKGROUND1_IMAGE = new Image("res/background1.png");
    private final static String DATAFILE0 = "res/level0.csv";
    private final static String DATAFILE1 = "res/level1.csv";


    // Font attributes
    private final static int TITLE_SIZE = 75;
    private final static int SUBTITLE_SIZE = 40;
    private final Font title  = new Font("res/frostbite.ttf", TITLE_SIZE);
    private final Font subtitle  = new Font("res/frostbite.ttf", SUBTITLE_SIZE);
    private final static double GAME_TITLE_X = 260;
    private final static double GAME_TITLE_Y = 250;
    private final static double START0_X = 350;
    private final static double START0_Y = 440;
    private final static double START1_X = 350;
    private final static double START1_Y = 350;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String LEVEL0_SUBTITLE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String LEVEL0_COMPLETE = "LEVEL COMPLETE!";
    private final static String LEVEL1_SUBTITLE = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private final static String LOSE_MESSAGE = "GAME OVER!";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";


    // Counter
    private static int wallCount = 0;
    private static int treeCount = 0;
    private static int sinkHoleCount = 0;
    private static int demonCount = 0;
    private static int time = 0;

    // Characters
    private Player fae;
    private Navec navec;
    private final static int NUMDEMON = 5;
    private final static Demon[] demon = new Demon[NUMDEMON];
    private final static int PLAYERMAXHEALTH = 100;
    private final static int DEMONMAXHEALTH = 40;
    private final static int NAVECMAXHEALTH = 80;

    // Game state definition
    private final static int STARTSCREEN = 0;
    private final static int LV0 = 1;
    private final static int LV0W = 2;
    private final static int LV1 = 3;
    private final static int LV1W = 4;
    private final static int LOSS = 5;
    private int StateofGame = STARTSCREEN;
    private final static int THREESEC = 180;


    // Direciton
    private final static int TL = 5;
    private final static int TR = 6;
    private final static int BL = 7;
    private final static int BR = 8;


    // Time scale
    private final static int MAX_SCALE = 3;
    private final static int MIN_SCALE = -3;
    private static int scale = 0;



    /**
     * Setter for the window of the game and accept the input for level 0
     */
    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        lineReader(DATAFILE0);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Read in one line from the csv file and pass into readCSV()
     * @param dataFile location of the csv file in the folder
     */
    private void lineReader(String dataFile){
        try(BufferedReader br = new BufferedReader(new FileReader(dataFile))){
            String line;
            while((line = br.readLine()) != null) {
                String[] worldData = line.split(",");
                readCSV(worldData[0], worldData[1], worldData[2]);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to create objects from a read world file
     * @param type The type of object of the csv line
     * @param x The x coordinate of the object
     * @param y The y coordinate of the object
     */
    private void readCSV(String type, String x, String y){
        // If the type is player, create the player at x and y and added collision model around.
        if (type.equals("Fae")) {
            fae = new Player(Double.parseDouble(x), Double.parseDouble(y));
        }
        // If the type is navec, intilaize navec
        if (type.equals("Navec")) {
            navec = new Navec(Double.parseDouble(x), Double.parseDouble(y));
        }
        // If the type is demon, append new demon object to the array of demons
        if(type.equals("Demon")) {
            demon[demonCount] = new Demon(Double.parseDouble(x), Double.parseDouble(y));
            demonCount++;
        }
        // If the type is wall, append new wall object to the array of walls
        if(type.equals("Wall")) {
            wall[wallCount] = new Wall(Double.parseDouble(x), Double.parseDouble(y));
            wallCount++;
        }
        // If the type is tree, append new wall object to the array of trees
        if(type.equals("Tree")) {
            tree[treeCount] = new Tree(Double.parseDouble(x), Double.parseDouble(y));
            treeCount++;
        }
        // If the type is sinkhole, append new sinkhole object to the array of sinkholes
        if(type.equals("Sinkhole")) {
            sinkhole[sinkHoleCount] = new Sinkhole(Double.parseDouble(x), Double.parseDouble(y));
            sinkHoleCount++;
        }
        // If they are the topleft and bottomright, set them to the coordinates for collision detection.
        if(type.equals("TopLeft")) {
            topLeft = new Point(Integer.parseInt(x), Integer.parseInt(y));
        }
        if(type.equals("BottomRight")) {
            botRight = new Point(Integer.parseInt(x), Integer.parseInt(y));
        }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input Take the input from keyboard
     */
    @Override
    protected void update(Input input) {

        // Close the game if escape is pressed
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // Game starting screen scenario
        if(StateofGame == STARTSCREEN){
            title.drawString(GAME_TITLE, GAME_TITLE_X, GAME_TITLE_Y);
            subtitle.drawString(LEVEL0_SUBTITLE, START0_X, START0_Y);
            if (input.isDown(Keys.SPACE)){
                StateofGame = LV0;
            }
        }
        // Level 0 running screen scenario
        if (StateofGame == LV0){

            // Background image display
            BACKGROUND0_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            // Load Sinkholes
            for(Sinkhole current: sinkhole) {
                current.draw();
            }
            // Load Walls
            for(Wall current: wall) {
                current.draw();
            }

            // Update the player
            fae.update(input, this);
        }

        // Pass Level 0
        if (fae.getX() >= WIN_X && fae.getY() >= WIN_Y && StateofGame == LV0){
            StateofGame = LV0W;
        }

        // Intermediate of level 0 and 1
        if (StateofGame == LV0W){
            if (timer(THREESEC) < THREESEC) {
                title.drawString(LEVEL0_COMPLETE, (WINDOW_WIDTH / 2.0) - (title.getWidth(LEVEL0_COMPLETE )/ 2.0),
                        (WINDOW_HEIGHT / 2.0) + (TITLE_SIZE / 2.0));
            }
            else {
                // After level 0 complete message shows for 3 seconds
                subtitle.drawString(LEVEL1_SUBTITLE, START1_X, START1_Y);
                if (input.isDown(Keys.SPACE)){
                    StateofGame = LV1;
                    readLevel1();
                }
            }
        }
        // Level 1
        if (StateofGame == LV1){
            BACKGROUND1_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

            // Load Sinkholes
            for(Sinkhole current: sinkhole) {
                current.draw();
            }
            // Load Trees
            for(Tree current: tree) {
                current.draw();
            }

            // Time scale modifications
            changeSpeed(input);

            // Update characters in level 1
            fae.update(input, this);
            navec.update(this);
            for (Demon current: demon){
                current.update(this);
            }

            // Win condition
            if (navec.getHealth() == 0){
                StateofGame = LV1W;
            }
        }

        // Lose condition
        if (fae.getHealth() == 0){
            StateofGame = LOSS;
        }

        // Win screen
        if(StateofGame == LV1W) {
            title.drawString(WIN_MESSAGE, (WINDOW_WIDTH/2.0) - (title.getWidth(WIN_MESSAGE)/2.0),
                    (WINDOW_HEIGHT/2.0) + (TITLE_SIZE/2.0));
        }

        // Lose Screen
        if(StateofGame == LOSS) {
            title.drawString(LOSE_MESSAGE, (WINDOW_WIDTH/2.0) - (title.getWidth(LOSE_MESSAGE)/2.0),
                    (WINDOW_HEIGHT/2.0) + (TITLE_SIZE/2.0));
        }
    }

    /**
     * Method that checks for collisions between player and other entity of the game, and performe different
     * actions
     * @param player Input the class of players
     */
    public void checkCollisions(Player player){
        // Player hitbox
        Rectangle faeBox = new Rectangle(player.getX(), player.getY(), player.getCurrentImage().getWidth(),
                player.getCurrentImage().getHeight());

        // Level 0
        if (StateofGame == LV0) {
            // Wall collision
            for (Wall current : wall) {
                Rectangle wallBox = current.getBoundingBox();
                if (faeBox.intersects(wallBox)) {
                    player.moveBack();
                }
            }
        }

        // Sinkhole collision
        for (Sinkhole hole : sinkhole){
            Rectangle holeBox = hole.getBoundingBox();
            if (hole.isActive() && faeBox.intersects(holeBox)){
                player.takeDamage(hole.getDamage());
                player.moveBack();
                hole.setActive(false);
                System.out.println("Sinkhole inflicts " + hole.getDamage() + " damage points on Fae. " +
                        "Fae's current health: " + player.getHealth() + "/" + Player.getMaxHealth());
            }
        }
        // Level 1
        if (StateofGame == LV1){
            // Tree collision
            for (Tree current : tree) {
                Rectangle treeBox = current.getBoundingBox();
                if (faeBox.intersects(treeBox)) {
                    player.moveBack();
                }
            }
            // Demon collision when player is attacking on it
            for (Demon current: demon){
                if (faeBox.intersects(current.getHitBox())
                        && player.isAttack() && !current.getIsInvincible()){
                    current.takeDamage(current.getHealth(), player.getDamage());
                    faeDamageOutput("Demon", player.getDamage(), current.getHealth());
                }
                enemyFireCollisions(current, faeBox, current.getRange());
            }

            // Navec collision when player is attacking it
            if(faeBox.intersects(navec.getHitBox())
                    && player.isAttack() && !navec.getIsInvincible()){
                navec.takeDamage(navec.getHealth(), player.getDamage());
                faeDamageOutput("Navec", player.getDamage(), navec.getHealth());
            }
            enemyFireCollisions(navec, faeBox, navec.getRange());
        }
    }

    /**
     * Performs updates on Navec interaction with stationary objects
     * @param enemy Input any of the child class of enemy
     */
    public void enemyCollision(Enemy enemy) {
        // Enemy hitbox
        Rectangle enemyHitBox = enemy.getHitBox();

        // Hits tree
        for(Tree current: tree) {
            if(enemyHitBox.intersects(current.getBoundingBox())) {
                enemy.rebound();
            }
        }

        // Hits sinkhole
        for(Sinkhole current: sinkhole) {
            if(enemyHitBox.intersects(current.getBoundingBox())) {
                enemy.rebound();
            }
        }

        // Hits border
        if (enemy.x <= topLeft.x || enemy.x >= botRight.x || enemy.y <= topLeft.y || enemy.y >= botRight.y) {
            enemy.rebound();
        }
    }

    /**
     * The interaction of enemy's fire and the player
     * @param enemy Input of any of the child class of enemy
     * @param faeHitBox Hitbox of player
     * @param range Range of the type of enemy
     */
    public void enemyFireCollisions(Enemy enemy, Rectangle faeHitBox, int range){
        if(!enemy.getIsDead()){
            // Player at top left of the enemy
            if(fae.getCenterX() <= enemy.getCenterX()
                    && fae.getCenterY() <= enemy.getCenterY()
                    && inRange(enemy, range)) {
                if(faeHitBox.intersects(enemy.inAttackRange(TL)) && !fae.isInvincible()) {
                    fae.takeDamage(enemy.getDamage());
                    enemyDamageOutput(enemy.getDamage());
                }
            }
            // Player at top right of the enemy
            if(fae.getCenterX() > enemy.getCenterX()
                    && fae.getCenterY() <= enemy.getCenterY()
                    && inRange(enemy, range)) {
                if(faeHitBox.intersects(enemy.inAttackRange(TR)) && !fae.isInvincible()) {
                    fae.takeDamage(enemy.getDamage());
                    enemyDamageOutput(enemy.getDamage());
                }
            }
            // Player at bottom left of the enemy
            if(fae.getCenterX() <= enemy.getCenterX()
                    && fae.getCenterY() > enemy.getCenterY()
                    && inRange(enemy, range)) {
                if(faeHitBox.intersects(enemy.inAttackRange(BL)) && !fae.isInvincible()) {
                    fae.takeDamage(enemy.getDamage());
                    enemyDamageOutput(enemy.getDamage());
                }
            }
            // Player at the bottom right of the enemy
            if(fae.getCenterX() > enemy.getCenterX()
                    && fae.getCenterY() > enemy.getCenterY()
                    && inRange(enemy, range)) {
                if(faeHitBox.intersects(enemy.inAttackRange(BR)) && !fae.isInvincible()) {
                    fae.takeDamage(enemy.getDamage());
                    enemyDamageOutput(enemy.getDamage());
                }
            }
        }
    }

    /**
     * Method that checks if Fae has gone out-of-bounds and performs corresponding action
     * @param player Input of the player class
     */
    public void checkOutOfBounds(Player player){
        if ((player.getY() > botRight.y) || (player.getY() < topLeft.y) || (player.getX() < topLeft.x)
                || (player.getX() > botRight.x)){
            player.moveBack();
        }
    }

    /**
     * Timer for pauses
     * @param frames Amount of time needed to pause
     * @return the time that has passed
     */
    private int timer(int frames){
        if(time < frames){
            time++;
        }
        return time;
    }

    /**
     * clears wall and sinkhole array and then read in level 1 data back
     */
    private void readLevel1(){
        fae = null;
        topLeft = null;
        botRight = null;
        wallCount = 0;
        sinkHoleCount = 0;
        for (Wall current : wall){
            current = null;
        }
        for (Sinkhole hole: sinkhole){
            hole = null;
        }
        lineReader(DATAFILE1);
    }

    /**
     * Method to test if player is in range of the enemy to trigger fire
     * @param enemy input of any of the child class of enemy
     * @param range range of the enemy
     * @return boolean value of in range or not
     */
    public boolean inRange(Enemy enemy, int range){
        return Math.pow((Math.pow(fae.getCenterX() - enemy.getCenterX(), 2)
                + Math.pow(fae.getCenterY() - enemy.getCenterY(), 2)), 0.5) <= range;
    }

    /**
     * Print the status update of enemy's damage to player to standard output
     * @param damage amount of damage dealt
     */
    public void enemyDamageOutput(int damage) {
        // From navec
        if(damage == navec.getDamage()) {
            System.out.println("Navec inflicts " + damage +
                    " damage on Fae. Fae's current health: " + fae.getHealth() + "/" + PLAYERMAXHEALTH);
        }
        // From demon
        if(damage == demon[0].getDamage()) {
            System.out.println("Demon inflicts " + damage +
                    " damage on Fae. Fae's current health: " + fae.getHealth() + "/" + PLAYERMAXHEALTH);
        }
    }

    /**
     * Print the status update of player's damage to enemy to standard output
     * @param enemy input of any child class of enemy
     * @param damage amount of damage dealt by player
     * @param health amount of health left of the enemy
     */
    public void faeDamageOutput(String enemy, int damage, int health){
        // To navec
        if (enemy.equals("Navec")){
            System.out.println("Fae inflicts " + damage +
                    " damage on Navec. Navec's current health: " + health + "/" + NAVECMAXHEALTH);
        }

        // To demon
        if (enemy.equals("Demon")){
            System.out.println("Fae inflicts " + damage +
                    " damage on Demon . Demons's current health: " + health + "/" + DEMONMAXHEALTH);
        }
    }

    /**
     * Method for altering the time scale in lv1 for the enemies
     * @param input input the input from keyboard
     */
    private void changeSpeed(Input input) {
        // Slow down
        if (input.wasPressed(Keys.K) && scale > MIN_SCALE) {
            scale--;
            System.out.println("Slowed down, Speed: " + scale);
            for (Demon current : demon) {
                current.scaleDown();
            }
            navec.scaleDown();
        }
        // Speed up
        if (input.wasPressed(Keys.L) && scale < MAX_SCALE) {
            scale++;
            System.out.println("Sped up, Speed: " + scale);
            for (Demon current : demon) {
                current.scaleUp();
            }
            navec.scaleUp();
        }
    }
}
