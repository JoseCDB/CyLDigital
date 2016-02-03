package es.jcyl.cee.cylford.app.cyldigital.parser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import es.jcyl.cee.cylford.app.cyldigital.parser.dto.CyLDFormacion;

/**
 * Created by josecarlos.delbarrio on 23/11/2015.
 * El interface ContentHandler define los siguientes métodos, que se ejecutarán cada vez que ocurra un evento al procesar un documento XML
 */
public abstract class CyLDGenericParser<M> implements ContentHandler{

    // ATRIBUTOS
    private static final String NODE_ELEMENT = "actividades";
    //private static final String NODE_STRING = "string";
    private static final String ATTR_NAME = "name";
    private static final int CONTEXT_NOCONTEXT = 0;
    protected int xmlContext = CONTEXT_NOCONTEXT;
    private StringBuilder strBuilder = new StringBuilder();
    private M currDTO = null;
    private String appendableUnder = null; // string o text
    private boolean isUnderAppendableElement = false;
    private List<M> dtos = new java.util.LinkedList<M>();


    // MÉTODOS ABSTRACTOS
    public abstract M crearNuevoDTO();
    public abstract int getContextForAttribute(String attr);
    public abstract String getAppendableOnlyUnderElement(int context); //devuelve a "appendableUnder" tipo string o texto dependiendo del tipo de campo
    public abstract void setAttributeValue(int context, String value, M dto);


    @Override
    /**
     * Este método se ejecuta cuando empieza un elemento del documento XML. Los argumentos que recibe son la dirección URI del espacio de nombres asociado al elemento,
     * el nombre del elemento (o etiqueta) sin el prefijo del espacio de nombres, el nombre del elemento en la versión 1.0 de la especificación de XML,
     * y los atributos que contiene la etiqueta en forma de una instancia de la clase Attributes.
     */
    public void startElement(String uri, String localName, String qName,
                             Attributes atts) throws SAXException {
        if (NODE_ELEMENT.equals(localName)) {
            // Nueva actividad formativa
            currDTO = crearNuevoDTO();
            isUnderAppendableElement = false;
        } else if(localName.equals("nombre")
                || (localName.equals("descripcion"))
                || (localName.equals("fechaInicio"))
                || (localName.equals("fechaFin"))
                || (localName.equals("numeroHoras"))
                || (localName.equals("numeroPlazas"))
                || (localName.equals("numeroSolicitudes"))
                || (localName.equals("plazasEnListaEspera"))
                || (localName.equals("agrupacion"))
                || (localName.equals("requisitos"))
                || (localName.equals("aviso"))
                || (localName.equals("tematica"))
                || (localName.equals("nombreAgrupacion"))
                || (localName.equals("url"))
                || (localName.equals("tipo"))
                || (localName.equals("horaInicio"))
                || (localName.equals("horaFin"))
                || (localName.equals("fechaInicioMatriculacion"))
                || (localName.equals("fechaFinMatriculacion"))
                || (localName.equals("centro"))
                || (localName.equals("nivel")) ){
            /*
            *
            *         } else if(localName.equals("nombre")) {
            //((CyLDFormacion) currDTO).setNombre();
        } else if(localName.equals("descripcion")) {
        } else if(localName.equals("fechaInicio")) {
        } else if(localName.equals("fechaFin")) {
        } else if(localName.equals("numeroHoras")) {
        } else if(localName.equals("numeroPlazas")) {
        } else if(localName.equals("numeroSolicitudes")) {
        } else if(localName.equals("plazasEnListaEspera")) {
        } else if(localName.equals("agrupacion")) {
        } else if(localName.equals("requisitos")) {
        } else if(localName.equals("aviso")) {
        } else if(localName.equals("tematica")) {
        } else if(localName.equals("nombreAgrupacion")) {
        } else if(localName.equals("url")) {
        } else if(localName.equals("tipo")) {
        } else if(localName.equals("horaInicio")) {
        } else if(localName.equals("horaFin")) {
        } else if(localName.equals("fechaInicioMatriculacion")) {
        } else if(localName.equals("fechaFinMatriculacion")) {
        } else if(localName.equals("centro")) {
        } else if(localName.equals("nivel")) {*/
            // New attribute, find out which one
            String name = atts.getValue(ATTR_NAME);
            strBuilder = new StringBuilder();
            //this.xmlContext = CONTEXT_NOCONTEXT;
            this.xmlContext = getContextForAttribute(localName);//le asignamos el valor numérico por nombre del atributo
            this.appendableUnder = getAppendableOnlyUnderElement(this.xmlContext); //string o text
        }
    }

    @Override
    /**
     * Se produce al teminar un elemento, Añadiendosele el valor del strBuilder al campo del dto. El significado de los atributos es el mismo que en startElement
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (localName.equals(NODE_ELEMENT)) {
            this.dtos.add(this.currDTO);
            this.currDTO = null;
            this.xmlContext = CONTEXT_NOCONTEXT;
        } else if (localName.equals("nombre") ||
                localName.equals("descripcion") ||
                localName.equals("fechaInicio") ||
                localName.equals("fechaFin") ||
                localName.equals("numeroHoras") ||
                localName.equals("numeroPlazas") ||
                localName.equals("numeroSolicitudes") ||
                localName.equals("plazasEnListaEspera") ||
                localName.equals("agrupacion") ||
                localName.equals("requisitos") ||
                localName.equals("aviso") ||
                localName.equals("tematica") ||
                localName.equals("nombreAgrupacion") ||
                localName.equals("url") ||
                localName.equals("tipo") ||
                localName.equals("horaInicio") ||
                localName.equals("horaFin") ||
                localName.equals("fechaInicioMatriculacion") ||
                localName.equals("fechaFinMatriculacion") ||
                localName.equals("centro") ||
                localName.equals("nivel")) {
            setAttributeValue(this.xmlContext, strBuilder.toString().replaceAll("\\n", ""), this.currDTO);
            this.xmlContext = CONTEXT_NOCONTEXT;
        } else if (localName.equals(appendableUnder)) { // End of string (in case of array addd value
            setAttributeValue(this.xmlContext, strBuilder.toString(), this.currDTO);
            strBuilder = new StringBuilder();
        }
    }


    public Collection<M> parse(InputStream is) throws CyLDParserException {

        SAXParserFactory factory=SAXParserFactory.newInstance();

        try {
            SAXParser sp= factory.newSAXParser();
            XMLReader reader=sp.getXMLReader();
            reader.setContentHandler(this);
            InputSource iSrc = new InputSource(is);
            iSrc.setEncoding("UTF-8");
            reader.parse(iSrc);

        } catch (SAXException saxpe) {
            System.err.println("Capturada SAXException");
            saxpe.printStackTrace();
            throw new CyLDParserException("SAXException encontrada");
        } catch (ParserConfigurationException pce) {
            System.err.println("Capturada ParserConfigurationException");
            pce.printStackTrace();
            throw new CyLDParserException("ParserConfigurationException encontrada");

        } catch (IOException ioe) {
            System.err.println("Capturada IOException");
            ioe.printStackTrace();
            throw new CyLDParserException("IOException encontrada");
        }
        Collection<M> lst = this.dtos;
        this.dtos = null;
        this.currDTO = null;
        this.xmlContext = CONTEXT_NOCONTEXT;
        return lst;
    }


    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String string = new String(ch, start, length);
        if (this.xmlContext != CONTEXT_NOCONTEXT ) {
            // if (this.xmlContext != CONTEXT_NOCONTEXT && (isUnderAppendableElement || getAppendableOnlyUnderElement(this.xmlContext) == null)) {
            //Solo se agrega al string builder el valor para el campo si es un nodo string
            strBuilder.append(string);
        }
    }

    @Override
    /**
     * Este método indica los espacios en blanco que pueden ser ignorados en el documento,
     * pero normalmente solo se utiliza cuando se valida el documento durante el procesamiento del fichero.
     * Los parametros tienen el mismo significado que en el método anterior, characters.
     */
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * Este evento se produce cuando se encuentra una instrucción de proceso de XML (llamadas también PI) distinta al a declaración de decomento XML,
     * como por ejemplo<? Cocoon process type = "xslt" ?>.
     * No entraré a explicar en detalle lo que es una PI, ya que esto no es tutorial de XML, simplemente diré que es una especie de instrucción para ciertos programas que procesas XML.
     * Los argumentos de este método son el destino de la PI y los atributos de dicha instrucción.
     */
    public void processingInstruction(String target, String data)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * Este método recive una instancia de la clase Locator que contendrá la información referente a la posición del documento donde sucede un evento.
     */
    public void setDocumentLocator(Locator locator) {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * Este método indica que una entidad externa se ha ignorado al procesar el fichero (algo que n pasará en un parser estable como es el Xerces).
     * Recibe como parametros el nombre de dicha entidad.
     */
    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * El evento que se produce al comenzar a procesar un documetno XML.
     */
    public void startDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * Fin del documento XML.
     */
    public void endDocument() throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * Indica el comienzo de un prefijo de un espacio de nombre (Namespace), cuya función también se escapa de los objetivos de este articulo,
     * aunque se puede definir como una forma de identificar inequivocamente los elementos del documento.
     * Los parametros de éste método son el prefijo del espacio de nombres y su dirección URI asociada.
     */
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        // TODO Auto-generated method stub

    }

    @Override
    /**
     * Indica cuando se ha terminado la petición de inicio del prefijo del espacio de nombres.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        // TODO Auto-generated method stub

    }

}
