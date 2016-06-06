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
import es.jcyl.cee.cylford.app.cyldigital.ui.ActividadesFormativasActivity;

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

    /**
     * Forma la Url dependiendo de si se consultan actividades de modalidad presencial u online
     * y realiza la petición de envío al servicio web.
     *
     * @param tipoFormacion
     * @param numeroAct
     * @param fechaInicio
     * @param fechaFin
     * @param text
     * @param tipoActividad
     * @param provincia
     * @param listener
     * @return
     * @throws CyLDWServiceException
     */
    public static Collection<CyLDFormacion> getActividades(String tipoFormacion, int numeroAct, String fechaInicio, String fechaFin,
                                            String text, String tipoActividad, String provincia, ActividadesFormativasActivity listener) throws CyLDWServiceException {
        InputStream is = null;
        HttpsURLConnection cnt = null;
        StringBuffer sUrl = new StringBuffer();
        int cont = 0;

        if (!estaRedAccesible()) {
            throw new CyLDWServiceException("La red no está disponible");
        }
        //Diferente url dependiendo del tipo de formación.
        if(tipoFormacion.equals(Constants.TIPO_ONLINE)){
            sUrl = new StringBuffer(Configuration.KEY_FORM_ONLINE_WSURL);
        } else if (tipoFormacion.equals(Constants.TIPO_PRESENCIAL)) {
            sUrl = new StringBuffer(Configuration.KEY_FORM_PRESEN_WSURL);
        }
        //Fechas de inicio y fin de actividades.
        fechaInicio= null; //null en ambas me devuelve todas las actividades.
        fechaFin = null;

        //Se completa la url con los posibles parámetros en caso de ser tipo consulta tipo Online
        if (numeroAct > 0) {
            sUrl.append("numActividades=").append(numeroAct);
            cont++;
        }
        if (fechaInicio != null) {
            if(cont > 0) {
                sUrl.append("&");
            }
            sUrl.append("strFechaInicio=").append(fechaInicio);
            cont++;
        }
        if (fechaFin != null) {
            if (cont > 0) {
                sUrl.append("&");
            }
            sUrl.append("strFechaFin=").append(fechaFin);
            cont++;
        }
        //Y si es búsqueda de actividades de tipo Presencial.
        //La no inclusión de uno de estos 2 parámetros en la consulta de actividades presenciales, devuelve mensaje de error.
        if (tipoFormacion.equals(Constants.TIPO_PRESENCIAL)) {
            if ((tipoActividad != null) && (provincia != null)) {
                if(cont > 0) {
                    sUrl.append("&");
                }
                sUrl.append("tipoActividad=").append(tipoActividad);
                cont++;
                if(cont > 0) {
                    sUrl.append("&");
                }
                sUrl.append("centro=").append(provincia);
                cont++;
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
            //Se realiza el parseo del xml recuperado en la consulta al servicio web.
            Collection<CyLDFormacion> actividadesOn = parser.parse(is);
            //Se agrega el tipo de formación a cada objeto de actividad recuperado.
            if(actividadesOn != null && actividadesOn.size() > 0) {
                agregaTipoFormacionActividad(actividadesOn, tipoFormacion);
            }

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

    /**
     * Método que añade el tipo de formación recuperada a la
     * colección de actividades de tipo CyLDFormacion.
     *
     * @param coleActividades colección de actividades recuperadas del servicio web.
     * @param tipo tipo de formación buscada, online o presencial.
     * @return boolean confirmando si ha habido algún cambio.
     */
    private static boolean agregaTipoFormacionActividad ( Collection<CyLDFormacion> coleActividades, String tipo) {
        boolean cambios = false;

        for(CyLDFormacion acti: coleActividades) {
            acti.tipoFormación = tipo;
            cambios= true;
        }
        return cambios;
    }
}

