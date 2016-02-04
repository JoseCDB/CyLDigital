package es.jcyl.cee.cylford.app.cyldigital.backend;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import es.jcyl.cee.cylford.app.cyldigital.backend.db.DBDataSource;

/**
 * Created by josecarlos.delbarrio on 27/10/2015.
 * Clase principal desde la cual se arranca la aplicación.
 * Se ha de añadir en "Manifest > application" propiedad android:name, para que se cree el objeto al iniciar.
 */
public class App extends Application {

    private static Context mContext;

    private static DBDataSource ds = null;


    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("logjc App onCreate 1");
        mContext = this.getBaseContext();
        System.out.println("logjc App onCreate 2");
        ds = new DBDataSource();
        ds.open();
        System.out.println("logjc App onCreate 3");
        //Ni idea de para que es esto
        //PRNGFixes.apply();
    }

    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static DBDataSource getDataSource() {
        return ds;
    }

    public static Context getContext(){
        return mContext;
    }


}
