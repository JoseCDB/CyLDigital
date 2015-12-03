package es.jcyl.cee.cylford.app.cyldigital.backend.ws;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import es.jcyl.cee.cylford.app.cyldigital.backend.App;

/**
 * Created by josecarlos.delbarrio on 13/10/2015.
 */
public class FormCylDigWS extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * Nos dice si la red está accesible
     */
    protected static boolean estaRedAccesible() {
        //ConnectivityManager: Class that answers queries about the state of network connectivity
        NetworkInfo ni = App.getConnectivityManager().getActiveNetworkInfo();
        return ni != null && ni.isConnected() && ni.isAvailable();
    }

    /**
     * Genera el objeto HttpURLConnection que se utilizará
     * en el InputStream que recupera las actividades.
     * */
    protected static HttpURLConnection enviarRequestA(String sUrl, int connectTimeOut, int readTimeOut) throws IOException {
        URL url = null;
        HttpURLConnection cnt = null;

        url = new URL(sUrl);
        cnt = (HttpURLConnection) url.openConnection();
        cnt.setRequestMethod("GET");
        //cnt.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:26.0) Gecko/20100101 Firefox/26.0");
        //cnt.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        cnt.setConnectTimeout(connectTimeOut);
        cnt.setReadTimeout(readTimeOut);
        return cnt;
    }

    public static Context getContext() {
        return mContext;
    }
}
