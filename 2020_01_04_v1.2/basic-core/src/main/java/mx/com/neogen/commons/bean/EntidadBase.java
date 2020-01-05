package mx.com.neogen.commons.bean;

import java.io.Serializable;

/**
 * 	Una entidad es todo objeto que tiene: 
 * 		1.- Una clave o identificador que lo define de forma única
 * 		2.- Un nombre o descripción corta que es mostrado al usuario 
 * 		3.- Una descripcion o mensaje de tooltip que puede mostrarse al 
 * 			usuario al pasar por el elemento
 * 		4.- Una bandera de activo o inactivo
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */

public abstract class EntidadBase<ID extends Serializable> extends DataBaseBean<ID> {
	
	private static final long serialVersionUID = -2097067257690210767L;
	
	protected String 	  nombre;
	protected String descripcion;
	protected boolean 	  activo;

	
	public EntidadBase( 	final ID id) {
		super( id);
		activo = true;
	}
	
	public String getNombre() {							return nombre;			}
	public void setNombre(		final String value) {	this.nombre = value;	}

	public String getDescripcion() {					return descripcion;		}
	public void setDescripcion(	final String value) {	this.descripcion = value;	}

	public boolean isActivo() {							return activo;			}
	public void setActivo(		final boolean value) {	this.activo = value;	}
	
}