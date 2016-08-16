package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.R;
import es.jcyl.cee.cylford.app.cyldigital.backend.handler.CyLDFormacionHandler;
import es.jcyl.cee.cylford.app.cyldigital.backend.handler.CyLDFormacionHandlerListener;
import es.jcyl.cee.cylford.app.cyldigital.model.Family;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.navdrawer.SimpleSideDrawer;

/*
* La Interfaz OnItemSelectedListener me obliga a implementar métodos: onItemSelected, onNothingSelected
* La Interfaz OnClickListener me obliga a implementar métodos: onClick, OnClickListener
* La Interfaz CyLDFormacionHandlerListener me obliga a implementar los métodos: onResults
* */
public class ActividadesFormativasActivity extends MainActivity
        implements
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        CyLDFormacionHandlerListener<CyLDFormacion>,
        OnRefreshListener<ListView> {

    //Retraso para mostrar los resultados en onResults()
    private static final int LOADING_DELAY = 3000;

    BoardAdapter adapter;
    ArrayList<CyLDFormacion> data = new ArrayList<CyLDFormacion>();
    ArrayList<CyLDFormacion> datosFuturos = new ArrayList<CyLDFormacion>();
    //Da formato a la fecha de las actividades mostradas en BoardAdapter.
    SimpleDateFormat sdf = new SimpleDateFormat("'Comienza el' d 'de' MMMM 'de' yyyy", Locale.getDefault());
    //Si el origen es una consulta tipo Online o Presencial
    String origen;
    //Combos localidad y tipo de actividad
    Spinner localidad, tipo;
    //Texto de valores seleccionados en los combos
    String centroVal, tipoVal, fechaInicioAct, fechaFinAct = "";
    //Campo buscar
    EditText search;
    //Campo que muestra el número de resutlados de la búsqueda.
    TextView count;
    //Contiene  "fecha + nombre"  de la llamada actual al Servicio Web.
    String currentCallId = "";
    //Fecha en long de la llamada actual al Servicio Web
    long currentCallTime = 0;
    //Nueva lista Pull to refresh para mostrar los resultados devueltos.
    PullToRefreshListView list;;
    //Botón de prueba que no se muestra en la versión final.
    Button botonPrueba;
    //Botón volver
    View back;
    //Botón menú lateral
    View btnMenuRight;
    SimpleSideDrawer slide_me;
    //Layout para los spinners de selección de centro y tipo de actividad
    //Layout para los botones de fecha de inicio y fecha de fin
    View layoutSpinners, layoutCalendars;

    //Fechas
    private Calendar cal;
    private int day, month, year;
    //F. Inicio
    private ImageButton imageButtonFechaInicio;
    private TextView editTextFechaInicio;
    //F. Fin
    private ImageButton imageButtonFechaFin;
    private TextView editTextFechaFin;

    private View itemPresentacion;
    private View itemFormativas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actformativas);
        getSupportActionBar().hide();

        origen = getIntent().getStringExtra("origen");

        //1 SPINNER LOCALIDAD
        localidad = (Spinner) this.findViewById(R.id.localidad);
        //Para que cuando se selecciona un valor en el Spinner, el contexto recoja el escuchador
        localidad.setOnItemSelectedListener(this);//Para esto tengo que implementar interfaz OnItemSelectedListener
        //Se crea el adaptador
        FamilyAdapter adapterL = new FamilyAdapter(this, android.R.layout.simple_spinner_item, Constants.provincias);
        //Se añade el layout para el menú
        adapterL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Se indica al spinner el adaptador a usar
        localidad.setAdapter(adapterL);

        //2 SPINNER TIPO ACTIVIDAD
        tipo = (Spinner) this.findViewById(R.id.tipo);
        tipo.setOnItemSelectedListener(this);
        FamilyAdapter adapterT = new FamilyAdapter(this, android.R.layout.simple_spinner_item, Constants.tiposActividad);
        adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adapterT);

        //3 CAMPO DE TEXTO BUSCAR
        search = (EditText) this.findViewById(R.id.search);
        //La acción de cuando se pulsa la lupa del teclado
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // String s = search.getText().toString();
                    // requestData(false, s, provinceVal, familyVal);
                    list.setRefreshing();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //4 BOTÓN VOLVER
        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);

        //5 BOTÓN PRUEBA
        botonPrueba = (Button) this.findViewById(R.id.btnPrueba);
        //botonPrueba.setTextColor(getResources().getColor(R.color.background));
        //botonPrueba.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.board_icon_selected_selector, 0, 0);
        botonPrueba.setOnClickListener(this);

        //6 CONTADOR Nº ACTIVIDADES
        count = (TextView) this.findViewById(R.id.count);
        count.setVisibility(View.GONE);

        //7 LISTA ITEMS ACTIVIDADES
        list = (PullToRefreshListView) findViewById(R.id.list);
        list.setOnRefreshListener(this);
        adapter = new BoardAdapter();
        list.setAdapter(adapter);
        list.setOnScrollListener(adapter);
        list.setOnItemClickListener(adapter);
        list.setMode(Mode.PULL_FROM_START);
        list.setScrollingWhileRefreshingEnabled(false);
        list.setRefreshingLabel(getString(R.string.update));

        //8 LAYOUTS
        layoutSpinners = this.findViewById(R.id.layoutSpinners);
        layoutCalendars = this.findViewById(R.id.layoutFechas);

        //9 BOTONES CALENDARIOS
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        //10 BOTÓN FECHA INICIO
        imageButtonFechaInicio = (ImageButton) findViewById(R.id.imagenCalendarFI);
        editTextFechaInicio = (TextView) findViewById(R.id.fechaInicio);
        imageButtonFechaInicio.setOnClickListener(this);
        //Escuchador para realizar la búsqueda trás la actualización de la fecha.
        editTextFechaInicio.addTextChangedListener((new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                fechaInicioAct = editTextFechaInicio.getText().toString().replaceAll("\\s", "");
                fechaFinAct = editTextFechaFin.getText().toString().replaceAll("\\s", "");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        list.setRefreshing();
                    }
                }, 250);
            }
        }));

        //11 BOTÓN FECHA FIN
        imageButtonFechaFin = (ImageButton) findViewById(R.id.imagenCalendarFF);
        editTextFechaFin = (TextView) findViewById(R.id.fechaFin);
        imageButtonFechaFin.setOnClickListener(this);

        //12
        slide_me = new SimpleSideDrawer(this);
        slide_me.setRightBehindContentView(R.layout.menu_right);

        //13 BOTÓN MENÚ LATERAL
        btnMenuRight = this.findViewById(R.id.btnrightmenu);
        btnMenuRight.setOnClickListener(this);

        //14 MENÚ
        itemPresentacion = this.findViewById(R.id.presentacion);
        itemPresentacion.setOnClickListener(this);

        itemFormativas = this.findViewById(R.id.formativas);
        itemFormativas.setOnClickListener(this);

        //A mostrar/ocultar en dependencia del tipo de formación
        if (origen.equals(Constants.TIPO_ONLINE)) {
            layoutSpinners.setVisibility(View.GONE);
            //Se cambia el título
            TextView tv = (TextView) this.findViewById(R.id.title);
            tv.setText("");
            tv = (TextView) findViewById(R.id.title);
            tv.setText(R.string.title_onlines);
            //Búsqueda automática
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    list.setRefreshing();
                }
            }, 250);

        } else if(origen.equals(Constants.TIPO_PRESENCIAL)) {
            layoutCalendars.setVisibility(View.GONE);
            //Se cambia el título
            TextView tv = (TextView) this.findViewById(R.id.title);
            tv.setText("");
            tv = (TextView) findViewById(R.id.title);
            tv.setText(R.string.title_presenciales);
            Utils.showSimpleDialog(ActividadesFormativasActivity.this, null,
                    getString(R.string.presenciales_error), null);
        }
    }

    /**
     * Método que se lanza al seleccionar un valor en un spinner.
     *
     * @param adapter
     * @param arg1
     * @param index
     * @param arg3
     */
    @Override
    public void onItemSelected(AdapterView<?> adapter, View arg1, int index, long arg3) {
        if (adapter == localidad) {
            if (index == 0) {
                centroVal = "";
            } else {
                centroVal = ((Family) localidad.getSelectedItem()).getCode();
                if(!tipoVal.isEmpty() && !tipoVal.equals("")){
                    list.setRefreshing();
                }
            }
        } else if (adapter == tipo) {
            if (index == 0) {
                tipoVal = "";
            } else {
                tipoVal = ((Family) tipo.getSelectedItem()).getCode();
                if(!centroVal.isEmpty() && !centroVal.equals("")){
                    list.setRefreshing();
                }
            }
        }
        //Se lanza de forma automática mostrando en la lista
        //la consulta de actividades online
        if (origen.equals(Constants.TIPO_ONLINE)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    list.setRefreshing();
                }
            }, 250);
        }
    }

    /**
     * Se lanza cuando se arrastra en la lista.
     * @param refreshView
     */
    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        //search-> valor del campo de texto de búsqueda
        //provinciaVal -> valor del spinner de provincias
        //tipoVal -> valor del spinner de tipo de actividad
        requestData(true, search.getText().toString());
    }

    //@Override
    protected void onScrollNext() {
        requestData(false, search.getText().toString());
    }

    /**
     * @param clear
     * @param search
     */
    private void requestData(boolean clear, String search) {
        // To prevent another scrolling event
        adapter.lock(); //Objeto de tipo BoardAdapter

        // if (!clear && data != null
        // && (data.size() == 0 || data.get(data.size() - 1) != null)) {
        // data.add(null);
        // adapter.notifyDataSetChanged();
        // }
        if (currentCallId != null) {
            CyLDFormacionHandler.cancelCall(currentCallId);
        }
        //Se limpia el ArrayList de nuevos datos de Actividades
        datosFuturos.clear();
        currentCallId = "education" + new Date().getTime();
        currentCallTime = new Date().getTime();
        //Si no hay valor en ninguno de los campos
        if (origen.equals(Constants.TIPO_ONLINE)) {
            CyLDFormacionHandler.listActivitiesOnline(currentCallId, ActividadesFormativasActivity.this, fechaInicioAct, fechaFinAct); // Parámetro this válido porque se implementa CyLDFormacionHandlerListener
        } else if (origen.equals(Constants.TIPO_PRESENCIAL)) {
            CyLDFormacionHandler.listActivitiesPresencial(0, null, null, search, tipoVal, centroVal, currentCallId, ActividadesFormativasActivity.this);
        }
    }// requestData

    @Override
    public void onNothingSelected(AdapterView<?> adapter) {
        if (adapter == localidad) {
            centroVal = "";
        } else if (adapter == tipo) {
            tipoVal = "";
        }
        list.setRefreshing();//Se vió la razón de esto.TODO
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
        if (v == back) {
            //this.onBackPressed(); //retorna a la actividad anterior
            Intent i = new Intent(this, MenuFormacionActivity.class);
            finish();
            startActivity(i);
        }else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        } else if (v == botonPrueba) {
            requestData(false, search.getText().toString());
        } else if (v == imageButtonFechaInicio) {
            showDialog(0);
        } else if (v == imageButtonFechaFin) {
            showDialog(1);
        } else if (v == itemPresentacion) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, PresentacionActivity.class);
            finish();
            startActivity(i);
        } else if (v == itemFormativas) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, MenuFormacionActivity.class);
            finish();
            startActivity(i);
        }
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new DatePickerDialog(this, datePickerListenerI, year, month, day);
        } else if (id == 1) {
            return new DatePickerDialog(this, datePickerListenerF, year, month, day);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListenerI = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            editTextFechaInicio.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListenerF = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            editTextFechaFin.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };


    @Override
    public void onResults(String callId, Collection<CyLDFormacion> res, final boolean expectRefresh){

        if (callId.equalsIgnoreCase(currentCallId)) {

            datosFuturos.clear();
            datosFuturos.addAll(res);

            if (!expectRefresh) {

                long dif = currentCallTime - new Date().getTime();
                dif = dif + LOADING_DELAY;
                if (dif < 0)
                    dif = 0;

                list.postDelayed(new Runnable() {
                    public void run() {
                        data.clear();
                        data.addAll(datosFuturos);
                        list.onRefreshComplete();
                        list.setScrollingWhileRefreshingEnabled(true);
                        adapter.unlock();
                        adapter.notifyDataSetChanged();
                        count.setText(String.format(getString(R.string.cuenta_actividades),
                                data.size()));
                        count.setVisibility(View.VISIBLE);
                        if (data.size() == 0) {
                            Utils.showSimpleDialog(ActividadesFormativasActivity.this, null,
                                    getString(R.string.no_results), null);
                        }
                    }
                }, dif);

            }
        }// if currentCallId
    }

    @Override
    public void onError(String callId, Exception e, boolean expectRefresh) {
        e.printStackTrace();

        if (!expectRefresh) {

            data.clear();
            data.addAll(datosFuturos);

            list.onRefreshComplete();
            adapter.unlock();
            adapter.notifyDataSetChanged();
            count.setVisibility(View.VISIBLE);
            if (data.size() == 0) {
                count.setText(getString(R.string.server_error_short));
                Utils.showSimpleDialog(this, null, getString(R.string.server_error), null);
            }else{
                count.setText(String.format(getString(R.string.cuenta_actividades),
                        data.size()));
            }
        }
    } //onError

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
    }// FamilyAdapter

    /**
     * Extiende de AutoPagingListAdapter
     * Implementa interfaz OnItemClickListener: implementar onItemClick()
     * Extiende AutoPagingListAdapter: implementar getView()
     * Rellena la lista de items
     *
     */
    private class BoardAdapter extends AutoPagingListAdapter<CyLDFormacion>
            implements AdapterView.OnItemClickListener {

        public BoardAdapter() {
            super(ActividadesFormativasActivity.this, R.layout.item_actividad, data);
        }

        /**
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);

            if (convertView == null) {
                if (type == TYPE_ITEM) {
                    convertView = getLayoutInflater().inflate(
                            R.layout.item_actividad, null); //Pillo el Layout item_actividad
                } else if (type == TYPE_LOADING) {
                    convertView = getLayoutInflater().inflate(
                            R.layout.item_menu, null);
                }
            }

            if (type == TYPE_ITEM) {
                CyLDFormacion it = getItem(position); //Objeto CyLDFormacion

                TextView tv = (TextView) convertView.findViewById(R.id.titulo_actividad); //textView titulo_actividad de item_actividad
                tv.setText(it.nombre); //Le paso el valor del nombre

                //Centro. Solo si es presencial
                tv = (TextView) convertView.findViewById(R.id.centro_realizacion);
                String location = "";
                if (it.centro != null && !it.centro.equals("")) {
                    location = it.centro;
                    tv.setText(location);
                }

                //Fecha de Inicio
                tv = (TextView) convertView.findViewById(R.id.fecha_realizacion);
                if (it.fechaInicio != null) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(it.fechaInicio);//M may. para obtener el mes correcto
                        tv.setText(sdf.format(date));
                    }catch (ParseException pe) {
                        System.err.println("Problemas parseando fecha de inicio actividad formativa");
                    }
                } else {
                    tv.setText("");
                }
                //Duración en horas
                tv = (TextView) convertView.findViewById(R.id.total_horas);
                tv.setText(Float.toString(it.numeroHoras) + " horas");

            } else if (type == TYPE_LOADING) {
                TextView tv = (TextView) convertView.findViewById(R.id.text);
                tv.setText("Cargando...");
            }

            return convertView;
        }

        @Override
        protected void onScrollNext() {
            requestData(false, search.getText().toString());
        }

        /**
         * Evento que se ejecuta cuando se selecciona una de las actividades de la lista.
         *
         * @param arg0
         * @param arg1
         * @param position
         * @param arg3
         */
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            CyLDFormacion it = getItem(position - 1);
            if (it != null) {
                //Globals.selectedItem = it;
                Intent i = new Intent(ActividadesFormativasActivity.this, DetalleActividadActivity.class);
                i.putExtra("SELECTED_ITEM", it);
                startActivity(i);
            }
        }
    }//BoardAdapter
}
