package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import es.jcyl.cee.cylford.app.cyldigital.R;
import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.MainActivity;

//Tenemos que implementar OnClick de OnClickListener
public class MenuFormacionActivity extends MainActivity implements OnClickListener {

    private LinearLayout layoutPresenciales;
    private ImageView iconoPresencial;
    private TextView presencial;
    private TextView textPresencial;

    private LinearLayout layoutOnlines;
    private ImageView iconoOnline;
    private TextView online;
    private TextView textOnline;

    private TextView build;

    private ImageView back;
    private ImageView btnMenuRight;

    private SimpleSideDrawer slide_me;

    private View itemPresentacion, itemFormativas, itemAcerca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuform);

        layoutPresenciales = (LinearLayout)this.findViewById(R.id.layoutPresenciales);
        layoutPresenciales.setOnClickListener(this);

        layoutOnlines = (LinearLayout)this.findViewById(R.id.layoutOnlines);
        layoutOnlines.setOnClickListener(this);

        presencial = (TextView)  this.findViewById(R.id.presencial);
        presencial.setOnClickListener(this);

        online = (TextView)  this.findViewById(R.id.online);
        online.setOnClickListener(this);

        iconoPresencial = (ImageView) this.findViewById(R.id.icono_presenciales);
        iconoPresencial.setOnClickListener(this);

        iconoOnline = (ImageView) this.findViewById(R.id.icono_online);
        iconoOnline.setOnClickListener(this);

        textPresencial = (TextView) this.findViewById(R.id.textView_presencial);
        textPresencial.setOnClickListener(this);

        textOnline = (TextView) this.findViewById(R.id.textView_online);
        textOnline.setOnClickListener(this);

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
        if (v == back) {
            Intent i = new Intent(this, PresentacionActivity.class);
            startActivity(i);
            finish();
        } else if (v == layoutPresenciales ||
                v == textPresencial ||
                v == presencial ||
                v == iconoPresencial) {
            Intent i = new Intent(this, ActividadesFormativasActivity.class);
            i.putExtra("origen", Constants.TIPO_PRESENCIAL);
            startActivity(i);
            finish();
        } else if (v == layoutOnlines ||
                v == textOnline ||
                v == online ||
                v == iconoPresencial) {
            Intent i = new Intent(this, ActividadesFormativasActivity.class);
            i.putExtra("origen", Constants.TIPO_ONLINE);
            startActivity(i);
            finish();
        } else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        } else if (v == itemPresentacion) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, PresentacionActivity.class);
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
