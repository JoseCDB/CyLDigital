package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.navdrawer.SimpleSideDrawer;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;

public class PresentacionActivity extends MainActivity {

    SimpleSideDrawer slide_me;
    Button left_button, right_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        slide_me = new SimpleSideDrawer(this);
        slide_me.setLeftBehindContentView(R.layout.left_menu);
        slide_me.setRightBehindContentView(R.layout.right_menu);

        left_button = (Button) findViewById(R.id.left_buton);
        right_button = (Button) findViewById(R.id.right_buton);
        left_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                slide_me.toggleLeftDrawer();
            }
        });
        right_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                slide_me.toggleRightDrawer();
            }
        });
    }


}
