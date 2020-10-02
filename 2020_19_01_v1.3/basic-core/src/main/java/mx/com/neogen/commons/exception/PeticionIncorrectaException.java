package mx.com.neogen.commons.exception;

import java.util.List;

import com.eurk.core.beans.respuesta.MensajeRespuesta;

/**
 * 	Representa la presencia de situaciones de error en la invocación de un servicio
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 * 
 */
public class PeticionIncorrectaException extends RuntimeException {
	
	private static final long serialVersionUID = 2056667382092781718L;
	
	private final List<MensajeRespuesta> mensajes;
	
	public PeticionIncorrectaException( String mensaje, Throwable causa) {
		super( mensaje, causa);
        this.mensajes = null;
	}
	
	public PeticionIncorrectaException( String mensaje) {
		super( mensaje);
        this.mensajes = null;
	}
	
	public PeticionIncorrectaException( List<MensajeRespuesta> mensajes) {
		super();
		this.mensajes = mensajes;
	}
	
	
	public List<MensajeRespuesta> getMensajes() {
		return mensajes;
	}
	
}