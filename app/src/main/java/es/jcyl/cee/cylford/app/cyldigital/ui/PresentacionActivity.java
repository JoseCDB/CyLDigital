package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.navdrawer.SimpleSideDrawer;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;

public class PresentacionActivity extends MainActivity implements View.OnClickListener {

    private SimpleSideDrawer slide_me;
    private View btnMenuRight;
    private View back;
    private View itemPresentacion;
    private View itemFormativas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);
        //getSupportActionBar().hide();

        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);

        btnMenuRight = this.findViewById(R.id.btnrightmenu);
        btnMenuRight.setOnClickListener(this);

        slide_me = new SimpleSideDrawer(this);
        slide_me.setRightBehindContentView(R.layout.menu_right);

        itemPresentacion = this.findViewById(R.id.presentacion);
        itemPresentacion.setOnClickListener(this);

        itemFormativas = this.findViewById(R.id.formativas);
        itemFormativas.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == back) {
           // this.onBackPressed();
            //finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        } else if (v == itemPresentacion) {
            //Intent i = new Intent(this, PresentacionActivity.class);
            //slide_me.closeRightSide();
            //startActivity(i);
        } else if (v == itemFormativas) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, MenuFormacionActivity.class);
            startActivity(i);
            finish();
        }
    }

}
