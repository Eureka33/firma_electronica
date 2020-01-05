package com.meve.ofsp.certificado;

/**
 * 	Define los tipos de certificados digitales conocidos.
 *  
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public enum TipoCertificadoEnum {
	
	CSD(				"csd"),		// certficado de sello digital 
	FIEL(				"fiel"), 	// certificado de firma electronica avanzada
	FUNCION_PUBLICA(	"fp"); 		// certificado para interoperabilidad
	
	String identificador;
	
	private TipoCertificadoEnum( String identificador) {
		this.identificador = identificador;
	}

	public String getIdentificador() {
		return identificador;
	}
}