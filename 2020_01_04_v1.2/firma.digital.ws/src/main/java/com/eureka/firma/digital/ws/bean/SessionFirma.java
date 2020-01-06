package com.eureka.firma.digital.ws.bean;

import java.io.File;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;


public class SessionFirma implements Serializable {

	private static final long serialVersionUID = -6792187764695478951L;

	public SolicitudFirma solicitud;
	public String certificadoBase64;
	public String archivoLlaveBase64;
	
	public PrivateKey privateKey;
	public PublicKey  publicKey;
	public Firma firma;
	
	public File archivo;
	
	public SessionFirma( final SolicitudFirma solicitud) {
		super();
		this.solicitud = solicitud; 
		firma = new Firma();
	}

}

