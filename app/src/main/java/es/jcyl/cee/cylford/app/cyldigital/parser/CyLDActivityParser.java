package es.jcyl.cee.cylford.app.cyldigital.parser;

import android.text.Html;

import java.text.ParseException;
import java.util.Map;

import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;

/**
 * Created by josecarlos.delbarrio on 24/11/2015.
 */
public class CyLDActivityParser extends CyLDGenericParser<CyLDFormacion>{

    private static final String ATTRVAL_IDENTIFIER = "Identificador";
    private static final String ATTRVAL_PROVINCEID = "Localidad_CodigoProvincia";
    private static final String ATTRVAL_NAME = "Nombre_es";
    private static final String ATTRVAL_DESCRIPTION = "Descripcion_es";
    private static final String ATTRVAL_STARTDATE = "FechaInicio";
    private static final String ATTRVAL_ENDDATE = "FechaFin";
    private static final String ATTRVAL_NUMHOR = "NumeroHoras";
    private static final String ATTRVAL_NUMPLA = "NumeroPlazas";
    private static final String ATTRVAL_REQUESTS = "NumeroSolicitudes";
    private static final String ATTRVAL_NUMPLAWLIST = "NumeroPlazasListaEsp";
    private static final String ATTRVAL_REQUIREMENTS = "Requisitos_es";
    private static final String ATTRVAL_WARN = "Aviso_es";
    private static final String ATTRVAL_THEME = "Tematica_es";
    private static final String ATTRVAL_CLUSTER = "Agrupada";
    private static final String ATTRVAL_CLUSTERNAME = "NombreAgrupacion_es";
    private static final String ATTRVAL_URL = "Url";
    private static final String ATTRVAL_ACTYPE = "TipoActividad_es";
    private static final String ATTRVAL_STARTHOUR = "HoraInicio";
    private static final String ATTRVAL_ENDHOUR = "HoraFin";
    private static final String ATTRVAL_ENROLLSTARTDATE = "FechaInicioMatricula";
    private static final String ATTRVAL_ENROLLENDDATE = "FechaFinMatricula";
    private static final String ATTRVAL_EXECUTIONPLACE = "Centro";
    private static final String ATTRVAL_LEVEL = "Nivel_es";



    private static int pos = 1;
    private static final int CONTEXT_IDENTIFICADOR = pos++;
    private static final int CONTEXT_PROVINCIA = pos++;
    private static final int CONTEXT_NOMBRE = pos++;
    private static final int CONTEXT_DESCRIPCION = pos++;
    private static final int CONTEXT_FECHAINICIO= pos++;
    private static final int CONTEXT_FECHAFIN= pos++;
    private static final int CONTEXT_NUMHORAS = pos++;
    private static final int CONTEXT_NUMPLAZAS = pos++;
    private static final int CONTEXT_NUMSOLIC = pos++;
    private static final int CONTEXT_PLAZASESP = pos++;
    private static final int CONTEXT_REQUISITOS = pos++;
    private static final int CONTEXT_AVISO = pos++;
    private static final int CONTEXT_TEMATICA = pos++;
    private static final int CONTEXT_AGRUPADA = pos++;
    private static final int CONTEXT_NOMAGR = pos++;
    private static final int CONTEXT_URL = pos++;
    private static final int CONTEXT_TIPACT = pos++;
    private static final int CONTEXT_HORAINI = pos++;
    private static final int CONTEXT_HORAFIN = pos++;
    private static final int CONTEXT_FECINIMAT = pos++;
    private static final int CONTEXT_FECFINMAT = pos++;
    private static final int CONTEXT_CENTRO = pos++;
    private static final int CONTEXT_NIVEL = pos++;


    private static Map<String, Integer> attributeToContextMap = new java.util.HashMap<String, Integer>();
    static {
        attributeToContextMap.put(ATTRVAL_IDENTIFIER, CONTEXT_IDENTIFICADOR);
        attributeToContextMap.put(ATTRVAL_PROVINCEID, CONTEXT_PROVINCIA);
        attributeToContextMap.put(ATTRVAL_NAME, CONTEXT_NOMBRE);
        attributeToContextMap.put(ATTRVAL_DESCRIPTION, CONTEXT_DESCRIPCION);
        attributeToContextMap.put(ATTRVAL_STARTDATE, CONTEXT_FECHAINICIO);
        attributeToContextMap.put(ATTRVAL_ENDDATE, CONTEXT_FECHAFIN);
        attributeToContextMap.put(ATTRVAL_NUMHOR, CONTEXT_NUMHORAS);
        attributeToContextMap.put(ATTRVAL_NUMPLA, CONTEXT_NUMPLAZAS);
        attributeToContextMap.put(ATTRVAL_REQUESTS, CONTEXT_NUMSOLIC);
        attributeToContextMap.put(ATTRVAL_NUMPLAWLIST, CONTEXT_PLAZASESP);
        attributeToContextMap.put(ATTRVAL_REQUIREMENTS, CONTEXT_REQUISITOS);
        attributeToContextMap.put(ATTRVAL_WARN, CONTEXT_AVISO);
        attributeToContextMap.put(ATTRVAL_THEME, CONTEXT_TEMATICA);
        attributeToContextMap.put(ATTRVAL_CLUSTER, CONTEXT_AGRUPADA);
        attributeToContextMap.put(ATTRVAL_CLUSTERNAME, CONTEXT_NOMAGR);
        attributeToContextMap.put(ATTRVAL_URL, CONTEXT_URL);
        attributeToContextMap.put(ATTRVAL_ACTYPE, CONTEXT_TIPACT);
        attributeToContextMap.put(ATTRVAL_STARTHOUR, CONTEXT_HORAINI);
        attributeToContextMap.put(ATTRVAL_ENDHOUR, CONTEXT_HORAFIN);
        attributeToContextMap.put(ATTRVAL_ENROLLSTARTDATE, CONTEXT_FECINIMAT);
        attributeToContextMap.put(ATTRVAL_ENROLLENDDATE, CONTEXT_FECFINMAT);
        attributeToContextMap.put(ATTRVAL_EXECUTIONPLACE, CONTEXT_CENTRO);
        attributeToContextMap.put(ATTRVAL_LEVEL, CONTEXT_NIVEL);
    }

    @Override
    public String getAppendableOnlyUnderElement(int context) {
        if ( context == CONTEXT_IDENTIFICADOR
                || context == CONTEXT_PROVINCIA
                || context == CONTEXT_NOMBRE
                || context == CONTEXT_FECHAINICIO
                || context == CONTEXT_FECHAFIN
                || context == CONTEXT_NUMHORAS
                || context == CONTEXT_NUMPLAZAS
                || context == CONTEXT_NUMSOLIC
                || context == CONTEXT_PLAZASESP
                || context == CONTEXT_REQUISITOS
                || context == CONTEXT_AVISO
                || context == CONTEXT_TEMATICA
                || context == CONTEXT_AGRUPADA
                || context == CONTEXT_NOMAGR
                || context == CONTEXT_URL
                || context == CONTEXT_TIPACT
                || context == CONTEXT_HORAINI
                || context == CONTEXT_HORAFIN
                || context == CONTEXT_FECINIMAT
                || context == CONTEXT_FECFINMAT
                || context == CONTEXT_CENTRO
                || context == CONTEXT_NIVEL ) {
            return "string";
        } else if (context == CONTEXT_DESCRIPCION || context == CONTEXT_DESCRIPCION)  {
            return "text";
        } else {
            return null;
        }
    }

    @Override
    public CyLDFormacion crearNuevoDTO() {
        return new CyLDFormacion();
    }


    @Override
    public void setAttributeValue(int context, String value, CyLDFormacion dto) {
        //try {
            if (context == CONTEXT_IDENTIFICADOR) {
                dto.id = Integer.parseInt(value);
            } else if (context == CONTEXT_PROVINCIA) {
                dto.provincia = value;
            } else if (context == CONTEXT_NOMBRE) {
                dto.nombre = value;
            } else if (context == CONTEXT_FECHAINICIO) {
                dto.fechaInicio = value;
            } else if (context == CONTEXT_FECHAFIN) {
                dto.fechaFin = value;
            } else if (context == CONTEXT_NUMHORAS) {
                dto.numeroHoras = Float.parseFloat(value);
            } else if (context == CONTEXT_NUMPLAZAS) {
                dto.numeroPlazas = Integer.parseInt(value);
            } else if (context == CONTEXT_NUMSOLIC) {
                dto.numeroSolicitudes = Integer.parseInt(value);
            } else if (context == CONTEXT_PLAZASESP) {
                dto.plazasEnListaEspera = Integer.parseInt(value);
            } else if (context == CONTEXT_REQUISITOS) {
                dto.requisitos = value;
            } else if (context == CONTEXT_AVISO) {
                dto.aviso = value;
            } else if (context == CONTEXT_TEMATICA) {
                dto.tematica = value;
            } else if (context == CONTEXT_AGRUPADA) {
                dto.agrupacion = Integer.parseInt(value);
            } else if (context == CONTEXT_NOMAGR) {
                dto.nombreAgrupacion = value;
            } else if (context == CONTEXT_URL) {
                dto.url = value;
            } else if (context == CONTEXT_TIPACT) {
                dto.tipo = value;
            } else if (context == CONTEXT_HORAINI) {
                dto.horaInicio = value;
            } else if (context == CONTEXT_HORAFIN) {
                dto.horaFin = value;
            } else if (context == CONTEXT_FECINIMAT) {
                dto.fechaInicioMatriculacion = value;
            } else if (context == CONTEXT_FECFINMAT) {
                dto.fechaFinMatriculacion = value;
            } else if (context == CONTEXT_CENTRO) {
                dto.centro = value;
            } else if (context == CONTEXT_NIVEL) {
                dto.nivel = value;
            }
        //} catch (ParseException pe) {//mod
          //  System.err.println("Unable to set date " + value + " on context " + context);
        //}
    }

    @Override
    public int getContextForAttribute(String attr) {
        if (attributeToContextMap.containsKey(attr)) {
            return attributeToContextMap.get(attr);
        }

        return 0;
    }
}
