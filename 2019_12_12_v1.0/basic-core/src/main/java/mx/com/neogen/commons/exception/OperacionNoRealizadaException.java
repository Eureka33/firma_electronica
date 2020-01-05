package mx.com.neogen.commons.exception;

/**
 * 	Arrojada cuando una operación no es terminada por alguna causa ajena 
 * al usuario.
 *
 * @author Marco Antonio García García	    g2marco@yahoo.com.mx
 */
public class OperacionNoRealizadaException extends RuntimeException {

	private static final long serialVersionUID = 2986959751555480881L;

	public OperacionNoRealizadaException( String mensaje) {
		super(mensaje);
	}

	public OperacionNoRealizadaException( String mensaje, Throwable causa) {
		super(mensaje, causa);
	}

}
