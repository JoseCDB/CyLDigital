package es.jcyl.cee.cylford.app.cyldigital.backend.ws;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collection;

import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.backend.Configuration;

/**
 * Created by josecarlos.delbarrio on 13/10/2015.
 */
public class FormCylDigOnlineWS extends FormCylDigWS {

    //Tiempo de conexión
    private static int connectTimeOut = -1;
    //Tiempo de lectura de datos
    private static int readTimeOut = -1;

    //Iniciamos los valores
    static {
        String sValue = Configuration.KEY_FORM_CONNECTTIMEOUT;
        connectTimeOut = Integer.parseInt(sValue) * 1000;
        sValue = Configuration.KEY_FORM_READTIMEOUT;
        readTimeOut = Integer.parseInt(sValue) * 1000;
    }

    public static Collection getActividades(String tipoFormacion, int numeroAct, String fechaInicio, String fechaFin,
                                            String text, String tipoActividad, String provincia) throws CyLDWServiceException {
        Collection actividadesOn = null;
        InputStream is = null;
        HttpURLConnection cnt = null;
        StringBuffer sUrl = new StringBuffer(Configuration.KEY_FORM_ONLINE_WSURL);
        int cont = 0;

        if (!estaRedAccesible()) {
            throw new CyLDWServiceException("La red no está disponible");
        }
        fechaInicio= "10-10-2001";
        fechaFin = "10-10-2015";

        // Se completa la url en caso de ser tipo consulta tipo Online
        if(tipoFormacion.equals(Constants.TIPO_ONLINE)) {
            if (numeroAct > 0) {
                sUrl.append("numActividades=").append(numeroAct);
                cont++;
            }
            if (fechaInicio != null) {
                if(cont > 0){
                    sUrl.append("&");
                }
                sUrl.append("strFechaInicio=").append(fechaInicio);
            }

            if (fechaFin != null) {
                if(cont > 0){
                    sUrl.append("&");
                }
                sUrl.append("strFechaFin=").append(fechaFin);
            }
        }

        try {
            //parser = new ECyLEducationParser();
            System.out.println("FORMACIÓN ONLINE: Parseando respuesta desde " + sUrl);
            //llamada al método de FormCylDigWS con la url que se enviará con GET al WS
            cnt = enviarRequestA(sUrl.toString(), connectTimeOut, readTimeOut);
            is = new BufferedInputStream(cnt.getInputStream());
            //Collection actividadesOn = parser.parse(is);
            return actividadesOn;
        } catch (IOException ioe) {
            //hay que retornar algo...
            throw new CyLDWServiceException("No ha sido capáz de leer el stream");
        }
    }
}

