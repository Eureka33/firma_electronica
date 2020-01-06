package com.eureka.firma.digital.ws.bean;

import java.io.Serializable;

public class InfoConfidencial implements Serializable {
	
	private static final long serialVersionUID = 6713684719961821612L;
	
	private String passwordLlave;
	private String rfc;
	
	private InfoArchivo archivoLlave;
	private InfoArchivo certificado;
	
	
	public InfoConfidencial() {
		super();
	}


	public String getPasswordLlave() {
		return passwordLlave;
	}
	public void setPasswordLlave(String passwordLlave) {
		this.passwordLlave = passwordLlave;
	}

	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public InfoArchivo getArchivoLlave() {
		return archivoLlave;
	}
	public void setArchivoLlave(InfoArchivo archivoLlave) {
		this.archivoLlave = archivoLlave;
	}

	public InfoArchivo getCertificado() {
		return certificado;
	}
	public void setCertificado(InfoArchivo certificado) {
		this.certificado = certificado;
	} 
	
}
