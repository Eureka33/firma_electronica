package mx.com.neogen.commons.exception;

/**
 * 	Excepción arrojada cuando una unidad de informacion no tiene el formato
 * adecuado para realizar una tarea.
 *
 * @author Marco Antonio Garcia Garcia      g2marco@yahoo.com.mx
 */
public class FormatoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = -4104347085584253713L;

	public FormatoInvalidoException( String mensaje) {
		super( mensaje);
	}
	
	public FormatoInvalidoException( String mensaje, Throwable causa) {
		super( mensaje, causa);
	}

}
