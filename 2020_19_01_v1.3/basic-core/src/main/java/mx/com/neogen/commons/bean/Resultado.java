package mx.com.neogen.commons.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  Esta clase representa el resultado de una operacion. Opcionalmente el
 * usuario puede registrar el progreso de la operación agregando mensajes a 
 * un listado.   
 *
 *	Si la operacion termina con exito ( setValor( valor) ):
 *		error		<= false;
 *		mensajes	<= listado de las operaciones realizadas
 *		resultado	<= bean resultante del tipo parametrizado
 *
 *		errorGrave	<= false
 *		causa		<= null
 *
 * 	Si la operacion termina con error, es decir, cualquier condición que el 
 * usuario puede arreglar ( setError( mensaje) ) :
 *		error		<= true
 *		mensajes	<= listado de las operaciones y/o el mensaje de error
 *		resultado  	<= null;
 *
 *		errorGrave	<= false
 *		causa		<= null
 *		
 *	Si al ejecutar la operacion ocurre un error grave (cualquier situación
 * fuera del ambito del usuario)
 * 		error		<= true
 * 		mensajes	<= log de las operaciones hasta donde la operacion falló
 *		resultado  	<= null;
 *
 *		errorGrave	<= true
 *		cause		<= instancia de Throwable con la información de la excepcion
 *		
 * @author Marco Antonio García García	    g2marco@yahoo.com.mx
 */
public class Resultado <T> implements Serializable {
	
	private static final long serialVersionUID = 6271409254449041684L;
	
	private boolean ok;		    	// exito o fracaso de la operacion
	
	private List<Mensaje> mensajes;	// log de operaciones
	private T valor;		    	// producto de la operacion

	private boolean errorGrave;		// bandera de error grave
	private Throwable causa;	    // causa del error


	public Resultado() {
		super();
	}

	
	public static <T> Resultado<T> crearInstancia(	final T objeto) {
		final Resultado<T> resultado = new Resultado<T>();
		
		resultado.setResultado( objeto);
		
		return resultado;
	}

	/**
	 *	Devuelve true si el resultado es erroneo
	 */
	public boolean isError() {
		return !this.ok;
	}

	/**
	 *	Devuelve true si la operacion resultó en un error grave
	 * (errores fuera del control del usuario, cosas que el usuario no puede
	 * arreglar)
	 */
	public boolean isErrorGrave() {
		return this.errorGrave;
	}
	
	/**
	 *	Establece el producto de la operacion. 
	 *	Al establecer un resultado no nulo, se presupone que el resultado de
	 * la operacion es exitoso.
	 *  Si el valor asignado es nulo, el resultado de la operación será 
	 * establecido como erroneo. 
	 */
	public void setResultado( T valor) {
		this.ok = valor != null;
		this.valor = valor;
	}

	/**
	 *	Establece un resultado erroneo acompañado de un mensaje descriptivo
	 *
	 * @param mensaje descripcion del error
	 */
	public void setError( String llaveMensaje, Object... parametros) {
		this.ok = false;
		agregarMensaje(llaveMensaje, parametros);
	}

	/**
	 *  Establece un resultado erroneo acompañado de un mensaje descriptivo
	 * y una causa.
	 *
	 * @param mensaje descripción del error
	 * @param cause   excepción que origino el error
	 */
	public void setErrorGrave( String mensaje, Throwable causa) {
		setError( mensaje);
		
		this.errorGrave = true;
		this.causa = causa;
	}

	/**
	 *	Agrega una linea al texto del mensaje, agrega un salto de línea al final
	 */
	public void agregarMensaje( String llaveMensaje, Object... parametros) {
		getMensajes().add( new Mensaje(llaveMensaje, parametros));
	}

	/**
	 *	Devuelve el log de mensaje generado durante la operacion
	 */
	public List<Mensaje>  getMensajes() {
		if ( mensajes == null) {
			mensajes = new ArrayList<Mensaje>();
		}
		return mensajes;
	}

	/**
	 *	Recupera el producto de la operacion
	 */
	public T getResultado() {
		return this.valor;
	}

	/**
	 * 	Recupera la causa de un error grave
	 */
	public Throwable getCausa() {
		return this.causa;
	}

}

