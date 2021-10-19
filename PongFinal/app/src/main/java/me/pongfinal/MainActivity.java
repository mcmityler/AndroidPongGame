package me.pongfinal;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    //Logic of Game
    PongScreen pongS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        // Initialize pongView and set it as the view
        pongS = new PongScreen(this, size.x, size.y);
        setContentView(pongS);

    }
    //Player EntersApp
    @Override
    protected void onResume() {
        super.onResume();

        //Start game when opened
        pongS.resume();
    }

    //Player Leaving App event
    @Override
    protected void onPause() {
        super.onPause();

        //tell pongscreen to pause
        pongS.pause();
    }
}
