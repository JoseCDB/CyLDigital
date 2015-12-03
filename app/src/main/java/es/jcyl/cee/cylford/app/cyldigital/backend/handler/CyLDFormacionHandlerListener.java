package es.jcyl.cee.cylford.app.cyldigital.backend.handler;

import java.util.Collection;

/**
 * Created by josecarlos.delbarrio on 23/10/2015.
 */
public interface CyLDFormacionHandlerListener<M> {
    //OnlineActvity debe implementar estos métodos
    public void onResults(String callId, Collection<M> res, boolean expectRefreshCall);
    public void onError(String callId, Exception e, boolean expectRefreshCall);
}
