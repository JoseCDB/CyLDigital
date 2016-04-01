package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Locale;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;

/**
 * Created by josecarlos.delbarrio on 15/03/2016.
 */
public class DetalleActividadActivity extends MainActivity implements OnClickListener {

    SimpleDateFormat sdf = new SimpleDateFormat("'Comienza el' d 'de' MMMM 'del' yyyy", Locale.getDefault());
    View back;
    CyLDFormacion it;

    WebView web;
    Button info;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);


    }


    @Override
    public void onClick(View v) {
        if( v == back){
            this.onBackPressed();
        } else if( v == info){
            Intent i = new Intent(this, WebContentActivity.class);
            i.putExtra("url", it.url);
            startActivity(i);
        }
    }// onClick
}
