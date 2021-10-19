package me.pongfinal;

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    private RectF _ball;
    private float bXVelocity;
    private float bYVelocity;
    private float ballWidth;
    private float ballHeight;
    private float bdirection = 1;

    public Ball(int screenX, int screenY) {

        //Size of Ball relative to screen
        ballWidth = screenX / 70;
        ballHeight = ballWidth;

        //Starting velocity of ball
        bYVelocity = screenY / 6;
        bXVelocity = bYVelocity;

        //init ball rectangle
        _ball = new RectF();

    }
    // Give access of ball to the Rect
    public RectF getRect(){
        return _ball;
    }
    //Update balls position
    public void update(long fps){
        _ball.left = _ball.left + (bXVelocity / fps);
        _ball.top = _ball.top + (bYVelocity / fps);
        _ball.right = _ball.left + ballWidth;
        _ball.bottom = _ball.top - ballHeight;
    }
    // reflect y vel
    public void reverseYVelocity(){
        bYVelocity = -bYVelocity;
    }

    // reflect on x vel
    public void reverseXVelocity(){
        bXVelocity = -bXVelocity;
    }

    public void setRandomXVelocity(){

        // Generate a random number either 0 or 1
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }
    }
    //speed up ball when hits paddle
    public void increaseVelocity(){
        bXVelocity = bXVelocity + bXVelocity / 10;
        bYVelocity = bYVelocity + bYVelocity / 10;
    }
    public void clearObstacleTop(float y){
        _ball.bottom = y;
        _ball.top = y - ballHeight;
    }
    public void clearObstacleBot(float y){
        _ball.top = y;
        _ball.bottom = y + ballHeight;
    }

    public void clearObstacleX(float x){
        _ball.left = x;
        _ball.right = x + ballWidth;
    }
    //Checks balls direction, resets speed, and changes directions when ball goes off screen
    public void reset(int x, int y){
        if(bdirection == 1){
            _ball.left = x / 2;
            _ball.top = y/2;
            _ball.right = x / 2 + ballWidth;
            _ball.bottom = y/2 - ballHeight;
            bYVelocity = x / 6;
            bXVelocity = bYVelocity;
            bdirection = -bdirection;
        }
        else if (bdirection == -1){
            bYVelocity = y / 6;
            bXVelocity = bYVelocity;
            bYVelocity = -bYVelocity;
            _ball.left = x / 2;
            _ball.top = y/2;
            _ball.right = x / 2 + ballWidth;
            _ball.bottom = y/2 - ballHeight;

            bdirection = -bdirection;
        }
    }
}
