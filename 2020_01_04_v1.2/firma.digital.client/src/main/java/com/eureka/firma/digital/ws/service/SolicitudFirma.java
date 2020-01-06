
package com.eureka.firma.digital.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para solicitudFirma complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="solicitudFirma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="archivoDatos" type="{http://service.ws.digital.firma.eureka.com/}infoArchivo" minOccurs="0"/>
 *         &lt;element name="cadena" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="infoConfidencial" type="{http://service.ws.digital.firma.eureka.com/}infoConfidencial" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "solicitudFirma", propOrder = {
    "archivoDatos",
    "cadena",
    "infoConfidencial"
})
public class SolicitudFirma {

    protected InfoArchivo archivoDatos;
    protected String cadena;
    protected InfoConfidencial infoConfidencial;

    /**
     * Obtiene el valor de la propiedad archivoDatos.
     * 
     * @return
     *     possible object is
     *     {@link InfoArchivo }
     *     
     */
    public InfoArchivo getArchivoDatos() {
        return archivoDatos;
    }

    /**
     * Define el valor de la propiedad archivoDatos.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoArchivo }
     *     
     */
    public void setArchivoDatos(InfoArchivo value) {
        this.archivoDatos = value;
    }

    /**
     * Obtiene el valor de la propiedad cadena.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadena() {
        return cadena;
    }

    /**
     * Define el valor de la propiedad cadena.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadena(String value) {
        this.cadena = value;
    }

    /**
     * Obtiene el valor de la propiedad infoConfidencial.
     * 
     * @return
     *     possible object is
     *     {@link InfoConfidencial }
     *     
     */
    public InfoConfidencial getInfoConfidencial() {
        return infoConfidencial;
    }

    /**
     * Define el valor de la propiedad infoConfidencial.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoConfidencial }
     *     
     */
    public void setInfoConfidencial(InfoConfidencial value) {
        this.infoConfidencial = value;
    }

}
