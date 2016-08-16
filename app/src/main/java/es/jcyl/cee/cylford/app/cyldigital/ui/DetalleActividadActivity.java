package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;

/**
 * Created by josecarlos.delbarrio on 15/03/2016.
 */
public class DetalleActividadActivity extends MainActivity implements OnClickListener {

    SimpleDateFormat sdf = new SimpleDateFormat("'Comienza el' d 'de' MMMM 'del' yyyy", Locale.getDefault());
    View back;
    View btnMenuRight;
    CyLDFormacion it;
    Button info;
    TextView centFor;
    SimpleSideDrawer slide_me;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        it = (CyLDFormacion) intent.getSerializableExtra("SELECTED_ITEM");

        //Titulo del menú superior
        TextView tv = (TextView) this.findViewById(R.id.title);//cambiar lo de title
        tv.setText("");
        tv = (TextView) findViewById(R.id.title);
        tv.setText(it.nombre);

        //Botón actividad atrás
        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);

        //Botón de menú lateral
        btnMenuRight = this.findViewById(R.id.btnrightmenu);
        btnMenuRight.setOnClickListener(this);

        //Objeto que realiza la acción de mostrar el menú lateral
        slide_me = new SimpleSideDrawer(this);
        slide_me.setRightBehindContentView(R.layout.menu_right);

        //Descripción de la actividad
        tv = (TextView) findViewById(R.id.description);
        String descActi = "";
        if(it.descripcion != null && !it.descripcion.equals("")){
            descActi = it.descripcion;
            descActi = descActi.substring(9, descActi.length()-3); //Se eliminan las etiquetas CDATA
        }

        tv.setText(Html.fromHtml(descActi));

        //Centro donde se imparte
        centFor = (TextView) findViewById(R.id.centro);
        String centroFormativo = "";
        if ( it.centro == null) {
            centroFormativo = "";
        } else {
            centroFormativo = it.centro;
        }

        //Texto Fecha de Inicio actividad
        tv = (TextView) findViewById(R.id.fecha);
        if( it.fechaInicio != null){
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(it.fechaInicio);
                tv.setText(sdf.format(date));
            }catch (ParseException pe) {
                System.err.println("Problemas parseando fecha de inicio actividad formativa");
            }
        }else{
            tv.setText("");
        }

        //Texto Duración de la actividad. si es presencial
        tv = (TextView) findViewById(R.id.duration);
        tv.setText(Float.toString(it.numeroHoras)+ " horas");

        //Botón Ir url de actividad
        info = (Button) this.findViewById(R.id.link);

        //Mostrar o no mostrar si Online o Persencial
        if(it.tipoFormación.equals(Constants.TIPO_ONLINE)) {
            info.setOnClickListener(this);
        } else  if(it.tipoFormación.equals(Constants.TIPO_PRESENCIAL)) {
            info.setVisibility(View.GONE);
            centFor.setText(centroFormativo);
        }
    }


    @Override
    public void onClick(View v) {
        if(v == back) {
            this.onBackPressed();
        }else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        } else if(v == info){
            Intent i = new Intent(this, WebContentActivity.class);
            i.putExtra("url", it.url);
            startActivity(i);
            finish();
        }
    }// onClick
}
