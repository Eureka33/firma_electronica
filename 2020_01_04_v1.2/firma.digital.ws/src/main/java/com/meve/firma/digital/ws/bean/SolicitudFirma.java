package com.meve.firma.digital.ws.bean;

import java.io.Serializable;

public class SolicitudFirma implements Serializable {

	private static final long serialVersionUID = 4442664398111922929L;

	private InfoConfidencial infoConfidencial;
	private InfoArchivo archivoDatos;
	private String cadena;
	
	public SolicitudFirma() {
		super();
	}


	public InfoConfidencial getInfoConfidencial() {
		return infoConfidencial;
	}
	public void setInfoConfidencial(final InfoConfidencial infoConfidencial) {
		this.infoConfidencial = infoConfidencial;
	}
	
	public InfoArchivo getArchivoDatos() {
		return archivoDatos;
	}
	public void setArchivoDatos(	final InfoArchivo archivoDatos) {
		this.archivoDatos = archivoDatos;
	}

	public String getCadena() {
		return cadena;
	}
	public void setCadena( 			final String cadena) {
		this.cadena = cadena;
	}
	
}