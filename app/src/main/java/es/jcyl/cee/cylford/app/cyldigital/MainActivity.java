package es.jcyl.cee.cylford.app.cyldigital;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

// For a minimum API level of 15, you'd want to use AppCompatActivity(ActionBarActivity is deprecated).
// So for example, your MainActivity would look like this:
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide title bar. Si lo ponía detrás del onCreate. CASQUE
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        Globals.activityCount++;
        super.onStart();


    }
    @Override
    public void onStop(){
        super.onStop();
        Globals.activityCount--;
        if( Globals.activityCount == 0){ //exit or background
            //ECyLPrivateHandler.logout(this,false);
        }
    }

}
