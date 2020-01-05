package mx.com.neogen.commons.bean;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	<p>
 * 		La identificación de un módulo consta del siguiente conjunto de
 * 		propiedades: 
 * 		
 * 		<ul>
 * 			<li>La clave del módulo </li>
 * 			<li>El nombre descriptivo del módulo</li>
 * 			<li>La prioridad de inicialización solicitada</li>
 * 		</ul>
 * 
 *  	Se asegura que un módulo con prioridad baja sea inicializado antes que
 *  	un módulo con prioridad alta.
 *  	<br>
 *  	No se asegura nada sobre el orden de inicialización de módulos con
 *  	la misma prioridad.
 * 	</p> 
 *  
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */

public class IdentificadorModulo implements Serializable {
	
	private static final long serialVersionUID = 704424454929765244L;

	private String  clave;
	private String nombre;
	private int prioridad;
	
 	
	
	public IdentificadorModulo() {
		super();
	}
	
	public IdentificadorModulo(	final String clave, final String nombre, final int prioridad) {
		super();
		
		this.clave  	=	  clave;
		this.nombre 	=	 nombre;
		this.prioridad 	= prioridad;
	}
	
	
	
	public String getClave() {							return clave;				}
	public void setClave( 		final String key) {		this.clave = key;			}
	
	public String getNombre() { 							return nombre;			}
	public void setNombre(		final String name) {	this.nombre = name;			}

	public int getPrioridad() {							return prioridad;			}
	public void setPrioridad(	final int priority) {	this.prioridad = priority;	}	
	
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
	@Override
	public boolean equals(	final Object object) {
		if ( object == null) {								return false;	}
		if ( this == object) { 								return true;	}
		if ( !(object instanceof IdentificadorModulo)) {	return false;	}
		
		final IdentificadorModulo otroID = (IdentificadorModulo) object; 
		
		if ( clave == null) {								return false;	}
		
		return clave.equals( otroID.clave) && (prioridad == otroID.prioridad);		
	}
	
	
	@Override
	public int hashCode() {
		return (clave == null) ? 0 : clave.hashCode();
	}
	
}