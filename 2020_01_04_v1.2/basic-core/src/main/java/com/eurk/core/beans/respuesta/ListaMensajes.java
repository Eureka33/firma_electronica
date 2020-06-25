package com.eurk.core.beans.respuesta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx.com.neogen.commons.exception.NegocioException;
import mx.com.neogen.commons.exception.PeticionIncorrectaException;
import mx.com.neogen.commons.util.UtilReflection;
import mx.com.neogen.commons.util.UtilText;


public class ListaMensajes implements Serializable {
	
	private static final long serialVersionUID = 8521370601770567224L;
	
	private List<MensajeRespuesta> listado;
    private int maxSize;
	
	
	public ListaMensajes() {
		this( Integer.MAX_VALUE);
	}
	
    public ListaMensajes( int maxSize) {
        super();
        this.listado = new ArrayList<>();
        this.maxSize = maxSize;
    }
    
	
	public ListaMensajes( Throwable causa) {
		this();
		
		if ( causa.getMessage() != null) {
			if ( (causa instanceof PeticionIncorrectaException) || (causa instanceof NegocioException) ) {
                if( causa.getMessage() != null) {
                    addLlaveMensaje( causa.getMessage());
                }
			} else {
				addTextoMensaje( causa.getMessage());
			}
		}
		
		if ( causa instanceof PeticionIncorrectaException) {	
			PeticionIncorrectaException exception = ((PeticionIncorrectaException) causa);
			
			if ( exception.getMensajes() != null ) {
				listado.addAll( exception.getMensajes());
			}
		}
		if ( causa instanceof NegocioException) {	
			NegocioException exception = ((NegocioException) causa);
			
			if ( exception.getMensajes() != null ) {
				listado.addAll( exception.getMensajes());
			}
		}
			
		if ( causa.getCause() != null) {
			addTextoMensaje( causa.getCause().getMessage());		
		}
		
		if ( !(causa instanceof PeticionIncorrectaException) && !(causa instanceof NegocioException) ) {
			addStackTraceMensaje( causa);
		}
	
	}
	
	public void setListado(List<MensajeRespuesta> listado) {
		this.listado = listado;
	}
	
	public List<MensajeRespuesta> getListado() {
		return listado;
	}
 	
	public int size() {
		return listado.size();
	}
	
	public void add( MensajeRespuesta mensaje) {
		if ( mensaje != null) {		listado.add( mensaje);	}
        failIfMaxSize();
	}
	
	public void addTextoMensaje( final String textoMensaje) {
		listado.add( MensajeRespuesta.crearMensajeConTexto( textoMensaje));
        failIfMaxSize();
	}
	
	public void addStackTraceMensaje( final Throwable causa) {
		listado.add( MensajeRespuesta.crearMensajeConStackTrace( UtilText.stackTraceToString( causa)));
        failIfMaxSize();
	}
	
	public void addLlaveMensaje( final String llaveMensaje) {
		listado.add( MensajeRespuesta.crearMensajeConLlave( llaveMensaje));
        failIfMaxSize();
	}
	
	public void addCampoTextoMensaje( final String claveCampo, final String textoMensaje) {
		listado.add( MensajeCampoRespuesta.crearMensajeConTexto( claveCampo, textoMensaje));
        failIfMaxSize();
	}
	
	public void addCampoLlaveMensaje( final String claveCampo, final String llaveMensaje) {
		listado.add( MensajeCampoRespuesta.crearMensajeConLlave( claveCampo, llaveMensaje));
        failIfMaxSize();
	}
	
	public void failIfNoEmpty() {
		if ( listado.size() > 0 ) {
			throw new PeticionIncorrectaException( listado);
		}
	}
    
    private void failIfMaxSize() {
		if ( listado.size() >= maxSize ) {
			throw new PeticionIncorrectaException( listado);
		}
	}
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}
