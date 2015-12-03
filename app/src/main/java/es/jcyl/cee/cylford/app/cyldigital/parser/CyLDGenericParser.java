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

/**
 * Created by josecarlos.delbarrio on 23/11/2015.
 * El interface ContentHandler define los siguientes métodos, que se ejecutarán cada vez que ocurra un evento al procesar un documento XML
 */
public abstract class CyLDGenericParser<M> implements ContentHandler{

    // ATRIBUTOS
    private static final String NODE_ELEMENT = "element";
    private static final String NODE_ATTRIBUTE = "attribute";
    //private static final String NODE_STRING = "string";
    private static final String ATTR_NAME = "name";
    private static final int CONTEXT_NOCONTEXT = 0;
    protected int xmlContext = CONTEXT_NOCONTEXT;
    private StringBuilder strBuilder = new StringBuilder();
    private M currDTO = null;
    private String appendableUnder = null;
    private boolean isUnderAppendableElement = false;
    private List<M> dtos = new java.util.LinkedList<M>();


    // MÉTODOS ABSTRACTOS
    public abstract M crearNuevoDTO();
    public abstract int getContextForAttribute(String attr);
    public abstract String getAppendableOnlyUnderElement(int context);
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
            // Entering a new element
            this.currDTO = crearNuevoDTO();


        } else if (NODE_ATTRIBUTE.equals(localName)) {
            // New attribute, find out which one
            String name = atts.getValue(ATTR_NAME);

            this.xmlContext = CONTEXT_NOCONTEXT;

            this.xmlContext = getContextForAttribute(name);
            strBuilder = new StringBuilder();
            this.appendableUnder = getAppendableOnlyUnderElement(this.xmlContext);

        } else if (localName.equals(this.appendableUnder)) {
            isUnderAppendableElement = true;
        }
    }

    @Override
    /**
     * Se produce al teminar un elemento. El sgnificado de los atributos es el mismo que en startElement
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (localName.equals(NODE_ELEMENT)) {
            this.dtos.add(this.currDTO);
            this.currDTO = null;
            this.xmlContext = CONTEXT_NOCONTEXT;
        } else if (NODE_ATTRIBUTE.equals(localName) && appendableUnder == null) {
            setAttributeValue(this.xmlContext, strBuilder.toString().replaceAll("\\n", ""), this.currDTO);
            this.xmlContext = CONTEXT_NOCONTEXT;

        } else if (localName.equals(appendableUnder)) { // End of string (in case of array addd value
            setAttributeValue(this.xmlContext, strBuilder.toString(), this.currDTO);
            strBuilder = new StringBuilder();
            isUnderAppendableElement = false;
        }
    }


    public Collection<M> parse(InputStream is) throws CyLDParserException {

        SAXParserFactory factory=SAXParserFactory.newInstance();

        try {
            SAXParser sp= factory.newSAXParser();
            XMLReader reader=sp.getXMLReader();
            reader.setContentHandler(this);
            InputSource iSrc = new InputSource(is);
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
        if (this.xmlContext != CONTEXT_NOCONTEXT && (isUnderAppendableElement || getAppendableOnlyUnderElement(this.xmlContext) == null)) {
            // Only append to string builder if we are under string node or there is no need of a string node
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
