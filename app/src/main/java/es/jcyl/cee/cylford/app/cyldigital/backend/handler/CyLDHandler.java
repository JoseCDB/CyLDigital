package es.jcyl.cee.cylford.app.cyldigital.backend.handler;

import android.database.SQLException;
import android.os.AsyncTask;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import es.jcyl.cee.cylford.app.cyldigital.backend.ws.CyLDWServiceException;
import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDDTO;

/**
 * Created by josecarlos.delbarrio on 21/10/2015.
 */
public class CyLDHandler {

    /***** ATRIBUTOS ******/

    // Set de Strings con las llamadas pendientes
    private static Set<String> llamadasPendientes = new java.util.HashSet<String>();

    // Mapa de, objeto "token remoto" y su lista de objetos ListenersConatainer
    private static Map<Object, List<ListenerContainer<? extends CyLDDTO>>> esperandoRefresco = new java.util.HashMap<Object, List<ListenerContainer<? extends CyLDDTO>>>();


    /***** CLASES PRIVADAS ******/

    // Clase privada Genérica que acepta un objeto que extiende del modelo CyLDDTO
    private static class ListenerContainer<T extends CyLDDTO> {
        public CyLDFormacionHandlerListener<T> listener;
        public String callId;
        public CollectionDataRetrieverBlock<T> retrieverBlock;
    };

    public static class Response<M> {
        public Exception error;
        public M data;
    }


    /***** MÉTODOS ******/

    /**
     * Elimina el actual callId de la lista de llamadas pendientes
     * @param callId
     * @return void
     */
    public static void cancelCall(String callId) {
        synchronized(CyLDHandler.class) { //trozo de código sicnronizado para ser accedido por un solo Thread al tiempo.
            llamadasPendientes.remove(callId);
        }
    }

    /**
     * Agrega el actual callId a la lista de llamadas pendientes.
     * @param callId Id de la llamada a añadir.
     * @return void
     */
    protected static void startCallId(String callId) {
        synchronized(CyLDHandler.class) {
            llamadasPendientes.add(callId);
        }
    }

    /**
     * Retrieves information executing background refresh if required
     * @param callId
     * @param expirationTime
     * @param retrieverBlock
     * @param saverBlock
     * @param listener
     */
    protected static <T extends CyLDDTO> void executeCollectionRetrieval(String callId, long expirationTime,
                                                                         String nombreTablaPrincipal, CollectionDataRetrieverBlock<T> retrieverBlock,
                                                                         CollectionDataSaverBlock<T> saverBlock,  CyLDFormacionHandlerListener<T> listener) {
        boolean requiereRefresco = false;
        try {
            //retrieverBlock se crea en CyLFromacionHandler, en el método listActivities. Se obtienen datos locales.
            Collection<T> data = retrieverBlock.localRetrieve();
            // Se comprueba si, para la tabla (Actividades), se necesita un refresco en bd.
            requiereRefresco = seRequiereRefresco(data, expirationTime, nombreTablaPrincipal);
            synchronized (CyLDHandler.class) {
                if (checkCall(callId)) {
                    listener.onResults(callId, data, requiereRefresco);

                }
            }
        } catch (Exception e) {
            synchronized (CyLDHandler.class) {
                if (checkCall(callId)) {
                    listener.onError(callId, e, requiereRefresco);
                }
            }
        }
        if (requiereRefresco) {
            // Realiza la petición de datos nuevos
            generalRefreshCollectionData(callId, retrieverBlock, saverBlock, listener);
        }
    }



    protected static boolean seRequiereRefresco(Collection<? extends CyLDDTO> dtos, long expirationTime, String nombreTablaPrincipal) {

        long oldestRefresh = Long.MAX_VALUE;

        if (dtos == null || dtos.size() == 0) {
            //Si no se pueden obtener modelos dtos,... buscar en la tabla general de refrescos.
            //oldestRefresh = App.getDataSource().getLastRefreshFor(nombreTablaPrincipal); //TODO: Refrescos
        } else {
            // Locate  the oldest center in the collection
            for (CyLDDTO dto: dtos) {
                if (dto.refreshedOn < oldestRefresh) {
                    oldestRefresh = dto.refreshedOn;
                }
            }
        }
        long now = System.currentTimeMillis();
        if (oldestRefresh > expirationTime) {
            return true;
        }
        return false;
    }


    /**
     * Comprueba si la llamada se ha realizado ya o no.
     * @param callId
     * @return
     */
    protected static boolean checkCall(String callId) {
        synchronized (CyLDHandler.class) {
            return llamadasPendientes.contains(callId);
        }
    }


    /**
     * Refreshes information from remote endpoint and calls the block after refreshing
     * @param block
     */
    protected static <T extends CyLDDTO> void generalRefreshCollectionData(String callId, CollectionDataRetrieverBlock<T> retrieverBlock,
                                                                           final CollectionDataSaverBlock<T> saverBlock, CyLDFormacionHandlerListener<T> listener) {

        final Object refreshToken = retrieverBlock.getRemoteRetrieveToken();
        final CollectionDataRetrieverBlock<T> commonRemoteRetrieverBlock = retrieverBlock;


        synchronized(esperandoRefresco) {

            List<ListenerContainer<? extends CyLDDTO>> listeners = esperandoRefresco.get(refreshToken);

            if (listeners == null) {
                listeners = new java.util.LinkedList<ListenerContainer<? extends CyLDDTO>>();
                esperandoRefresco.put(refreshToken, listeners);
            } else {
                System.out.println("Se ha detectado otra instancia esperando para refresco remoto: \"" + refreshToken + "\"");
                // Replace the listener with this one
                // Delete repeated callIds
                ListenerContainer<? extends CyLDDTO> toDelete = null;
                for (ListenerContainer<? extends CyLDDTO> l : listeners) {
                    if (l.callId.equals(callId)) {
                        toDelete = l;
                        break;
                    }
                }
                if (toDelete != null) {
                    listeners.remove(toDelete);
                }
            }
            ListenerContainer<T> lc = new ListenerContainer<T>();
            lc.listener = listener;
            lc.callId = callId;
            lc.retrieverBlock = retrieverBlock;
            listeners.add(lc);
            if (listeners.size() > 1) { // If there is already a refresh under progress just exit waiting the refresh
                return;
            }
        }


        AsyncTask<Void, Void, Response<Collection<T>>> refreshTask = new AsyncTask<Void, Void, Response<Collection<T>>>() {
            @Override
            protected Response<Collection<T>> doInBackground(Void... params) {
                Response<Collection<T>> resp = new Response<Collection<T>>();
                try {
                    resp.data = commonRemoteRetrieverBlock.remoteRetrieve();
                    saverBlock.storeData(resp.data);

                } catch (Exception e) {
                    System.out.println("Excepción refrescando actividades");
                    e.printStackTrace();
                    resp.error = e;
                }
                System.out.println("Finalizada la tarea de refresco del handler");
                return resp;
            }

            @Override
            protected void onPostExecute(Response<Collection<T>> centers) {
                System.out.println("Ejecución de Post...");
                synchronized (CyLDHandler.class) {

                    if (centers.error == null) { // Only execute the block after successful refresh
                        synchronized (esperandoRefresco) {

                            for (ListenerContainer<? extends CyLDDTO> block: esperandoRefresco.get(refreshToken)) {
                                ListenerContainer<T> lc = (ListenerContainer<T>) block;
                                if (!checkCall(lc.callId)) {
                                    continue;
                                }

                                Collection<T> data = lc.retrieverBlock.localRetrieve();

                                lc.listener.onResults(lc.callId, data, false);
                            }
                            esperandoRefresco.get(refreshToken).clear();
                        }
                    } else {
                        System.out.println("Sucedió un error"); // In this case do nothing. Unable to refresh data...will try next time the info is requested
                        centers.error.printStackTrace();
                        synchronized (esperandoRefresco) {
                            for (ListenerContainer<? extends CyLDDTO> block: esperandoRefresco.get(refreshToken)) {
                                ListenerContainer<T> lc = (ListenerContainer<T>) block;
                                if (llamadasPendientes.contains(block.callId)) {
                                    lc.listener.onError(lc.callId, centers.error, false);
                                }
                            }
                            esperandoRefresco.get(refreshToken).clear();
                        }
                    }
                }
            }

        };
        refreshTask.execute();
    }

    /********************************************************** INTERFACES **************************************************************************/

    /**
     * Interfaz interna genérica para la entrega de datos que acepta un objeto genérico que extiende de CyLDTO
     */
    protected static interface CollectionDataRetrieverBlock<M extends CyLDDTO> {
        /**
         * La implementación de este método obtendrá datos desde la base de datos de tipo CyLDDTO
         * @return
         * @throws SQLException
         */
        public Collection<M> localRetrieve() throws SQLException;

        /**
         * La implementación de este método obtendrá datos remotos para refrescar la base de datos local con información actualizada
         * @return
         * @throws CyLDWServiceException
         */
        public Collection<M> remoteRetrieve() throws CyLDWServiceException;

        /**
         * Implementation of this method must return an object that identifies uniquely the refresh call. If there is
         * a single call that retrieves all data remotely a unique string like "wholeWhatever" suffices (as long
         * as other handlers use different strings). But in the case of webservices that return only a subset of the information
         * the token object must be able to "equal" only those that retrieve the same data remotely. An example could be a
         * query that retrieves remotely cars from barcelona. The token must be something that contains the condition "from barcelona"
         * in order to compare it to other tokens and be only equal if the condition is also "from barcelona"
         * @return Token comparable
         */
        public Object getRemoteRetrieveToken();
    }

    /**
     * Interfaz interna genérica para el guardado de datos que acepta un objeto genérico que extiende de CyLDTO
     */
    protected static interface CollectionDataSaverBlock<M extends CyLDDTO> {

        public void storeData(Collection<M> data) throws SQLException;

    }
}
