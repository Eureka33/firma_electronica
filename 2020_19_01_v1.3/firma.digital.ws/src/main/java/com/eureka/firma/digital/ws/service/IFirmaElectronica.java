package com.eureka.firma.digital.ws.service;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;

@WebService
public interface IFirmaElectronica {

	RespuestaFirma firmarArchivo( @WebParam( name="solicitudFirma") SolicitudFirma solicitudFirma);
	
}
