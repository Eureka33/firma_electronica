package com.eureka.firma.digital.ws.bean;

public class RespuestaSolicitud extends RespuestaFirma {

	private Solicitud solicitud;
	
	
	public RespuestaSolicitud() {
		super();
	}

    
    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
	
}