package es.jcyl.cee.cylford.app.cyldigital.backend.handler;

import android.database.SQLException;

import java.util.Collection;

import es.jcyl.cee.cylford.app.cyldigital.backend.db.DBTableActivities;
import es.jcyl.cee.cylford.app.cyldigital.backend.ws.CyLDWServiceException;
import es.jcyl.cee.cylford.app.cyldigital.backend.ws.FormCylDigOnlineWS;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;
import es.jcyl.cee.cylford.app.cyldigital.backend.App;
import es.jcyl.cee.cylford.app.cyldigital.ui.OnlineActivity;

/**
 * Created by josecarlos.delbarrio on 20/10/2015.
 */
public class CyLDFormacionHandler extends CyLDHandler {


    /***** ATRIBUTOS ******/

    private static long dataExpirationTime = -1;

    private static final String REMOTERETRIEVE_TOKEN = "wholeEducations";

    //Objeto de clase saverBlock (de clase anónima que implementa la Interfaz CollectionDataSaverBlock) para hacer el guardado en BD de la Colección pasada
    private static CollectionDataSaverBlock<CyLDFormacion> saverBlock = new CollectionDataSaverBlock<CyLDFormacion>() {
        @Override
        public void storeData(Collection<CyLDFormacion> data) {
            guardaActividades(data);
        }

    };



    /***** MÉTODOS ******/

    /**
     * Método que crea el objeto con los métodos (localRetrieve, remoteRetrieve) que realizan las consultas de actividades.
     * @param text
     * @param tipoActividad
     * @param provincia
     * @param callId
     * @param listener
     * @return void
     */
    public static void listActivities(final String tipoFormacion, final int numeroAct, final String fechaInicio, final String fechaFin,
                                      final String text, final String tipoActividad, final String provincia,
                                      final String callId, final OnlineActivity listener) {
        startCallId(callId);

        //**Se crea un objeto "retrieverBlock" de una clase anónima interna que implementa la interfaz "CollectionDataRetrieverBlock" declarada en CyLDHandler.
        CollectionDataRetrieverBlock<CyLDFormacion> retrieverBlock = new CollectionDataRetrieverBlock<CyLDFormacion>() {
            //Esta nueva clase, debe implementar los métodos de la interfaz (necesariamente).

            @Override
            public Collection<CyLDFormacion> localRetrieve() throws SQLException {
                try {
                    // Desde la base de datos se obtienen datos de actividades.
                    Collection<CyLDFormacion> data = App.getDataSource().listActivities(tipoFormacion, text, tipoActividad, provincia);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SQLException("Imposible obtener datos dela base de datos");
                }
            }

            @Override
            public Collection<CyLDFormacion> remoteRetrieve() throws CyLDWServiceException {
                // Desde el Web Service se obtienen datos de actividades.
                return getActividadesDesdeWebservice(tipoFormacion, numeroAct, fechaInicio, fechaFin, text, tipoActividad, provincia, listener);
            }

            @Override
            public Object getRemoteRetrieveToken() {
                return REMOTERETRIEVE_TOKEN;
            }
        };//**Fin Objeto "retrieverBlock"

        executeCollectionRetrieval(callId, dataExpirationTime, DBTableActivities.NAME, retrieverBlock, saverBlock, listener);

    } //**Fin Método listEducations



    public static void listActivities(final String tipoFormacion, final String callId, final OnlineActivity listener) {
        startCallId(callId);

        //**Añado un objeto "retrieverBlock" de una clase anónima interna que implementa la interfaz "CollectionDataRetrieverBlock" declarada en CyLDHandler
        CollectionDataRetrieverBlock<CyLDFormacion> retrieverBlock = new CollectionDataRetrieverBlock<CyLDFormacion>() {
            //Esta nueva clase, debe implementar los métodos de la interfaz (necesariamente).

            @Override
            public Collection<CyLDFormacion> localRetrieve() throws SQLException {
                try {
                    // Desde la base de datos se obtienen datos de actividades.
                    Collection<CyLDFormacion> data = App.getDataSource().listActivities(tipoFormacion, null, null, null);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SQLException("Imposible obtener datos de la base de datos");
                }
            }

            @Override
            public Collection<CyLDFormacion> remoteRetrieve() throws CyLDWServiceException {
                // Desde el Web Service se obtienen datos de actividades.
                return getActividadesDesdeWebservice(null, 0, null, null, null, null, null, listener);
            }

            @Override
            public Object getRemoteRetrieveToken() {
                return REMOTERETRIEVE_TOKEN;
            }
        };//**Fin Objeto "retrieverBlock"
    }


    /*
    * Método que realiza la llamada al método de llamada WS para Form Online
    * @return Collection<CyLDFormacion> con las diferentes actividades online o presenciales consultadas.
    * @throws CyLDWServiceException
    * **/
    private static Collection<CyLDFormacion> getActividadesDesdeWebservice(String tipoFormacion, int numeroAct, String fechaInicio, String fechaFin,
                                                                           String text, String tipoActividad, String provincia, OnlineActivity listener){
        try {
            return FormCylDigOnlineWS.getActividades(tipoFormacion, numeroAct, fechaInicio, fechaFin, text, tipoActividad, provincia, listener);
        } catch (CyLDWServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void guardaActividades(Collection<CyLDFormacion> actividades) {
        App.getDataSource().sustituyeActividades(actividades);
    }
}
