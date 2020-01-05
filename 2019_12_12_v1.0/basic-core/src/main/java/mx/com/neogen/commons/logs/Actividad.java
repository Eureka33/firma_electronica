package mx.com.neogen.commons.logs;

import java.util.ArrayList;
import java.util.List;

public class Actividad extends RegistroEvento {

	private static final long serialVersionUID = 4902247674387841414L;

	private List<Tarea> tareas;
	
	
	public Actividad() {
		super();
		
		tareas = new ArrayList<Tarea>();
	}
	
	
	
	public static Actividad iniciar(	final String descripcion) {
		final Actividad actividad = new Actividad();
		actividad.setDescripcion( descripcion);

		return actividad;
	}


	public void terminar() {
		boolean hayErrores = false;
		for ( Tarea tarea : tareas) {
			if ( tarea.isExito() == false) {
				hayErrores = true;
			}
		}
		
		if( ! hayErrores) {
			terminarOK( "Actividad terminada con éxito");
		
		} else {
			terminarError( "Actividad terminada con errores");
		}

	}

	
	public Tarea iniciarTarea( final String descripcionTarea) {
		final Tarea tarea = Tarea.iniciar( descripcionTarea);
		tareas.add ( tarea);
		
		return tarea;
	}
	
	public List<Tarea> getTareas() {						return tareas;			}
	public void setTareas(	final List<Tarea> tareas) {		this.tareas = tareas;	}
	
}