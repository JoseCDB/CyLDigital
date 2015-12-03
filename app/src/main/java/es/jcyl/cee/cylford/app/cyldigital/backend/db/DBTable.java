package es.jcyl.cee.cylford.app.cyldigital.backend.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

/**
 * Created by josecarlos.delbarrio on 27/10/2015.
 */
public abstract class DBTable {
    public static final String SEPARATOR_ARRAY = "|";
    public static final String SEPARATOR_ARRAY_ESCAPED = "\\|";

    protected static String join(List<String> what) {
        StringBuffer to = new StringBuffer();

        for (String val: what) {
            if (to.length() > 0) {
                to.append(DBTable.SEPARATOR_ARRAY);
            }
            to.append(val);
        }
        return to.toString();
    }
    protected static String joinDates(List<Date> what) {
        StringBuffer to = new StringBuffer();

        for (Date val: what) {
            if (to.length() > 0) {
                to.append(DBTable.SEPARATOR_ARRAY);
            }
            to.append(val.getTime());
        }
        return to.toString();
    }
    protected static List<String> split(String what) {

        List<String> tmp = new java.util.ArrayList<String>();
        if (what == null) {
            return tmp;
        }
        String []splitted = what.split(DBTable.SEPARATOR_ARRAY_ESCAPED);
        for (String add: splitted) {
            tmp.add(add);
        }
        return tmp;
    }

    protected static List<Date> splitDates(String what) {
        List<Date> tmp = new java.util.ArrayList<Date>();

        if (what != null && what.length() > 0) {
            String []splitted = what.split(DBTable.SEPARATOR_ARRAY_ESCAPED);
            for (String add: splitted) {
                tmp.add(new Date(Long.parseLong(add)));
            }
        }
        return tmp;
    }

    public abstract void onCreate(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int from, int to);
}
