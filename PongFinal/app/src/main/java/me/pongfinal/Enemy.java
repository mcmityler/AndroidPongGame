package me.pongfinal;

import android.graphics.RectF;

public class Enemy {//enemy's sprite
    private RectF rect;
    //dimensions
    private float length;
    private float height;
    //position (top left)
    private float xCord;
    private float yCord;
    //movement speed, adjust this (in constructor) to make the enemy more difficult(faster)
    private float speed;
    //States that control enemy movement. self explanitory.
    public final int STOP = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    //Enemy starts out not moving
    private int moving = STOP;
    //Placeholder for screen positions
    private int screenX;
    private int screenY;
    //CONSTRUCTOR
    //===================================================================================
    public Enemy(int x, int y){
        screenX = x;
        screenY = y;

        length = screenX/8;
        height = screenY/50;

        xCord = screenX/2;
        yCord = 20;

        rect = new RectF(xCord,yCord, xCord+length, yCord+height);
        speed = screenX/3;
    }
    //===================================================================================
    //WAY TO GET POSITION
    //the main function uses this to check for the enemy's position, enemy.getRect().left
    //will give you the enemy's x coordinate, .top will give you y, use this for collision detection
    public RectF getRect() { return rect; }
    //WAY TO MOVE ENEMY
    //quite simply set the state to LEFT(1) to move the enemy left, set it to RIGHT(2) to move them
    //right, set it to STOP(0) to stop the enemy moving, that last one is what you'll be using most
    //for pausing and stuff.
    public void setState(int state) { moving = state; }
    //UPDATE
    //===================================================================================
    public void update(long fps){
        if (moving == LEFT){ //impliments leftward movement
            xCord = xCord - speed/fps;
        }
        if (moving == RIGHT){ //impliments rightward movement
            xCord = xCord + speed/fps;
        }
        if(rect.left < 0){ xCord = 0; } //this stuff stops the enemy from leaving the screen
        if(rect.right > screenX){
            xCord = screenX -
                    // The width of the Bat
                    (rect.right - rect.left);
        }
        //updates the sprite for drawing
        rect.left = xCord;
        rect.right = xCord+length;
    }
    //===================================================================================
}
