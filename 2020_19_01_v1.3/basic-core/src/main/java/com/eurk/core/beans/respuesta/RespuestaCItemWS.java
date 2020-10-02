package com.eurk.core.beans.respuesta;

import java.util.HashMap;
import java.util.Map;

public final class RespuestaCItemWS extends RespuestaWS {

	private Map<String, Object>	items;
	
	
	public RespuestaCItemWS( CodigoRespuestaEnum tipo) {
		super( tipo);
	}
	
	
	public static RespuestaCItemWS crear( CodigoRespuestaEnum tipo) {
		return new RespuestaCItemWS( tipo);
	}
	
	public void addItem( final String key, final Object item) {
		if ( items == null) {
			items = new HashMap<>();
		}
		
		items.put( key, item);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getItem( 	final String key) {
		return (T) this.items.get( key);
	}
	
	public Map<String, Object> getItems() {
		return this.items;
	}
		
}