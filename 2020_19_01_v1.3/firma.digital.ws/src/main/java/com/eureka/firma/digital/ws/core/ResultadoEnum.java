package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Resultado;

public enum ResultadoEnum {

	OK( 0, "Operación exitosa" ),
	ERROR_SAVING_FILE( 		1, "Error al recuperar información de solicitud"),
	INVOCATION_ERROR(  		2, "Error de invocación. Información incompleta"),
	ERROR_VALIDACION(  		3, "Error al realizar operación de validación"),
	ERROR_FIRMADO(     		4, "Error al realizar la operacion de firmado"),
	ERROR_INTERNAL_CONFIG( 	5, "Error de configuración interno, notifique a su administrador"),
	ERROR_DESCONOCIDO( 		9, "Situación inesperada");
	
	
	private Resultado<Void> resultado;
	
	
	private ResultadoEnum( Integer codigo, String mensaje) {
		resultado = new Resultado<Void>();
		
		resultado.setCodigo( 	codigo);
		resultado.setMensaje( 	mensaje);	
	}
	
	
	public <T> Resultado<T> getResultado( String descripcion) {
		Resultado<T> bean = new Resultado<T>();
		
		bean.setCodigo( resultado.getCodigo());
		bean.setMensaje(resultado.getMensaje() + ": " + descripcion);
		
		return bean;
	}
	
}
