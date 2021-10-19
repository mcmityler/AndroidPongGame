package me.pongfinal;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

class PongScreen extends SurfaceView implements Runnable {

    //tells game to pause/play when started/exited
    Thread gameThread = null;
    //allows us to draw
    SurfaceHolder surface;

    // holds if the game running
    // It is volatile because it is accessed from inside and outside the thread
    volatile boolean playing;

    //start game pasued
    boolean paused = true;

    // canvas of game
    Canvas canvas;
    //paint for objects
    Paint paint;

    //framerate of game
    long frames;

    //size of the screen in pixels
    int screenX;
    int screenY;

    // The Players bat
    Bat bat;

    // Ball
    Ball ball;

    //Enemy
    Enemy enemy;

    // Scores
    int playerScore = 0;
    int playerLives = 3;
    int highScore = 0;

    //Inititializer
    public PongScreen(Context context, int x, int y) {
        super(context);

        // Set the screen width and height
        screenX = x;
        screenY = y;

        // Init surface for drawing on
        surface = getHolder();
        //Init paint for drawing shapes color
        paint = new Paint();

        // spawn playerBat
        bat = new Bat(screenX, screenY);

        //Spawn Ball
        ball = new Ball(screenX, screenY);

        //Spawn Enemy
        enemy = new Enemy(screenX, screenY);

        setupAndRestart();

    }
    //Reset if ball goes off screen
    public void setupAndRestart(){

        // Put the ball at orgin
        ball.reset(screenX, screenY);
        if(playerLives == 0){
            if(playerScore > highScore){
                highScore = playerScore;
            }
            playerScore = 0;
            playerLives = 3;
        }

    }
    //Game Loop
    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            //Update if not paused
            if (!paused) {
                update();
            }

            //Draw frame
            draw();
            //FPS calculation
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                frames = 1000 / timeThisFrame;
            }

        }

    }
    // Everything that needs to be updated goes in here
    // Movement, collision detection etc.
    public void update() {

        //Move Bat
        bat.update(frames);
        //Move Ball
        ball.update(frames);
        //Check Enemy State
        if(ball.getRect().left < enemy.getRect().left){
            enemy.setState(1);
        }else if (ball.getRect().right > enemy.getRect().right){
            enemy.setState(2);
        }else{
            enemy.setState(0);
        }
        //Move Enemy
        enemy.update(frames);

        //HighScore
        if(playerScore > highScore){
            highScore = playerScore;
        }

        //Player Collision
        if(RectF.intersects(bat.getRect(), ball.getRect())) {
            ball.setRandomXVelocity();
            ball.reverseYVelocity();
            //sends ball to top of paddle to ensure it doesnt keep increasting speed
            ball.clearObstacleTop(bat.getRect().top - 2);
            //Increase speed of Ball
            ball.increaseVelocity();
        }
        //Enemy Collision
        if(RectF.intersects(enemy.getRect(), ball.getRect())) {
            ball.setRandomXVelocity();
            ball.reverseYVelocity();
            //sends ball to bottom of paddle to ensure it doesnt keep increasting speed
            ball.clearObstacleBot(enemy.getRect().bottom);

            //Increase speed of ball
            ball.increaseVelocity();
        }
        //Ball exits bottom of screen
        if(ball.getRect().bottom > screenY){
            ball.reverseYVelocity();
            ball.clearObstacleTop(screenY - 2);

            //aiScore addition
            playerLives--;
            setupAndRestart();
        }
        //Ball Exits top of screen
        if(ball.getRect().top < 0){
            ball.reverseYVelocity();
            ball.clearObstacleTop(12);
            //playerScore addition
            playerScore++;
            setupAndRestart();
        }
        //Ball Left Wall Collision
        if(ball.getRect().left < 0){
            ball.reverseXVelocity();
            ball.clearObstacleX(2);


        }
        // Ball Right Wall Collision
        if(ball.getRect().right > screenX){
            ball.reverseXVelocity();
            ball.clearObstacleX(screenX - 22);
        }
    }
    //Drawing Method
    public void draw() {

        // Make sure surface is valid
        if (surface.getSurface().isValid()) {
            //Set canvas to current
            canvas = surface.lockCanvas();

            //Screen Color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            //Color to white
            paint.setColor(Color.argb(255, 255, 255, 255));

            //Draw the bat
            canvas.drawRect(bat.getRect(), paint);
            // Draw the ball
            canvas.drawRect(ball.getRect(), paint);
            //Draw the Enemy
            canvas.drawRect(enemy.getRect(),paint);

            //Color to white
            paint.setColor(Color.argb(255, 255, 255, 255));

            //Draw Score
            paint.setTextSize(40);
            canvas.drawText("Score: " + playerScore + "     Player Lives: " + playerLives, 10, 50, paint);
            canvas.drawText("High Score: " + highScore, 10, 90, paint);
            //draw everything to canvas
            surface.unlockCanvasAndPost(canvas);
        }

    }
    //Pause game on exiting application
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    //Resume game on re-entering
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
    //Touch events
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                paused = false;

                //Move bat left or right depending on side of screen touched
                if(motionEvent.getX() > screenX / 2){
                  bat.setMovementState(bat.RIGHT);
                }
                else{
                   bat.setMovementState(bat.LEFT);
                }

                break;

            //Stop bat when player lifts finger
            case MotionEvent.ACTION_UP:

               bat.setMovementState(bat.STOPPED);
               break;
        }
        return true;
    }
}
