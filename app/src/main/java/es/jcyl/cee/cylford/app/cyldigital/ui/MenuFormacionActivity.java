package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import es.jcyl.cee.cylford.app.cyldigital.R;
import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.MainActivity;

//Tenemos que implementar OnClick de OnClickListener
public class MenuFormacionActivity extends MainActivity
        implements OnClickListener {

    View presencial;
    View online;
    View iconoPresencial;
    View iconoOnline;
    View textPresencial;
    View textOnline;
    TextView build;
    View back;
    View btnMenuRight;
    SimpleSideDrawer slide_me;
    private View itemPresentacion;
    private View itemFormativas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuform);
        getSupportActionBar().hide();

        presencial = this.findViewById(R.id.presencial);
        presencial.setOnClickListener(this);

        online = this.findViewById(R.id.online);
        online.setOnClickListener(this);

        iconoPresencial = this.findViewById(R.id.icono_presenciales);
        iconoPresencial.setOnClickListener(this);

        iconoOnline = this.findViewById(R.id.icono_online);
        iconoOnline.setOnClickListener(this);

        textPresencial = this.findViewById(R.id.textView_presencial);
        textPresencial.setOnClickListener(this);

        textOnline = this.findViewById(R.id.textView_online);
        textOnline.setOnClickListener(this);

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

        //Para ver en pantalla el build (versión) de la app
        build = (TextView) this.findViewById(R.id.build);
        try {
            int vcode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            build.setText(String.format(getString(R.string.build), vcode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        build.setVisibility(View.GONE);//No lo muestro.
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, ActividadesFormativasActivity.class);
        if (v == back) {
            i = new Intent(this, PresentacionActivity.class);
            startActivity(i);
            finish();
            //this.onBackPressed();
        } else if (v == presencial ||
                v == iconoPresencial ||
                v == textPresencial) {
            i.putExtra("origen", Constants.TIPO_PRESENCIAL);
            startActivity(i);
            finish();
        } else if (v == online ||
                v == iconoOnline ||
                v == textOnline) {
            i.putExtra("origen", Constants.TIPO_ONLINE);
            startActivity(i);
            finish();
        } else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        }else if (v == itemPresentacion) {
            slide_me.closeRightSide();
            i = new Intent(this, PresentacionActivity.class);
            startActivity(i);
            finish();
        } else if (v == itemFormativas) {
            //i = new Intent(this, MenuFormacionActivity.class);
            //slide_me.closeRightSide();
            //startActivity(i);
        }
    }
}
