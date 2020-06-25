package mx.com.neogen.commons.exception;

import java.util.ArrayList;
import java.util.List;

import com.eurk.core.beans.respuesta.MensajeCampoRespuesta;
import com.eurk.core.beans.respuesta.MensajeRespuesta;

/**
 * Una excepción de negocio representa cualquier situación de negocio que impide la ejecución de una operación.
 * 
 * @author eurk Soluciones
 */
public class NegocioException extends RuntimeException {
	
	private static final long serialVersionUID = 4928315585173182773L;

	final private List<MensajeRespuesta> mensajes;
	
	
	public NegocioException( String mensaje) {
		super( mensaje);
        this.mensajes = null;
	}
	
	public NegocioException( String mensaje, Throwable causa) {
		super( mensaje, causa);
        this.mensajes = null;
	}
	
	public NegocioException( String claveCampo, String llaveMensaje) {
		this.mensajes = new ArrayList<>();
		this.mensajes.add( MensajeCampoRespuesta.crearMensajeConLlave( claveCampo, llaveMensaje));
	}

	public NegocioException( MensajeRespuesta mensaje) {
		this.mensajes = new ArrayList<>();
		this.mensajes.add( mensaje);
	}
	
	public NegocioException(List<MensajeRespuesta> mensajes) {
		this.mensajes = mensajes;
	}

	
	public List<MensajeRespuesta> getMensajes() {
		return mensajes;
	}

}