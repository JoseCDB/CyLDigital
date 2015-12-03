package es.jcyl.cee.cylford.app.cyldigital.parser;

import android.text.Html;

import java.text.ParseException;

import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;

/**
 * Created by josecarlos.delbarrio on 24/11/2015.
 */
public class CyLDActivityParser extends CyLDGenericParser<CyLDFormacion>{

    private static final String ATTRVAL_IDENTIFIER = "Identificador";
    private static final String ATTRVAL_TITLE = "Titulo_es";
    private static final String ATTRVAL_LOCALITY = "Localidad_NombreLocalidad";
    private static final String ATTRVAL_STARTDATE = "FechaInicio";
    private static final String ATTRVAL_DURATION = "Duracion";
    private static final String ATTRVAL_DESCRIPTION = "Descripcion_es";
    private static final String ATTRVAL_FAMILY = "Materia_es";
    private static final String ATTRVAL_CONTENTSLINK = "Enlace al contenido";
    private static final String ATTRVAL_PROVINCEID = "Localidad_CodigoProvincia";
    private static final String ATTRVAL_EXECUTIONPLACE = "LugarCelebracion";
    private static final String ATTRVAL_REGISTRATIONMEAN = "Forma_inscripcion_es";


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

    private static final int CONTEXT_DURATION = pos++;
    private static final int CONTEXT_DESCRIPTION = pos++;
    private static final int CONTEXT_FAMILY = pos++;
    private static final int CONTEXT_CONTENTSLINK = pos++;
    private static final int CONTEXT_PROVINCEID = pos++;
    private static final int CONTEXT_EXECUTIONPLACE = pos++;
    private static final int CONTEXT_REGISTRATIONMEAN = pos++;


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


            || context == CONTEXT_IDENTIFIER
            || context == CONTEXT_STARTDATE
            || context == CONTEXT_LOCALITY
            || context == CONTEXT_PROVINCEID
            || context == CONTEXT_FAMILY
            || context == CONTEXT_CONTENTSLINK
            || context == CONTEXT_EXECUTIONPLACE ) {
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
        try {
            if (context == CONTEXT_IDENTIFICADOR) {
                dto.id = Integer.parseInt(value);
            } else if (context == CONTEXT_TITLE) {
                dto.title = value;
            } else if (context == CONTEXT_DESCRIPCION) {
                dto.description = Html.fromHtml(value).toString();
            } else if (context == CONTEXT_REGISTRATIONMEAN) {
                dto.registrationMean = Html.fromHtml(value).toString();
            } else if (context == CONTEXT_FAMILY) {
                dto.family = value;
            } else if (context == CONTEXT_STARTDATE) {
                if (value != null && value.length() > 0) {
                    dto.startDate = dtFmt.parse(value);
                }
            } else if (context == CONTEXT_LOCALITY) {
                dto.locality = value;
            } else if (context == CONTEXT_PROVINCEID) {
                dto.provinceId = value;
            } else if (context == CONTEXT_DURATION) {
                dto.duration = value;
            } else if (context == CONTEXT_CONTENTSLINK) {
                dto.contentsLink = value;
            } else if (context == CONTEXT_EXECUTIONPLACE) {
                if (dto.executionPlace != null) {
                    dto.executionPlace += "\n" + value;
                } else {
                    dto.executionPlace = value;
                }
            }
        } catch (ParseException pe) {
            System.err.println("Unable to set date " + value + " on context " + context);
        }
    }

    @Override
    public int getContextForAttribute(String attr) {
        if (attributeToContextMap.containsKey(attr)) {
            return attributeToContextMap.get(attr);
        }

        return 0;
    }
}
