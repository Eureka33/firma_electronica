package mx.com.neogen.commons.exception;

/**
 * 	Una excepción de infraestructura representa cualquier situación que 
 * no permite que una petición sea atendida. 
 * 
 * @author eurk Soluciones
 */
public class InfraestructuraException extends RuntimeException {

	private static final long serialVersionUID = 1784699269492339461L;

	public InfraestructuraException( String mensaje, Throwable causa) {
		super( mensaje, causa);
	}
	
	public InfraestructuraException( String mensaje) {
		super( mensaje);
	}
}