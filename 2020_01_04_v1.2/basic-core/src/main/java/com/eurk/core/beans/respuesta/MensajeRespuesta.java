package com.eurk.core.beans.respuesta;

import mx.com.neogen.commons.util.UtilReflection;


public class MensajeRespuesta {

	private final TipoMensajeEnum tipo;			// tipo de mensaje: llave, texto, template
	private final String mensaje;				// cuerpo de mensaje
	
	
	protected MensajeRespuesta( final TipoMensajeEnum tipo, final String mensaje) {
		super();
		
		this.tipo 	 = tipo;
		this.mensaje = mensaje;
	}
	
	
	public static MensajeRespuesta crearMensajeConLlave( String llave) {
		return new MensajeRespuesta( TipoMensajeEnum.KEY, llave);
	}
	
	public static MensajeRespuesta crearMensajeConTexto( String texto) {
		return new MensajeRespuesta( TipoMensajeEnum.TEXT, texto);
	}
	
	public static MensajeRespuesta crearMensajeConStackTrace( String texto) {
		return new MensajeRespuesta( TipoMensajeEnum.STACK, texto);
	}
	
	public TipoMensajeEnum getTipo() {	return tipo;		}
	public String getMensaje() {		return mensaje;		}

	
	@Override
	public boolean equals(Object object) {
		if( this == object) { return true; 	}
		if( object == null) { return false; }
		
		if ( ! (object instanceof MensajeRespuesta)) { return false;	}
		
		MensajeRespuesta instancia = (MensajeRespuesta) object;
		
		if ( tipo == null || mensaje == null) { return false; }
		
		return tipo.equals( instancia.tipo) && mensaje.equals( instancia.mensaje);
	}

    @Override
    public int hashCode() {
        return (int) (tipo.hashCode() * mensaje.hashCode());
    }
    
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}

}