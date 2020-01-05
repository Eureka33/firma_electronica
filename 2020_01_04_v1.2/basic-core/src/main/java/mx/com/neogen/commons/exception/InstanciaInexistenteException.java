package mx.com.neogen.commons.exception;

/**
 * 	Arrojada cuando se solicita una instancia de un objeto no definido o 
 * declarado previamente.
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public class InstanciaInexistenteException extends RuntimeException {

	private static final long serialVersionUID = -2311860211213061496L;
	
	public InstanciaInexistenteException( String mensaje) {
		super( mensaje);
	}
	
	public InstanciaInexistenteException( String mensaje, Throwable causa) {
		super( mensaje, causa);
	}

}
