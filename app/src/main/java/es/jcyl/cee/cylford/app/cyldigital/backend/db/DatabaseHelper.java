package es.jcyl.cee.cylford.app.cyldigital.backend.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by josecarlos.delbarrio on 27/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DB_VERSION = 4; // Tuve que subir el nº de version para que me volviese a crear la bbdd.
    private static final String DB_NAME = "CyLD";

    DBTable tablaActividades  = new DBTableActivities();

    /**
     * Constructor
     */
    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        System.out.println("logjc DatabaseHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //for (DBTable table: tables) {
        System.out.println("logjc DatabaseHelper onCreate");
        tablaActividades.onCreate(db);
        //}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //for (DBTable table: tables) {
        tablaActividades.onUpgrade(db, oldVersion, newVersion);
        //}
    }
}
