package mx.com.neogen.commons.exception;

/**
 * 	Arrojada al ocurrir cualquier error durante una operación de reflexión.
 * 
 *  Esta excepción ha sido creada para envolver (to wrapp) las diversas  
 * excepciones arrojadas por el API de reflexión.
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = -8640982335304539498L;

	public ReflectionException(String mensaje) {
		super( mensaje);
	}
	
	public ReflectionException(String mensaje, Throwable causa) {
		super( mensaje, causa);
	}
}
