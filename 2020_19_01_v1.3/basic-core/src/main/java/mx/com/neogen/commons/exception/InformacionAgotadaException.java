package mx.com.neogen.commons.exception;

public class InformacionAgotadaException extends RuntimeException {

	private static final long serialVersionUID = 1561584785017175032L;

	public InformacionAgotadaException() {
		super();
	}

	public InformacionAgotadaException( final String mensaje) {
		super( mensaje);
	}
	
}
