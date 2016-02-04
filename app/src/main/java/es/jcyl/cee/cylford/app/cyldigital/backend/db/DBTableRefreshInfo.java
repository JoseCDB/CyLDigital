package es.jcyl.cee.cylford.app.cyldigital.backend.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by josecarlos.delbarrio on 04/02/2016.
 */
public class DBTableRefreshInfo extends DBTable {
    public static final String NAME = "CYLD_REFRESHINFO";
    public static final String COL_NAME = "NAME";
    public static final String COL_REFRESHDATE = "REFRESHDATE";



    public static String[] COLUMNS = {
            COL_NAME,
            COL_REFRESHDATE
    };

    private static final StringBuffer CREATE_DB = new StringBuffer()
            .append("CREATE TABLE ").append(NAME).append("(")
            .append(COL_NAME).append(" TEXT PRIMARY KEY, ")
            .append(COL_REFRESHDATE).append(" INTEGER ")
            .append(")");



    private void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_DB.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        System.out.println("Created table " + NAME);
    }

    public void onUpgrade(SQLiteDatabase db, int from, int to) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME);
        onCreate(db);
    }

}
