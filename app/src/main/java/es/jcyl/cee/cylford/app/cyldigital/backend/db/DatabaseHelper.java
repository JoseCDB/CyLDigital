package es.jcyl.cee.cylford.app.cyldigital.backend.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by josecarlos.delbarrio on 27/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DB_VERSION = 8; // Se tuvo que subir el nº de version para que me volviese a crear la bbdd.
    private static final String DB_NAME = "CyLD";

    private static DBTable[] tables = {
        new DBTableActivities(),
        new DBTableRefreshInfo()
    };

    /**
     * Constructor
     * Esta clase extiende de SQLiteOpenHelper. Hay que pasarle > super(context, name, factory, version);
     */
    public DatabaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        System.out.println("logjc DatabaseHelper constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("logjc Inicio: DatabaseHelper onCreate");
        for (DBTable table: tables) {
            table.onCreate(db);
        }
        System.out.println("logjc Creadas CYLD_ACTIVITIES y CYLD_REFRESHINFO");
        System.out.println("logjc Fin: DatabaseHelper onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("logjc Inicio: DatabaseHelper onUpgrade");
        for (DBTable table: tables) {
            table.onUpgrade(db, oldVersion, newVersion);//al tener nueva versión actualiza la BBDD
        }
        System.out.println("logjc Fin: DatabaseHelper onUpgrade");
    }
}
