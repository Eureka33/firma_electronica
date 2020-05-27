package mx.com.neogen.commons.logs;

/**
 * 	Una tarea forma parte de una actividad. Es la unidad de trabajo más 
 * pequeña en que puede dividirse un proceso.
 *
 * @author Marco Antonio García García			g2marco@yahoo.com.mx
 */

public class Tarea extends RegistroEvento {

	private static final long serialVersionUID = 2670391040700361473L;
	

	public Tarea() {
		super();
	}
	
	
	
	public static Tarea iniciar(	final String descripcion) {
		final Tarea tarea = new Tarea();
		tarea.setDescripcion( descripcion);
		
		return tarea;
	}
	
}