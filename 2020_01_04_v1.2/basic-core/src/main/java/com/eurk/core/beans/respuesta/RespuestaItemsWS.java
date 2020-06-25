package com.eurk.core.beans.respuesta;

import java.util.List;

import com.eurk.core.beans.consulta.Consulta;

public final class RespuestaItemsWS extends RespuestaWS {

	private final List<?> items;
	private Consulta consulta;
	
	
	public RespuestaItemsWS( List<? extends Object> items) {
		super( CodigoRespuestaEnum.OPERACION_EXITOSA);
		this.items = items;
	}
	
	
	public static RespuestaItemsWS crear( List<? extends Object> items) {
		return new RespuestaItemsWS( items);
	}
	
	public List<?> getItems() {
		return this.items;
	}
	

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}
		
}