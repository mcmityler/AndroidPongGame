package me.pongfinal;

import android.graphics.RectF;

public class Bat {
    //rectangle of bat
    private RectF _bat;

    //width and height of bat
    private float batLength;
    private float batHeight;

    //left of bat
    private float batXCoord;

    //top of bat
    private float batYCoord;

    //Player bat speed
    private float batSpeed;

    //Constants
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    //moving direction of bat
    private int batMoving = STOPPED;

    //Screen size
    private int screenX;
    private int screenY;

    //Constructor
    public Bat(int x, int y){

        screenX = x;
        screenY = y;

        //length of bat
        batLength = screenX / 8;

        //height of bat
        batHeight = screenY / 50;

        //starting position of bat
        batXCoord = screenX / 2;
        batYCoord = screenY - 375;
        //create new bat object
        _bat = new RectF(batXCoord, batYCoord, batXCoord + batLength, batYCoord + batHeight);

        //Speed of player bat
        batSpeed = screenX;
    }
    //return rectangle of bat
    public RectF getRect(){
        return _bat;
    }
    //set state
    public void setMovementState(int state){
        batMoving = state;
    }
    //Update state of bat and position
    public void update(long fps){

        if(batMoving == LEFT){
            batXCoord = batXCoord - batSpeed / fps;
        }

        if(batMoving == RIGHT){
            batXCoord = batXCoord + batSpeed / fps;
        }

        //keep bat on Screen
        if(_bat.left < 0){ batXCoord = 0; } if(_bat.right > screenX){
            batXCoord = screenX -
                    // The width of the Bat
                    (_bat.right - _bat.left);
        }

        //Move Bat
        _bat.left = batXCoord;
        _bat.right = batXCoord + batLength;
    }
}
