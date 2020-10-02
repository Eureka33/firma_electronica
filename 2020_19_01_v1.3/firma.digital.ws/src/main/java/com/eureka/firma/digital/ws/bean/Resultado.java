package com.eureka.firma.digital.ws.bean;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

public class Resultado<T> implements Serializable {

	private static final long serialVersionUID = 4535118053751061130L;

	private Integer codigo;
	private String mensaje;
	private T resultado;
		
	
	public Resultado() {
		super();
	}


	public Integer getCodigo() {	
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public T getResultado() {
		return resultado;
	}
	public void setResultado( T resultado) {
		this.resultado = resultado;
	}

	
	/**
	 *  Cualquier codigo de error mayor a cero se considera erroneo
	 */
	public boolean isError() {
		return codigo > 0;
	}
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}

}