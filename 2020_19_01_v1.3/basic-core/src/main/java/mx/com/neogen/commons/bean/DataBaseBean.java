package mx.com.neogen.commons.bean;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	Base para todo bean que transporta informacion hacia y desde una base de 
 * datos (bean de entidad). 	
 * 	Todo bean de entidad debe contar con un identificador (id) o llave
 * primaria.
 *  Todo bean de entidad debe tener la capacidad de clonarse, con el fin de 
 * contar con una copia para operaciones de modificación.
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public abstract class DataBaseBean<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = 6798057107851166677L;
	
	protected ID id;					

	
	public DataBaseBean( ID id) {
		this.id = id;
	}
	
	
	
	public ID getId() {						return id;		}
	public void setId(	final ID id) {		this.id = id;	}
	
	/**
	 * 	Si el identificador no es nulo, devuelve su hashcode, de lo contrario
	 * devuelve el hascode de la instancia, tal como esta implementado en Object
	 */
	@Override
	public int hashCode() {
		return (this.id == null)? super.hashCode() : id.hashCode();
	}
	
	/**
	 * 	Devuelve true si ambas instancias tienen el mismo identificador.
	 * 
	 */
	@Override
	public boolean equals( Object obj) {
		if ( obj == null) {	return false; 	}
		if ( this == obj) {	return true;	}
		if ( (obj instanceof DataBaseBean<?>) == false) {	return false;	}
			
		return 
			(this.id == null)? false: this.id.equals( ((DataBaseBean<?>) obj).id);
	}

	/**
	 * 	Crea y devuelve una cadena con todas las propiedades de la instancia, 
	 * excepto aquellas propiedades marcadas con la anotacion @ExcludeFromToString 
	 */
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}