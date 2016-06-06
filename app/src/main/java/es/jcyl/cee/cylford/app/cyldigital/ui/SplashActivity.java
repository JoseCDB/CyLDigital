package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;

public class SplashActivity extends MainActivity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash);
        //Se oculta el actionBar
        //getActionBar().hide();
        getSupportActionBar().hide();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity. En nuestro caso arrancamos desde aquí la MenuFormacionActivity
                Intent mainIntent = new Intent().setClass(SplashActivity.this, MenuFormacionActivity.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

        // Hide title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
    }



    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
