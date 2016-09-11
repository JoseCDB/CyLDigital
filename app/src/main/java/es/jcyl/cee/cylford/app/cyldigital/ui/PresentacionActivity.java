package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;

public class PresentacionActivity extends MainActivity implements View.OnClickListener {

    private SimpleSideDrawer slide_me;

    private ImageView back, btnMenuRight;

    TextView build;

    private View itemPresentacion, itemFormativas, itemAcerca;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);

        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);

        btnMenuRight = (ImageView) this.findViewById(R.id.btnrightmenu);
        btnMenuRight.setOnClickListener(this);

        slide_me = new SimpleSideDrawer(this);
        slide_me.setRightBehindContentView(R.layout.menu_right);

        itemPresentacion = this.findViewById(R.id.presentacion);
        itemPresentacion.setOnClickListener(this);

        itemFormativas = this.findViewById(R.id.formativas);
        itemFormativas.setOnClickListener(this);

        itemAcerca = this.findViewById(R.id.acerca);
        itemAcerca.setOnClickListener(this);

        build = (TextView) this.findViewById(R.id.build);
        try {
            int vcode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            build.setText(String.format(getString(R.string.build), vcode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //build.setVisibility(View.GONE);
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
        } else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        } else if (v == itemFormativas) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, MenuFormacionActivity.class);
            startActivity(i);
            finish();
        } else if (v == itemAcerca) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, AcercaActivity.class);
            startActivity(i);
            //finish();
        }
    }

}
