package es.jcyl.cee.cylford.app.cyldigital.backend;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.jcyl.cee.cylford.app.cyldigital.backend.ws.FormCylDigWS;

/**
 * Created by josecarlos.delbarrio on 09/10/2015.
 */
public class Configuration {

    private static Configuration _instance_ = null;

    private Properties props = null;

    //el valor de estas constantes lo recogen en un documento aparte en "res/raw/configuration.properties"
    public static final String KEY_FORM_ONLINE_WSURL = "https://admin.sigecyl.es/servicios/actividades/actividadesOnline?";

    public static final String KEY_FORM_PRESEN_WSURL = "https://admin.sigecyl.es/servicios/actividades/actividadesPresenciales?";

    public static final String KEY_FORM_CONNECTTIMEOUT = "60";

    public static final String KEY_FORM_READTIMEOUT = "60";

    //24 hours
    public static final String KEY_ACTIVITIES_DATAEXPIRATIONTIME = "86400";


    /**
     * Constructor usado para obtener las propiedades de
     * "res/raw/configuration.properties"
     * */
    /*
    private Configuration() {
        props = new Properties();
        InputStream is = FormCylDigWS.getContext().getResources().openRawResource(R.raw.config);
        try {
            props.load(is);
        } catch (IOException ioe) {
            System.err.println("Problems loading properties");
            props = null;
        }

    }

    public static String getProperty(String prop) {
        synchronized (Configuration.class) {
            if (_instance_ == null) {
                _instance_ = new Configuration();
            }
        }
        return _instance_.innerGetProperty(prop);
    }

    private String innerGetProperty(String prop) {
        if (props != null) {
            return props.getProperty(prop);
        } else {
            return null;
        }
    }
    */
}
