package com.meve.firma.digital.ws.service;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.meve.firma.digital.ws.bean.RespuestaFirma;
import com.meve.firma.digital.ws.bean.SolicitudFirma;

@WebService
public interface IFirmaElectronica {

	RespuestaFirma firmarArchivo( @WebParam( name="solicitudFirma") SolicitudFirma solicitudFirma);
	
}
