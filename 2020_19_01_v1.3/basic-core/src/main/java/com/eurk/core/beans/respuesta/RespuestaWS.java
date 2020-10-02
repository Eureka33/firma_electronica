package com.eurk.core.beans.respuesta;

import static com.eurk.core.beans.respuesta.CodigoRespuestaEnum.*;
import java.util.List;
import mx.com.neogen.commons.exception.InfraestructuraException;
import mx.com.neogen.commons.exception.NegocioException;
import mx.com.neogen.commons.exception.PeticionIncorrectaException;
import mx.com.neogen.commons.util.UtilReflection;

public class RespuestaWS {
	
	private Integer	   codigo;
	private String		clave;
	private ListaMensajes mensajes;
	
	
	public RespuestaWS( CodigoRespuestaEnum tipo) {
		this( tipo, new ListaMensajes());
	}
	
	public RespuestaWS( CodigoRespuestaEnum tipo, ListaMensajes mensajes) {
		super();
		
		this.codigo   = tipo.getCodigo();
		this.clave    = tipo.getClave();
		this.mensajes = mensajes;
	}
	
	public RespuestaWS( Throwable causa) {
		super();
		
		CodigoRespuestaEnum tipo = null;
		
		if ( causa instanceof NegocioException) {
			tipo = ERROR_NEGOCIO;
			
		} else if ( causa instanceof PeticionIncorrectaException) {
			tipo = ERROR_PETICION;
					
		} else if( causa instanceof InfraestructuraException){
			tipo = ERROR_INFRAESTRUCTURA;
			
		} else {
			tipo = ERROR_DESCONOCIDO;
			
		}
		
		this.codigo = tipo.getCodigo();
		this.clave	= tipo.getClave();
		this.mensajes = new ListaMensajes( causa);
		
	}
	
	
	public static RespuestaWS crear( CodigoRespuestaEnum tipo, String llaveMensaje) {
		final RespuestaWS respuesta = new RespuestaWS( tipo);
		respuesta.mensajes.addLlaveMensaje( llaveMensaje);
		
		return respuesta;
	}
	
	public static RespuestaWS crear( CodigoRespuestaEnum tipo, ListaMensajes mensajes){
		return new RespuestaWS( tipo, mensajes);
	}
	
	public static RespuestaWS crear( CodigoRespuestaEnum tipo, String claveCampo, String llaveMensaje) {
		final RespuestaWS respuesta = new RespuestaWS( tipo);
		respuesta.mensajes.addCampoLlaveMensaje( claveCampo, llaveMensaje);
		
		return respuesta;
	}
	
	public static RespuestaWS crear( Throwable causa) {
		return new RespuestaWS( causa);
	}
	
    public static RespuestaWS crear( ) {
		return new RespuestaWS( OPERACION_EXITOSA);
	}
	
	public Integer getCodigo() {	return codigo;	}
	public String getClave() {		return clave;	}
	
	
	public List<MensajeRespuesta> getMensajes() {
		return mensajes.getListado();
	}
	
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}