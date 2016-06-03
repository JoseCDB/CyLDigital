package es.jcyl.cee.cylford.app.cyldigital.backend.handler;

import android.database.SQLException;

import java.util.Collection;

import es.jcyl.cee.cylford.app.cyldigital.Constants;
import es.jcyl.cee.cylford.app.cyldigital.backend.Configuration;
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

    private static final String REMOTERETRIEVE_TOKEN = "wholeActivities";// Token used to identify refresh operations..if another
                                                                         // refresh is required from another method and this token is on
                                                                         // the pending listeners it means it is already being refreshed

    static {
        String sValue = Configuration.KEY_ACTIVITIES_DATAEXPIRATIONTIME;//86400 segundos tiene un día
        dataExpirationTime = Long.parseLong(sValue) * 1000; //lo que tardan en expirar las actividades consultadas es
    }

    //Objeto de clase saverBlock (de clase anónima que implementa la Interfaz CollectionDataSaverBlock) para hacer el guardado en BD de la Colección pasada
    private static CollectionDataSaverBlock<CyLDFormacion> saverBlock = new CollectionDataSaverBlock<CyLDFormacion>() {
        @Override
        public void storeData(Collection<CyLDFormacion> data) {
            guardaActividades(data);
        }

    };

    /***** MÉTODOS ******/

    /**
     * Método que crea el objeto CollectionDataRetrieverBlock con métodos
     * que realizan las consultas de actividades presenciales en local y remoto.
     *
     * @param text texto para búsqueda de actividades
     * @param tipoActividad Tipo de actividad formativa a buscar. Seleccionado en el Spinner.
     * @param centro Lugar donde tendrá encuentro la actividad formativa. Seleccionado en el Spinner.
     * @param callId
     * @param listener
     * @return void
     */
    public static void listActivitiesPresencial(final int numeroAct, final String fechaInicio, final String fechaFin,
                                      final String text, final String tipoActividad, final String centro,
                                      final String callId, final OnlineActivity listener) {
        startCallId(callId);

        //**Se crea un objeto "retrieverBlock" de una clase anónima interna que implementa la interfaz "CollectionDataRetrieverBlock" declarada en CyLDHandler.
        CollectionDataRetrieverBlock<CyLDFormacion> retrieverBlock = new CollectionDataRetrieverBlock<CyLDFormacion>() {
            //Esta nueva clase, debe implementar los métodos de la interfaz (necesariamente).

            @Override
            public Collection<CyLDFormacion> localRetrieve() throws SQLException {
                try {
                    // Desde la base de datos se obtienen datos de actividades.
                    Collection<CyLDFormacion> data = App.getDataSource().listActivities(Constants.TIPO_PRESENCIAL, text, tipoActividad, centro, null, null);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SQLException("Imposible obtener datos de la base de datos");
                }
            }

            @Override
            public Collection<CyLDFormacion> remoteRetrieve() throws CyLDWServiceException {
                // Desde el Web Service se obtienen datos de actividades presenciales.
                return getActividadesDesdeWebservice(Constants.TIPO_PRESENCIAL, numeroAct, null, null, text, tipoActividad, centro, listener);
            }

            @Override
            public Object getRemoteRetrieveToken() {
                return REMOTERETRIEVE_TOKEN;
            }
        };//**Fin Objeto "retrieverBlock"
        //Se le pasa el objeto retrieverBlock que hará la llamada a la consulta local, si hace falta se llama al WS y el saverBlock guarda las nuevas.
        //executeCollectionRetrieval(callId, dataExpirationTime, DBTableActivities.NAME, retrieverBlock, saverBlock, listener);//JC11052016
        executeCollectionRetrieval(callId, dataExpirationTime, DBTableActivities.NAME + "_" + Constants.TIPO_PRESENCIAL + "_" + centro + "_" + tipoActividad, retrieverBlock, saverBlock, listener);
    }

    /**
     * Método que crea el objeto CollectionDataRetrieverBlock con métodos
     * que realizan las consultas de actividades online en local y remoto.
     *
     * @param callId
     * @param listener
     */
    public static void listActivitiesOnline(final String callId, final OnlineActivity listener, final String fechaInicio, final String fechaFin) {
        startCallId(callId);

        //**Añado un objeto "retrieverBlock" de una clase anónima interna que implementa la interfaz "CollectionDataRetrieverBlock" declarada en CyLDHandler
        CollectionDataRetrieverBlock<CyLDFormacion> retrieverBlock = new CollectionDataRetrieverBlock<CyLDFormacion>() {
            //Esta nueva clase, debe implementar los métodos de la interfaz (necesariamente).

            @Override
            public Collection<CyLDFormacion> localRetrieve() throws SQLException {
                try {
                    // Desde la base de datos se obtienen datos de actividades.
                    Collection<CyLDFormacion> data = App.getDataSource().listActivities(Constants.TIPO_ONLINE, null, null, null, fechaInicio, fechaFin);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SQLException("Imposible obtener datos de la base de datos");
                }
            }

            @Override
            public Collection<CyLDFormacion> remoteRetrieve() throws CyLDWServiceException {
                // Desde el Web Service se obtienen datos de actividades.
                return getActividadesDesdeWebservice(Constants.TIPO_ONLINE, 0, null, fechaInicio, fechaFin, null, null, listener);
            }

            @Override
            public Object getRemoteRetrieveToken() {
                return REMOTERETRIEVE_TOKEN;
            }
        };//**Fin Objeto "retrieverBlock"
        //Se le pasa el objeto retrieverBlock que hará la llamada a la consulta local, si hace falta se llama al WS y el saverBlock guarda las nuevas.
        //executeCollectionRetrieval(callId, dataExpirationTime, DBTableActivities.NAME, retrieverBlock, saverBlock, listener); //JC11052016
        executeCollectionRetrieval(callId, dataExpirationTime, DBTableActivities.NAME + "_" + Constants.TIPO_ONLINE, retrieverBlock, saverBlock, listener);
    }

    /**
     * Método que realiza la llamada al método getActividades de la
     * clase FormCylDigOnlineWS que se ocupa de realizar la consulta al WS.
     *
     * @return Collection<CyLDFormacion> con las diferentes actividades online o presenciales consultadas.
     * @throws CyLDWServiceException
     */
    private static Collection<CyLDFormacion> getActividadesDesdeWebservice(String tipoFormacion, int numeroAct, String fechaInicio, String fechaFin,
                                                                           String text, String tipoActividad, String provincia, OnlineActivity listener){
        try {
            return FormCylDigOnlineWS.getActividades(tipoFormacion, numeroAct, fechaInicio, fechaFin, text, tipoActividad, provincia, listener);
        } catch (CyLDWServiceException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método utilizado por el atributo de clase de tipo CollectionDataSaverBlock para
     * a través de objeto DBDataSource de App, eliminar las actividades existentes
     * en local y sustituirlas por las recuperadas de servicio web.
     *
     * @param actividades
     */
    private static void guardaActividades(Collection<CyLDFormacion> actividades) {
        App.getDataSource().sustituyeActividades(actividades);
    }
}
