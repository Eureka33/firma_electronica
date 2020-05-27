package mx.com.neogen.commons.exception;

/**
 *  Arrojada cuando un recurso solicitado no es encontrado por alguna causa
 * 
 * @author Marco Antonio García García	    g2marco@yahoo.com.mx
 */
public class RecursoNoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 607637568854937545L;

	public RecursoNoEncontradoException( String mensaje) {
		super( mensaje);
	}

	public RecursoNoEncontradoException( String mensaje, Throwable causa) {
		super(mensaje, causa);
	}

}
