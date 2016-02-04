package es.jcyl.cee.cylford.app.cyldigital.backend.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.Normalizer;
import java.util.Collection;
import java.util.List;

import es.jcyl.cee.cylford.app.cyldigital.backend.App;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;

/**
 * Created by josecarlos.delbarrio on 27/10/2015.
 */
public class DBDataSource {

    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    /**
     * Constructor
     */
    public DBDataSource() {
        System.out.println("logjc DBDataSource constructor");
        dbHelper = new DatabaseHelper(App.getContext());
    }

    public void open() {
        System.out.println("logjc DBDataSource open");
        //Creamos Objeto SQLiteDatabase.
        //Con método que DatabaseHelper hereda de SQLiteOpenHelper
        db = dbHelper.getWritableDatabase(); //Hasta que no se llama este método, la DB no se crea (no se llama al onCreate)
        System.out.println("logjc DBDataSource getWritableDatabase");
    }


    /* --------- ACTIVIDADES ------------ */

    /**
     * Realiza la consulta (con los datos aportados) a la base de datos SQLite.
     * @param tipoFormacion Online o Presencial
     * @param text Texto de búsqueda en descripción
     * @param tipoActividad Tipo curso, taller o charla
     * @param provincia Localidad donde se realiza (Presencial)
     * @return Collection<CyLDFormacion> con los datos de actividades Online o Presenciales recuperadas.
     * */
    public Collection<CyLDFormacion> listActivities(String tipoFormacion, String text, String tipoActividad, String provincia) throws SQLException {
        // Filtros para la query
        StringBuffer osConditions = new StringBuffer();

        /* ------------ TEXTO LIBRE ------------ */
        // Se compara el campo de texto libre con la descripción o el nombre de la actividad. En caso de haber.
        if (text != null && text.length() > 0) {
            //Se realiza un normalizado Unicode del texto. Decomposición canónica "NFD".
            String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

            osConditions.append("(").append(DBTableActivities.COL_DESCRIPCION).append(" LIKE '%").append(normalized).append("%' ")
                    .append(" OR ").append(DBTableActivities.COL_NOMBRE).append(" LIKE '%").append(normalized).append("%'")
                    .append(")");
        }
        /* ------------ PARÁMETROS DE BÚSQUEDA ------------ */

        // Filtro PROVINCIA
        if (provincia != null && provincia.length() > 0) {
            if (osConditions.length() > 0) {
                osConditions.append(" AND ");
            }
            //osConditions.append("( ");
            osConditions.append(DBTableActivities.COL_PROVINCIA).append(" = '").append(provincia).append("' ");
            //osConditions.append(" )");
        }

        // Filtro TIPO_ACTIVIDAD
        if (tipoActividad != null && tipoActividad.length() > 0) {
            if (osConditions.length() > 0) {
                osConditions.append(" AND ");
            }
            osConditions.append(DBTableActivities.COL_TIPO).append(" = '").append(tipoActividad).append("' ");
        }

        // Filtro TIPO FORMACIÓN
        if (tipoFormacion != null && tipoFormacion.length() > 0) {
            if (osConditions.length() > 0) {
                osConditions.append(" AND ");
            }
            osConditions.append(DBTableActivities.COL_TIPO_FORMACION).append(" = '").append(tipoFormacion).append("' ");
        }

        Cursor c = db.query(DBTableActivities.NAME, DBTableActivities.COLUMNS, osConditions.toString(),
                null, // SELECTION ARGS
                null, //GROUP BY
                null, //HAVING
                DBTableActivities.COL_FECHA_INICIO + " DESC, " + //ORDER BY
                        DBTableActivities.COL_NOMBRE + " ASC");
        //Mueve el cursor a la primera fila.
        c.moveToFirst();
        try {
            List<CyLDFormacion> acts = DBTableActivities.pasaCursorAListaModelos(c);
            return acts;
        } finally {
            c.close();
        }
    }

    /**
     * Elimina las actividades que hubiese en Base de datos e inserta las nuevas.
     * @param actividades Colección con actividades
     * @return boolean
     */
    public boolean sustituyeActividades(Collection<CyLDFormacion> actividades) {
        boolean success = true;
        // Inicia la transacción en modo EXCLUSIVO
        db.beginTransaction();
        try {
            // 1º Se eliminan todas las actividades.
            eliminaActividades();
            // 2º Se insertan una a una las nuevas actividades.
            for (CyLDFormacion act: actividades) {
                if (!insertaActividad(act)) {
                    success = false;
                    break;
                }
            }
            /*
            //En la original existe una tabla (ECYL_REFRESHINFO) con nombre de resto de tablas y fecha de actualización.
            if (success) {
                success = insertOrUpdateRefreshDate(DBTableActivities.NAME, System.currentTimeMillis()); //TODO: Refrescos
            }
            */
            if (success) {
                db.setTransactionSuccessful(); // 3º Se marca la transacción como exitosa.
            }
            return success;
        } catch (SQLException e) {
            return false;
        }
        finally
        {
            db.endTransaction();
        }
    }

    /**
     * Elimina las filas existentes en la Tabla CYLD_ACTIVITIES.
     * @return void
     */
    public void eliminaActividades() {
        //Se realiza el borrado de la tabla.
        db.delete(DBTableActivities.NAME, null, null);
    }

    /**
     * Inserta una actividad en la tabla CYLD_ACTIVITIES.
     * @param act actividad a insertar.
     * @return boolean Éxito o no en la operación de inserción.
     */
    public boolean insertaActividad(CyLDFormacion act) {
        ContentValues values = DBTableActivities.pasaDatosModeloAValores(act);
        try {
            //Se realiza el insert en la tabla.
            db.insertOrThrow(DBTableActivities.NAME, null, values);
        } catch (SQLException sqle) {
            System.out.println("Imposible insertar la actividad");
            sqle.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Obtiene el último refresco de la tabla pasáda como parámetro.
     * @param table
     * @return
     */
    public long getLastRefreshFor(String table) {
        Cursor c = db.query(DBTableRefreshInfo.NAME, DBTableRefreshInfo.COLUMNS, DBTableRefreshInfo.COL_NAME + " = '" + table + "'", null, null, null, null);
        if (!c.moveToFirst()) {
            return -1;
        }
        try {
            if (c.isAfterLast()) {
                return -1;
            }
            long timestamp = c.getLong(1);

            return timestamp;
        } finally {
            c.close();
        }
    }


    }


