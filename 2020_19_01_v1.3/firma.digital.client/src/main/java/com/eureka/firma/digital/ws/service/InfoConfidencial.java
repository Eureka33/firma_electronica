
package com.eureka.firma.digital.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para infoConfidencial complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="infoConfidencial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="archivoLlave" type="{http://service.ws.digital.firma.eureka.com/}infoArchivo" minOccurs="0"/>
 *         &lt;element name="certificado" type="{http://service.ws.digital.firma.eureka.com/}infoArchivo" minOccurs="0"/>
 *         &lt;element name="passwordLlave" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infoConfidencial", propOrder = {
    "archivoLlave",
    "certificado",
    "passwordLlave",
    "rfc"
})
public class InfoConfidencial {

    protected InfoArchivo archivoLlave;
    protected InfoArchivo certificado;
    protected String passwordLlave;
    protected String rfc;

    /**
     * Obtiene el valor de la propiedad archivoLlave.
     * 
     * @return
     *     possible object is
     *     {@link InfoArchivo }
     *     
     */
    public InfoArchivo getArchivoLlave() {
        return archivoLlave;
    }

    /**
     * Define el valor de la propiedad archivoLlave.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoArchivo }
     *     
     */
    public void setArchivoLlave(InfoArchivo value) {
        this.archivoLlave = value;
    }

    /**
     * Obtiene el valor de la propiedad certificado.
     * 
     * @return
     *     possible object is
     *     {@link InfoArchivo }
     *     
     */
    public InfoArchivo getCertificado() {
        return certificado;
    }

    /**
     * Define el valor de la propiedad certificado.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoArchivo }
     *     
     */
    public void setCertificado(InfoArchivo value) {
        this.certificado = value;
    }

    /**
     * Obtiene el valor de la propiedad passwordLlave.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordLlave() {
        return passwordLlave;
    }

    /**
     * Define el valor de la propiedad passwordLlave.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordLlave(String value) {
        this.passwordLlave = value;
    }

    /**
     * Obtiene el valor de la propiedad rfc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * Define el valor de la propiedad rfc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfc(String value) {
        this.rfc = value;
    }

}
