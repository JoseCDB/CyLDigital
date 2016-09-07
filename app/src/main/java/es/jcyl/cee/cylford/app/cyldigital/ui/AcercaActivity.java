package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;

public class AcercaActivity extends MainActivity implements View.OnClickListener{

    private SimpleSideDrawer slide_me;

    private ImageView back, btnMenuRight;

    private View itemPresentacion, itemFormativas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca);

        TextView tv = (TextView) this.findViewById(R.id.title);
        tv.setText("Acerca de esta App");

        btnMenuRight = (ImageView) this.findViewById(R.id.btnrightmenu);
        btnMenuRight.setOnClickListener(this);

        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);

        slide_me = new SimpleSideDrawer(this);
        slide_me.setRightBehindContentView(R.layout.menu_right);

        itemPresentacion = this.findViewById(R.id.presentacion);
        itemPresentacion.setOnClickListener(this);

        itemFormativas = this.findViewById(R.id.formativas);
        itemFormativas.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == back) {
            this.onBackPressed();
        }else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        } else if (v == itemPresentacion) {
            Intent i = new Intent(this, PresentacionActivity.class);
            slide_me.closeRightSide();
            startActivity(i);
            finish();
        } else if (v == itemFormativas) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, MenuFormacionActivity.class);
            startActivity(i);
            finish();
        }
    }

}
