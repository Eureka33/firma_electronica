package mx.com.neogen.commons.exception;

public class ParametroNoExisteException extends RuntimeException {

	private static final long serialVersionUID = -552401939166915742L;
	
	public ParametroNoExisteException( String msg) {
		super(msg);
	}

}
