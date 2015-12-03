package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;

//Tenemos que implementar OnClick de OnClickListener
public class MenuActivity extends MainActivity implements OnClickListener {

    //Nuevas
    View presencial;
    View online;

    TextView build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        presencial = this.findViewById(R.id.presencial);
        presencial.setOnClickListener(this);

        online = this.findViewById(R.id.online);
        online.setOnClickListener(this);

        //Para ver en pantalla el build (versión) de la app
        build = (TextView) this.findViewById(R.id.build);
        try {
            int vcode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            build.setText(String.format(getString(R.string.build), vcode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        build.setVisibility(View.GONE);//No lo muestro.
        //

    }
    @Override
    public void onClick(View v) {
        if (v == presencial) {
            //Si el evento onclick es en la formación presencial
            startActivity(new Intent(this, PresencialActivity.class));
        } else if (v == online) {
            //Si el onclick es en la formación online
            startActivity(new Intent(this, OnlineActivity.class));
        }
    }

}
