package mx.com.neogen.commons.exception;

/**
 * 	Arrojada por metodos que todavía no cuentan con una implementación
 * adecuada. 
 * 	Se ha pensado como un mecanismo para permitir la implementación paulatina
 * de funcionalidad, evitando la necesidad de proveer implementaciones 
 * inadecuadas.
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public class MetodoNoImplementadoException extends RuntimeException {

	private static final long serialVersionUID = -4082313355622265949L;

	public MetodoNoImplementadoException( String mensaje) {
		super( mensaje);
	}
	
	public MetodoNoImplementadoException( String mensaje, Throwable causa) {
		super( mensaje, causa);
	}
	
}
