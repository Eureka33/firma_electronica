package com.eurk.core.beans.respuesta;

public final class RespuestaItemWS<T> extends RespuestaWS {

	private T item;
	
	
	public RespuestaItemWS( T item) {
		super( CodigoRespuestaEnum.OPERACION_EXITOSA);
		
		this.item = item;
	}
		
	public RespuestaItemWS( Throwable causa) {
		super( causa);
	}
	
	
	public static <T> RespuestaItemWS<T> crear( T item) {
		return new RespuestaItemWS<>( item);
	}
	
	public  void setItem( final T item) { 
		this.item = item;
	}

	public T getItem() { 
		return item;
	}
	
}