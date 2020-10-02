package mx.com.neogen.commons.logs;

import java.util.ArrayList;
import java.util.List;

public class Proceso extends RegistroEvento {

	private static final long serialVersionUID = 4771091581135182758L;

	private List<Actividad> actividades;
	
	
	
	public Proceso() {
		super();
		
		actividades = new ArrayList<Actividad>();
	}
	
	
	
	public static Proceso iniciar(	final String descripcion) {
		final Proceso proceso = new Proceso();
		proceso.setDescripcion( descripcion);

		return proceso;
	}
	
	public void terminar() {
		boolean hayErrores = false;
		for ( Actividad actividad : actividades) {
			if ( actividad.isExito() == false) {
				hayErrores = true;
			}
		}
		
		if( ! hayErrores) {
			terminarOK( "Actividad terminada con éxito");
		
		} else {
			terminarError( "Actividad terminada con errores");
		}

	}
	
	public Actividad iniciarActividad( 	final String descripcion) {
		final Actividad actividad = Actividad.iniciar( descripcion);
		actividades.add(actividad);
		
		return actividad;
	}
	

	
	public List<Actividad> getActividades() {							return actividades;				}
	public void setActividades(	final List<Actividad> actividades) {	this.actividades = actividades;	}
	
}