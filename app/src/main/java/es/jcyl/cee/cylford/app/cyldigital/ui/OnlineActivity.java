package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.R;
import es.jcyl.cee.cylford.app.cyldigital.backend.handler.CyLDFormacionHandler;
import es.jcyl.cee.cylford.app.cyldigital.backend.handler.CyLDFormacionHandlerListener;
import es.jcyl.cee.cylford.app.cyldigital.model.Family;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;


/*
* La Interfaz OnItemSelectedListener me obliga a implementar métodos: onItemSelected, onNothingSelected
* La Interfaz OnClickListener me obliga a implementar métodos: onClick, OnClickListener
* La Interfaz CyLDFormacionHandlerListener me obliga a implementar los métodos: onResults
* */

public class OnlineActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener, CyLDFormacionHandlerListener<CyLDFormacion> {

    //Combos localidad y tipo de actividad
    Spinner localidad;
    Spinner tipo;

    //Valores seleccionados en los combos
    String provinciaVal = "";
    String tipoVal = "";

    //Campo buscar
    EditText search;

    //Botón volver
    View back;

    //Campo que muestra el número de resutlados de la búsqueda.
    TextView count;

    //Contiene  "fecha + nombre"  de la llamada actual al Servicio Web.
    String currentCallId = "";

    //ArrayList con los datos de actividades consultados
    ArrayList<CyLDFormacion> nuevosDatos = new ArrayList<CyLDFormacion>();

    //Fecha en long de la llamada actual al Servicio Web
    long currentCallTime = 0;

    //PullToRefreshListView list;

    Button botonPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        //1 SPINNER LOCALIDAD
        localidad = (Spinner) this.findViewById(R.id.localidad);
        //Para que cuando se selecciona un valor en el Spinner, el contexto recoja el escuchador
        localidad.setOnItemSelectedListener(this);//Para esto tengo que implementar interfaz OnItemSelectedListener
        //Creamos el adaptador
        FamilyAdapter adapterL = new FamilyAdapter(this, android.R.layout.simple_spinner_item, Constants.provincias);
        //Añadimos el layout para el menú
        adapterL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        localidad.setAdapter(adapterL);


        //2 SPINNER TIPO ACTIVIDAD
        tipo = (Spinner) this.findViewById(R.id.tipo);
        tipo.setOnItemSelectedListener(this);
        //Creamos el adaptador
        FamilyAdapter adapterT = new FamilyAdapter(this, android.R.layout.simple_spinner_item, Constants.tiposActividad);
        adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adapterT);

        //3 CAMPO DE TEXTO BUSCAR
        search = (EditText) this.findViewById(R.id.search);

        //4 BOTÓN VOLVER
        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);

        //5 BOTÓN PRUEBA
        botonPrueba = (Button) this.findViewById(R.id.btnPrueba);
        //botonPrueba.setTextColor(getResources().getColor(R.color.background));
        //botonPrueba.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.board_icon_selected_selector, 0, 0);
        botonPrueba.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View arg1, int index, long arg3) {
        if (adapter == localidad) {
            if (index == 0) {
                provinciaVal = "";
            } else {
                provinciaVal = ((Family) localidad.getSelectedItem()).getCode();
            }
        } else if (adapter == tipo) {
            if (index == 0) {
                tipoVal = "";
            } else {
                tipoVal = ((Family) tipo.getSelectedItem()).getCode();
            }
        }
    }

    /*
    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        //search-> valor del campo de texto de búsqueda
        //provinciaVal -> valor del spinner de provincias
        //tipoVal -> valor del spinner de tipo de actividad
        requestData(true, search.getText().toString(), provinciaVal, tipoVal);
    }
    */

    //@Override
    protected void onScrollNext() {
        requestData(false, search.getText().toString(), provinciaVal,
                tipoVal);
    }

    private void requestData(boolean clear, String search, String prov, String tipo) {

        //adapter.lock(); Objeto de tipo BoardAdapter

        // if (!clear && data != null
        // && (data.size() == 0 || data.get(data.size() - 1) != null)) {
        // data.add(null);
        // adapter.notifyDataSetChanged();
        // }
        if (currentCallId != null) {
            CyLDFormacionHandler.cancelCall(currentCallId);
        }
        //Se limpia el ArrayList de nuevos datos de Actividades
        nuevosDatos.clear();
        currentCallId = "education" + new Date().getTime();
        currentCallTime = new Date().getTime();
        //Si no hay valor en ninguno de los campos
        if (search.length() == 0 && prov.length() == 0 && tipo.length() == 0) {
            CyLDFormacionHandler.listActivities(Constants.TIPO_ONLINE, currentCallId, this);// Parámetro this válido porque se implementa CyLDFormacionHandlerListener
        } else {
            CyLDFormacionHandler.listActivities(Constants.TIPO_ONLINE, 1, null, null, search, tipoVal, provinciaVal, currentCallId, this);
        }
    }// requestData



    @Override
    public void onNothingSelected(AdapterView<?> adapter) {
        if (adapter == localidad) {
            provinciaVal = "";
        } else if (adapter == tipo) {
            tipoVal = "";
        }
        //list.setRefreshing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_online, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v == back) {//Si el evento es sobre el botón volver
            this.onBackPressed();
        }else if (v == botonPrueba) {
            requestData(false, search.getText().toString(), provinciaVal,
                    tipoVal);
        }
    }

    @Override
    public void onResults(String callId, Collection<CyLDFormacion> res, boolean expectRefreshCall){

    }

    @Override
    public void onError(String callId, Exception e, boolean expectRefresh) {

    }

    /**
     * Clase que extiende de ArrayAdapter a la cual se le pasa en el constructor
     * Los objetos en un Array de tipo Family
     * El contexto (this)
     * La vista, un TextView para contener el valor del objeto de tipo Family
     * */
    private class FamilyAdapter extends ArrayAdapter<Family> {

        LayoutInflater inflater;

        /**
        *  Constructor con el que se crea un objeto LayoutInflater.
        */
        public FamilyAdapter(Context context, int textViewResourceId,  Family[] objects) {
            super(context, textViewResourceId, objects);
            inflater = LayoutInflater.from(context);
        }

        @Override
        /*Devuelve/rellena en pantalla los elementos del combo*/
        public View getDropDownView(final int position, View convertView,
                                    ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);//con false no da error
            }

            TextView item = (TextView) convertView;
            item.setText(getItem(position).getName());
            final TextView finalItem = item;
            item.post(new Runnable() {
                @Override
                public void run() {
                    finalItem.setSingleLine(false);
                }
            });
            return item;
        }

    }// FamiliAdapter
}
