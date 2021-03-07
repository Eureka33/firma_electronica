package com.eureka.firma.digital.ws.service;

import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.eureka.firma.digital.ws.core.FirmaElectronicaBsnsComponent;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  Este comando genera las clases cliente necesarias:
 *  
 *  >> c:\software\apache-cxf-2.7.6\bin\wsdl2java -client -verbose -frontend jaxws21 http://fiel.asa.gob.mx/digitalSignWSSS/servicios/FirmaElectronica?wsdl
 *  
 *  - Cambiar byte[] por DataHandler donde corresponda.
 *
 */
@WebService(
	endpointInterface = "com.eureka.firma.digital.ws.service.IFirmaElectronica",
	serviceName 	  = "FirmaElectronicaService"
)
public class FirmaElectronicaServiceImpl implements IFirmaElectronica {

	@Autowired private FirmaElectronicaBsnsComponent component;
	
	
	/**
	 * 	Firma el archivo recibido como argumento y devuelve el archivo firmado
	 */
    @Override
	public RespuestaFirma firmarArchivo( final SolicitudFirma solicitud) {
        
        return component.firmarArchivo( solicitud);      
	}

}
