package com.eurk.core.beans.respuesta;

/**
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public enum CodigoRespuestaEnum {
	OPERACION_EXITOSA(		200, "operacion.exitosa"),	
	ERROR_NEGOCIO(			300, "error.negocio"),
	ERROR_PETICION(			400, "error.peticion"),
	ERROR_INFRAESTRUCTURA(	500, "error.infraestructura"),
	ERROR_DESCONOCIDO(		600, "error.desconocido");
	
	private int codigo;
	private String clave;
	
	private CodigoRespuestaEnum( int codigo, String clave) {
		this.codigo = codigo;
		this.clave  = clave;
	}
	
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getClave() {
		return clave;
	}
	
}
