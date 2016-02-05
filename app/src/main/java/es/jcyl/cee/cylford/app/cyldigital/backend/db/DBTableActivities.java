package es.jcyl.cee.cylford.app.cyldigital.backend.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;

/**
 * Created by josecarlos.delbarrio on 27/10/2015.
 */
public class DBTableActivities extends DBTable {

    // Nombre TABLA.
    public static final String NAME = "CYLD_ACTIVITIES";

    // COLUMNAS tabla:
    public static final String COL_ID = "_id";

    // Campos Actividades Presenciales
    public static final String COL_TIPO = "CAM_TIP";
    public static final String COL_HORA_INICIO = "CAM_HOR_INI";
    public static final String COL_HORA_FIN = "CAM_HOR_FIN";
    public static final String COL_FECHA_INICIO_MATRIC = "CAM_FEC_INI_MAT";
    public static final String COL_FECHA_FIN_MATRIC = "CAM_FEC_FIN_MAT";
    public static final String COL_CENTRO = "CAM_CEN";
    public static final String COL_NIVEL = "CAM_NIV";

    // Campos Actividades Online
    public static final String COL_AGRUPACION = "CAM_AGR";
    public static final String COL_NOM_AGRUPACION = "CAM_NOM_AGR";
    public static final String COL_URL = "CAM_URL";

    // Campos Comunes
    public static final String COL_TIPO_FORMACION = "CAM_TIP_FOR"; //Online o Presencial

    public static final String COL_NOMBRE = "CAM_NOM";
    public static final String COL_DESCRIPCION = "CAM_DES";
    public static final String COL_FECHA_INICIO = "CAM_FEC_INI";
    public static final String COL_FECHA_FIN = "CAM_FEC_FIN";
    public static final String COL_NUM_HORAS = "CAM_NUM_HOR";
    public static final String COL_NUM_PLAZAS = "CAM_NUM_PLA";
    public static final String COL_NUM_SOLIC = "CAM_NUM_SOL";
    public static final String COL_NUM_PLAZAS_LISTA = "CAM_NUM_PLA_LIS";
    public static final String COL_REQUISITOS = "CAM_REQ";
    public static final String COL_AVISO = "CAM_AVI";
    public static final String COL_TEMATICA = "CAM_TEM";

    //public static final String METACOL_REFRESHEDON = "REFRESHEDON";

    // Array con los nombres de las columnas de la tabla CYLD_ACTIVITIES
    public static String[] COLUMNS = {
            COL_NOMBRE,
            COL_DESCRIPCION,
            COL_FECHA_INICIO,
            COL_FECHA_FIN,
            COL_NUM_HORAS,
            COL_NUM_PLAZAS,
            COL_NUM_SOLIC,
            COL_NUM_PLAZAS_LISTA,
            COL_AGRUPACION,
            COL_REQUISITOS,
            COL_AVISO,
            COL_TEMATICA,
            COL_NOM_AGRUPACION,
            COL_URL,
            COL_TIPO,
            COL_HORA_INICIO,
            COL_HORA_FIN,
            COL_FECHA_INICIO_MATRIC,
            COL_FECHA_FIN_MATRIC,
            COL_CENTRO,
            COL_NIVEL,
            COL_TIPO_FORMACION
            //METACOL_REFRESHEDON
    };

    // Buffer de script de creación de la tabla.
    private static final StringBuffer CREATE_DB = new StringBuffer()
            .append("CREATE TABLE IF NOT EXISTS ")
            .append(NAME).append("(")

            .append(COL_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COL_TIPO_FORMACION).append(" TEXT, ")

            .append(COL_TIPO).append(" TEXT, ")// Presenciales
            .append(COL_HORA_INICIO).append(" TEXT, ")
            .append(COL_HORA_FIN).append(" TEXT, ")
            .append(COL_FECHA_INICIO_MATRIC).append(" TEXT, ")
            .append(COL_FECHA_FIN_MATRIC).append(" TEXT, ")
            .append(COL_CENTRO).append(" TEXT, ")
            .append(COL_NIVEL).append(" TEXT, ")
            .append(COL_AGRUPACION).append(" INTEGER, ")// Online
            .append(COL_NOM_AGRUPACION).append(" TEXT, ")
            .append(COL_URL).append(" TEXT, ")
            .append(COL_NOMBRE).append(" TEXT COLLATE NOCASE, ")
            .append(COL_DESCRIPCION).append(" TEXT COLLATE NOCASE, ") /* collating sequence NOCASE. Columns will be handled case insensitive. 'smith' = 'Smith'  */
            .append(COL_FECHA_INICIO).append(" TEXT, ")
            .append(COL_FECHA_FIN).append(" TEXT, ")
            .append(COL_NUM_HORAS).append(" REAL, ")
            .append(COL_NUM_PLAZAS).append(" INTEGER, ")
            .append(COL_NUM_SOLIC).append(" INTEGER, ")
            .append(COL_NUM_PLAZAS_LISTA).append(" INTEGER, ")
            .append(COL_REQUISITOS).append(" TEXT, ")
            .append(COL_AVISO).append(" TEXT, ")
            .append(COL_TEMATICA).append(" TEXT ")

            .append(")");

            //.append(COL_IDENTIFIER).append(" INTEGER PRIMARY KEY, ")
           // .append(METACOL_REFRESHEDON).append(" INTEGER, ")

    private void createTable(SQLiteDatabase db) {
        //Se pasa a String el StringBuffer con el texto de creación de la tabla.
        System.out.println("logjc DBTableActivities createTable");
        db.execSQL(CREATE_DB.toString());
    }

    /**
     * Implementa el método abstracto de DBTable
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        System.out.println("logjc Tabla creada onCreate" + NAME);
    }

    /**
     * Implementa el método abstracto de DBTable
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int from, int to) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

    /**
     * Recorre el cursor añadiendo valores a objetos CyLDFormacion creando una Lista.
     * @param c
     * @return List<CyLDFormacion> con
     */
    public static List<CyLDFormacion> pasaCursorAListaModelos(Cursor c) {
        List<CyLDFormacion> lst = new java.util.LinkedList<CyLDFormacion>();
        //Si hay otra fila en el cursor..
        while (!c.isAfterLast()) {
            CyLDFormacion act = pasaCursorAModeloInividual(c);
            lst.add(act); //la añadimos a nuestra lista.
            c.moveToNext();
        }
        return lst;
    }

    /**
     * Rellena el objeto CyLDFormacion acon los valores del Cursor.
     * @param c Cursor con los valores para rellenar el objeto CyLDFormacion.
     * @return Objeto CyLDFormacion con los valores del de la actividad del cursor.
     */
    public static CyLDFormacion pasaCursorAModeloInividual(Cursor c) {
        CyLDFormacion act = new CyLDFormacion();
        //posicion de la columna del cursor
        int pos = 0;

        act.id = c.getInt(pos++);
        act.tipoFormación = c.getString(pos++);

        // Campos propios act. Presencial
        act.tipo = c.getString(pos++);
        act.horaInicio = c.getString(pos++);
        act.horaFin = c.getString(pos++);
        act.fechaInicioMatriculacion = c.getString(pos++);
        act.fechaFinMatriculacion = c.getString(pos++);
        act.centro = c.getString(pos++);
        act.nivel = c.getString(pos++);

        // Campos propios act. Online
        act.agrupacion = c.getInt(pos++);
        act.nombreAgrupacion = c.getString(pos++);
        act.url = c.getString(pos++);

        // Campos comunes
        act.provincia = c.getString(pos++);
        act.nombre = c.getString(pos++);
        act.descripcion  = c.getString(pos++);
        act.fechaInicio = c.getString(pos++);
        act.fechaFin = c.getString(pos++);
        act.numeroHoras = c.getFloat(pos++);
        act.numeroPlazas = c.getInt(pos++);
        act.numeroSolicitudes = c.getInt(pos++);
        act.plazasEnListaEspera = c.getInt(pos++);
        act.requisitos = c.getString(pos++);
        act.aviso = c.getString(pos++);
        act.tematica = c.getString(pos++);

        //edu.refreshedOn = c.getLong(pos++);

        return act;
    }

    /**
     * Pasa los valores contenidos en el objeto de tipo CyLDFormacion.
     * @param act
     * @return ContentValues
     */
    public static ContentValues pasaDatosModeloAValores(CyLDFormacion act) {

        ContentValues values = new ContentValues();

        values.put(COL_ID, act.id);

        values.put(COL_TIPO_FORMACION, act.tipoFormación);

        values.put(COL_TIPO, act.tipo);// Presenciales
        values.put(COL_HORA_INICIO, act.horaInicio);
        values.put(COL_HORA_FIN, act.horaFin);
        values.put(COL_FECHA_INICIO_MATRIC, act.fechaInicioMatriculacion);
        values.put(COL_FECHA_FIN_MATRIC, act.fechaFinMatriculacion);
        values.put(COL_CENTRO, act.centro);
        values.put(COL_NIVEL, act.nivel);
        values.put(COL_AGRUPACION, act.agrupacion);// Online
        values.put(COL_NOM_AGRUPACION, act.nombreAgrupacion);
        values.put(COL_URL, act.url);
        values.put(COL_NOMBRE, act.nombre);
        values.put(COL_DESCRIPCION, act.descripcion); /* collating sequence NOCASE. Columns will be handled case insensitive. 'smith' = 'Smith'  */
        values.put(COL_FECHA_INICIO, act.fechaInicio);
        values.put(COL_FECHA_FIN, act.fechaFin);
        values.put(COL_NUM_HORAS, act.numeroHoras);
        values.put(COL_NUM_PLAZAS, act.numeroPlazas);
        values.put(COL_NUM_SOLIC, act.numeroSolicitudes);
        values.put(COL_NUM_PLAZAS_LISTA, act.plazasEnListaEspera);
        values.put(COL_REQUISITOS, act.requisitos);
        values.put(COL_AVISO, act.aviso);
        values.put(COL_TEMATICA, act.tematica);

        /*
        values.put(COL_IDENTIFIER, edu.identifier);
        values.put(COL_TITLEES, edu.title);


        String normalized = null;
        if (edu.title != null) {
            normalized = Normalizer.normalize(edu.title, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            values.put(DBTableJobs.COL_TITLEES_DIACRITIC, normalized.toLowerCase());
            values.put(COL_DESCRIPTIONES, edu.description);
        } else {
            values.put(COL_DESCRIPTIONES, "");
        }

        if (edu.description != null) {
            normalized = Normalizer.normalize(edu.description, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            values.put(DBTableJobs.COL_DESCRIPTIONES_DIACRITIC, normalized.toLowerCase());
        } else {
            values.put(DBTableJobs.COL_DESCRIPTIONES_DIACRITIC, "");
        }

        if (edu.startDate != null) {
            values.put(COL_STARTDATE, edu.startDate.getTime());
        } else {
            values.putNull(COL_STARTDATE);
        }
        if (edu.locality == null) {
            System.out.println("Null?");
        }
        values.put(COL_LOCALITY, edu.locality);
        values.put(COL_PROVINCEID, edu.provinceId);
        values.put(COL_EXECUTIONPLACE, edu.executionPlace);
        values.put(COL_DURATION, edu.duration);
        values.put(COL_CONTENTSLINK, edu.contentsLink);
        values.put(COL_FAMILY, edu.family);
        values.put(COL_REGISTRATION, edu.registrationMean);
        values.put(METACOL_REFRESHEDON, System.currentTimeMillis());

        */
        return values;
    }


}
