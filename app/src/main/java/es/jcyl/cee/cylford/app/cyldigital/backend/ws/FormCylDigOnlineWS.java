package es.jcyl.cee.cylford.app.cyldigital.backend.ws;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.net.ssl.HttpsURLConnection;

import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.backend.Configuration;
import es.jcyl.cee.cylford.app.cyldigital.parser.CyLDActivityParser;
import es.jcyl.cee.cylford.app.cyldigital.parser.CyLDParserException;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;
import es.jcyl.cee.cylford.app.cyldigital.ui.OnlineActivity;

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

    public static Collection<CyLDFormacion> getActividades(String tipoFormacion, int numeroAct, String fechaInicio, String fechaFin,
                                            String text, String tipoActividad, String provincia, OnlineActivity listener) throws CyLDWServiceException {
        InputStream is = null;
        HttpsURLConnection cnt = null;
        StringBuffer sUrl = new StringBuffer(Configuration.KEY_FORM_ONLINE_WSURL);
        int cont = 0;

        if (!estaRedAccesible()) {
            throw new CyLDWServiceException("La red no está disponible");
        }
        fechaInicio= null; //null en ambas me devuelve todas las actividades
        fechaFin = null;

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
            CyLDActivityParser parser = new CyLDActivityParser();
            System.out.println("FORMACIÓN ONLINE: Parseando respuesta desde " + sUrl);
            //llamada al método de FormCylDigWS con la url que se enviará con GET al WS
            cnt = enviarRequestA(sUrl.toString(), connectTimeOut, readTimeOut, listener);
            is = new BufferedInputStream(cnt.getInputStream());
            //Guardado de fichero en sdcard
                /*
                try{
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");
                    dir.mkdirs();
                    File file = new File(dir, "filename.xml");
                    FileOutputStream f = new FileOutputStream(file);

                    byte[] buf =new byte[1024];
                    int len;
                    while((len=is.read(buf))>0){
                        f.write(buf,0,len);
                    }
                    f.close();
                    is.close();
                }catch(IOException e){
                    System.out.println("Se produjo el error : "+e.toString());
                }
                */

            Collection<CyLDFormacion> actividadesOn = parser.parse(is);
            return actividadesOn;
        } catch (IOException ioe) {
            throw new CyLDWServiceException("No ha sido capáz de leer el stream");//hay que retornar algo...
        }catch (CyLDParserException eclp) {
            System.out.println("Unable to read education courses");
            eclp.printStackTrace();
            throw new CyLDWServiceException("No ha sido capáz de leer el stream");//hay que retornar algo...
        } finally {
            if (is != null) {try { is.close(); } catch (IOException ioe) {}}
            if (cnt != null) { cnt.disconnect(); }
        }
    }
}

