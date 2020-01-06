
package com.eureka.firma.digital.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para firmarArchivo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="firmarArchivo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="solicitudFirma" type="{http://service.ws.digital.firma.eureka.com/}solicitudFirma" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firmarArchivo", propOrder = {
    "solicitudFirma"
})
public class FirmarArchivo {

    protected SolicitudFirma solicitudFirma;

    /**
     * Obtiene el valor de la propiedad solicitudFirma.
     * 
     * @return
     *     possible object is
     *     {@link SolicitudFirma }
     *     
     */
    public SolicitudFirma getSolicitudFirma() {
        return solicitudFirma;
    }

    /**
     * Define el valor de la propiedad solicitudFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link SolicitudFirma }
     *     
     */
    public void setSolicitudFirma(SolicitudFirma value) {
        this.solicitudFirma = value;
    }

}
