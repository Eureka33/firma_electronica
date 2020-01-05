
package com.meve.firma.digital.ws.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.meve.firma.digital.ws.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FirmarArchivo_QNAME = new QName("http://service.ws.digital.firma.meve.com/", "firmarArchivo");
    private final static QName _FirmarArchivoResponse_QNAME = new QName("http://service.ws.digital.firma.meve.com/", "firmarArchivoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.meve.firma.digital.ws.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FirmarArchivo }
     * 
     */
    public FirmarArchivo createFirmarArchivo() {
        return new FirmarArchivo();
    }

    /**
     * Create an instance of {@link FirmarArchivoResponse }
     * 
     */
    public FirmarArchivoResponse createFirmarArchivoResponse() {
        return new FirmarArchivoResponse();
    }

    /**
     * Create an instance of {@link RespuestaFirma }
     * 
     */
    public RespuestaFirma createRespuestaFirma() {
        return new RespuestaFirma();
    }

    /**
     * Create an instance of {@link InfoConfidencial }
     * 
     */
    public InfoConfidencial createInfoConfidencial() {
        return new InfoConfidencial();
    }

    /**
     * Create an instance of {@link InfoArchivo }
     * 
     */
    public InfoArchivo createInfoArchivo() {
        return new InfoArchivo();
    }

    /**
     * Create an instance of {@link SolicitudFirma }
     * 
     */
    public SolicitudFirma createSolicitudFirma() {
        return new SolicitudFirma();
    }

    /**
     * Create an instance of {@link Firma }
     * 
     */
    public Firma createFirma() {
        return new Firma();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarArchivo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.digital.firma.meve.com/", name = "firmarArchivo")
    public JAXBElement<FirmarArchivo> createFirmarArchivo(FirmarArchivo value) {
        return new JAXBElement<FirmarArchivo>(_FirmarArchivo_QNAME, FirmarArchivo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarArchivoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ws.digital.firma.meve.com/", name = "firmarArchivoResponse")
    public JAXBElement<FirmarArchivoResponse> createFirmarArchivoResponse(FirmarArchivoResponse value) {
        return new JAXBElement<FirmarArchivoResponse>(_FirmarArchivoResponse_QNAME, FirmarArchivoResponse.class, null, value);
    }

}
